import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';

// Initialize Firebase Admin
admin.initializeApp();

// Import function modules
import * as notifications from './notifications';
import * as documents from './documents';
import * as security from './security';
import * as users from './users';

// Export functions
export const notificationFunctions = notifications;
export const documentFunctions = documents;
export const securityFunctions = security;
export const userFunctions = users;

// Initialize Firestore and Storage
export const db = admin.firestore();
export const storage = admin.storage();
export const auth = admin.auth();