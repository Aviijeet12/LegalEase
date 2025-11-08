import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import * as sgMail from '@sendgrid/mail';
import { db } from './index';

// Initialize SendGrid
sgMail.setApiKey(functions.config().sendgrid.key);

export const sendEmailNotification = functions.firestore
    .document('email_notifications/{notificationId}')
    .onCreate(async (snapshot, context) => {
        const notification = snapshot.data();
        if (!notification) return;

        try {
            const msg = {
                to: notification.email,
                from: functions.config().email.from_address,
                subject: notification.title,
                text: notification.message,
                html: `<div>
                    <h2>${notification.title}</h2>
                    <p>${notification.message}</p>
                </div>`
            };

            await sgMail.send(msg);
            
            // Update notification status
            await snapshot.ref.update({
                status: 'sent',
                sentAt: admin.firestore.FieldValue.serverTimestamp()
            });
        } catch (error) {
            console.error('Error sending email:', error);
            await snapshot.ref.update({
                status: 'error',
                error: error.message
            });
        }
    });

export const sendPushNotification = functions.firestore
    .document('notifications/{notificationId}')
    .onCreate(async (snapshot, context) => {
        const notification = snapshot.data();
        if (!notification || !notification.userId) return;

        try {
            // Get user's FCM token
            const userDoc = await db.collection('users')
                .doc(notification.userId)
                .get();
            
            if (!userDoc.exists) return;
            const userData = userDoc.data();
            if (!userData?.fcmToken) return;

            const message = {
                token: userData.fcmToken,
                notification: {
                    title: notification.title,
                    body: notification.message
                },
                data: {
                    type: notification.type || 'GENERAL',
                    id: context.params.notificationId,
                    click_action: 'FLUTTER_NOTIFICATION_CLICK'
                }
            };

            await admin.messaging().send(message);

            // Update notification status
            await snapshot.ref.update({
                status: 'sent',
                sentAt: admin.firestore.FieldValue.serverTimestamp()
            });
        } catch (error) {
            console.error('Error sending push notification:', error);
            await snapshot.ref.update({
                status: 'error',
                error: error.message
            });
        }
    });

export const cleanupOldNotifications = functions.pubsub
    .schedule('every 24 hours')
    .onRun(async (context) => {
        const thirtyDaysAgo = admin.firestore.Timestamp.fromDate(
            new Date(Date.now() - 30 * 24 * 60 * 60 * 1000)
        );

        try {
            // Delete old notifications
            const oldNotifications = await db.collection('notifications')
                .where('createdAt', '<', thirtyDaysAgo)
                .get();

            const batch = db.batch();
            oldNotifications.docs.forEach((doc) => {
                batch.delete(doc.ref);
            });

            await batch.commit();
            console.log(`Deleted ${oldNotifications.size} old notifications`);
        } catch (error) {
            console.error('Error cleaning up notifications:', error);
        }
    });