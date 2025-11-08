import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import { db } from './index';

interface SecurityEvent {
    userId: string;
    eventType: string;
    details: string;
    deviceInfo?: {
        [key: string]: string;
    };
    timestamp: admin.firestore.Timestamp;
}

export const logSecurityEvent = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError(
            'unauthenticated',
            'Must be authenticated to log security events'
        );
    }

    const { eventType, details, deviceInfo } = data;
    if (!eventType || !details) {
        throw new functions.https.HttpsError(
            'invalid-argument',
            'Event type and details are required'
        );
    }

    const event: SecurityEvent = {
        userId: context.auth.uid,
        eventType,
        details,
        deviceInfo,
        timestamp: admin.firestore.Timestamp.now()
    };

    await db.collection('security_logs').add(event);
});

export const monitorSuspiciousActivity = functions.firestore
    .document('security_logs/{logId}')
    .onCreate(async (snapshot, context) => {
        const event = snapshot.data() as SecurityEvent;
        const userId = event.userId;

        try {
            // Check for multiple failed login attempts
            if (event.eventType === 'LOGIN_FAILED') {
                const recentFailures = await db.collection('security_logs')
                    .where('userId', '==', userId)
                    .where('eventType', '==', 'LOGIN_FAILED')
                    .where('timestamp', '>', 
                        admin.firestore.Timestamp.fromDate(
                            new Date(Date.now() - 15 * 60 * 1000)
                        )
                    )
                    .get();

                if (recentFailures.size >= 5) {
                    // Lock the account temporarily
                    await admin.auth().updateUser(userId, {
                        disabled: true
                    });

                    // Create a notification
                    await db.collection('notifications').add({
                        userId,
                        title: 'Account Temporarily Locked',
                        message: 'Multiple failed login attempts detected. Please reset your password.',
                        type: 'SECURITY',
                        priority: 'HIGH',
                        createdAt: admin.firestore.Timestamp.now()
                    });
                }
            }

            // Monitor document access patterns
            if (event.eventType === 'DOCUMENT_ACCESS') {
                const recentAccesses = await db.collection('security_logs')
                    .where('userId', '==', userId)
                    .where('eventType', '==', 'DOCUMENT_ACCESS')
                    .where('timestamp', '>', 
                        admin.firestore.Timestamp.fromDate(
                            new Date(Date.now() - 5 * 60 * 1000)
                        )
                    )
                    .get();

                if (recentAccesses.size > 50) {
                    // Log suspicious activity
                    await db.collection('security_alerts').add({
                        userId,
                        type: 'SUSPICIOUS_ACCESS_PATTERN',
                        details: 'High frequency document access detected',
                        timestamp: admin.firestore.Timestamp.now()
                    });
                }
            }
        } catch (error) {
            console.error('Error monitoring security events:', error);
        }
    });

export const cleanupOldSecurityLogs = functions.pubsub
    .schedule('every 168 hours')
    .onRun(async (context) => {
        const ninetyDaysAgo = admin.firestore.Timestamp.fromDate(
            new Date(Date.now() - 90 * 24 * 60 * 60 * 1000)
        );

        try {
            const oldLogs = await db.collection('security_logs')
                .where('timestamp', '<', ninetyDaysAgo)
                .get();

            const batch = db.batch();
            oldLogs.docs.forEach((doc) => {
                batch.delete(doc.ref);
            });

            await batch.commit();
            console.log(`Deleted ${oldLogs.size} old security logs`);
        } catch (error) {
            console.error('Error cleaning up security logs:', error);
        }
    });