import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import { Storage } from '@google-cloud/storage';
import * as sharp from 'sharp';
import { db, storage } from './index';

const gcs = new Storage();

interface DocumentMetadata {
    contentType: string;
    size: number;
    createdAt: admin.firestore.Timestamp;
    updatedAt: admin.firestore.Timestamp;
    version: number;
    userId: string;
    isEncrypted: boolean;
}

export const processUploadedDocument = functions.storage
    .object()
    .onFinalize(async (object) => {
        if (!object.name) return;

        const filePath = object.name;
        const contentType = object.contentType || '';
        const userId = object.metadata?.userId;

        if (!userId) {
            console.error('No user ID in metadata');
            return;
        }

        try {
            // Create document metadata
            const metadata: DocumentMetadata = {
                contentType,
                size: parseInt(object.size || '0'),
                createdAt: admin.firestore.Timestamp.now(),
                updatedAt: admin.firestore.Timestamp.now(),
                version: 1,
                userId,
                isEncrypted: object.metadata?.encrypted === 'true'
            };

            // Store metadata in Firestore
            await db.collection('documents')
                .doc(filePath)
                .set(metadata);

            // Generate thumbnail for images
            if (contentType.startsWith('image/')) {
                await generateThumbnail(filePath);
            }

            // Log access
            await logDocumentAccess(filePath, userId, 'upload');
        } catch (error) {
            console.error('Error processing document:', error);
        }
    });

export const handleDocumentDeletion = functions.storage
    .object()
    .onDelete(async (object) => {
        if (!object.name) return;

        try {
            // Delete metadata from Firestore
            await db.collection('documents')
                .doc(object.name)
                .delete();

            // Delete thumbnails if they exist
            if (object.contentType?.startsWith('image/')) {
                const thumbnailPath = `thumbnails/${object.name}`;
                await storage.bucket().file(thumbnailPath).delete();
            }
        } catch (error) {
            console.error('Error handling document deletion:', error);
        }
    });

async function generateThumbnail(filePath: string): Promise<void> {
    const bucket = storage.bucket();
    const tempFilePath = `/tmp/${filePath.split('/').pop()}`;
    const thumbnailPath = `thumbnails/${filePath}`;

    try {
        // Download original file
        await bucket.file(filePath).download({
            destination: tempFilePath
        });

        // Generate thumbnail
        await sharp(tempFilePath)
            .resize(200, 200, {
                fit: 'inside',
                withoutEnlargement: true
            })
            .toFile(`${tempFilePath}_thumb`);

        // Upload thumbnail
        await bucket.upload(`${tempFilePath}_thumb`, {
            destination: thumbnailPath,
            metadata: {
                contentType: 'image/jpeg'
            }
        });
    } catch (error) {
        console.error('Error generating thumbnail:', error);
        throw error;
    }
}

async function logDocumentAccess(
    documentPath: string,
    userId: string,
    action: string
): Promise<void> {
    await db.collection('document_access_logs').add({
        documentPath,
        userId,
        action,
        timestamp: admin.firestore.FieldValue.serverTimestamp(),
        userAgent: 'cloud-function'
    });
}