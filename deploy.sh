# Install dependencies for functions
cd functions
npm install

# Set Firebase configuration
firebase functions:config:set sendgrid.key="YOUR_SENDGRID_API_KEY"
firebase functions:config:set email.from_address="notifications@legalease.com"

# Deploy security rules first
firebase deploy --only firestore:rules,storage:rules

# Deploy functions
firebase deploy --only functions

# Deploy indexes
firebase deploy --only firestore:indexes