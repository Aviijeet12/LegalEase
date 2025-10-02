# LegalEase 🏛️⚖️

A comprehensive legal services platform connecting clients with lawyers through modern mobile technology.

## 🌟 Features

### For Clients
- **Easy Lawyer Search**: Find qualified lawyers by specialization and location
- **Case Management**: Track case progress and important dates
- **Document Upload**: Securely upload and manage legal documents
- **Real-time Chat**: Direct communication with assigned lawyers
- **Multi-language Support**: Available in multiple Indian languages
- **Payment Integration**: Secure online payment system

### For Lawyers
- **Client Management**: Organize and manage client cases efficiently
- **Calendar Integration**: Track hearings and important deadlines
- **Document Management**: Store and organize case documents
- **Analytics Dashboard**: View practice insights and performance metrics
- **Communication Tools**: Chat system for client interaction
- **Case Timeline**: Visual representation of case progress

### For Administrators
- **User Management**: Manage lawyers, clients, and system users
- **System Analytics**: Monitor platform usage and performance
- **Content Management**: Manage legal articles and knowledge base
- **System Logs**: Track system activities and troubleshoot issues

## 🏗️ Technical Architecture

### Android Application
- **Language**: Kotlin
- **Architecture**: MVVM with ViewBinding
- **UI Framework**: Material Design Components
- **Networking**: Retrofit 2.9.0 + OkHttp
- **Authentication**: JWT Token-based
- **Database**: Room (Local) + Firebase (Cloud)
- **Real-time**: Socket.io for chat and notifications

### Backend Server
- **Runtime**: Node.js with Express 5.1.0
- **Database**: MongoDB with Mongoose 8.18.3
- **Authentication**: JWT with bcryptjs
- **File Storage**: Cloudinary for document management
- **Real-time**: Socket.io 4.8.1 for live chat
- **Email**: Nodemailer for notifications
- **Security**: CORS, helmet, rate limiting

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Node.js 16+ and npm
- MongoDB (local or cloud)
- Git

### Backend Setup
```bash
cd NyayaSetu-backend
npm install
cp .env.example .env
# Configure your MongoDB URI, JWT secret, and Cloudinary credentials in .env
npm start
```

### Android App Setup
```bash
# Open the project in Android Studio
# Sync project with Gradle files
# Run the app on emulator or device
./gradlew assembleDebug
```

## 📱 App Structure

```
app/
├── src/main/java/com/nyayasetu/
│   ├── app/                    # Activities and Fragments
│   ├── adapters/              # RecyclerView Adapters
│   ├── models/                # Data Models
│   ├── network/               # API Services
│   ├── firebase/              # Firebase Integration
│   └── payment/               # Payment Integration
├── res/
│   ├── layout/                # XML Layout Files
│   ├── drawable/              # Images and Drawables
│   ├── values/                # Colors, Strings, Styles
│   └── menu/                  # Navigation Menus
```

## 🔧 Backend Structure

```
NyayaSetu-backend/
├── config/                    # Database and service configs
├── controllers/               # Business logic
├── models/                    # MongoDB schemas
├── routes/                    # API endpoints
├── middleware/                # Authentication and validation
├── utils/                     # Helper functions
└── uploads/                   # File storage
```

## 🌐 API Endpoints

### Authentication
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `GET /auth/profile` - Get user profile

### Cases
- `GET /cases` - Get user cases
- `POST /cases` - Create new case
- `PUT /cases/:id` - Update case
- `DELETE /cases/:id` - Delete case

### Documents
- `POST /documents/upload` - Upload document
- `GET /documents/:caseId` - Get case documents
- `DELETE /documents/:id` - Delete document

### Chat
- `GET /chat/conversations` - Get conversations
- `POST /chat/send` - Send message
- WebSocket events for real-time messaging

## 🛡️ Security Features

- JWT-based authentication
- Password encryption with bcrypt
- Input validation and sanitization
- CORS protection
- Rate limiting
- Secure file upload with validation
- Environment variable configuration

## 🎨 UI/UX Features

- Modern Material Design 3
- Dark/Light theme support
- Multi-language localization
- Responsive layout for tablets
- Smooth animations and transitions
- Accessibility support

## 📊 Analytics & Monitoring

- User activity tracking
- Case progress analytics
- System performance monitoring
- Error logging and reporting

## 🧪 Testing

```bash
# Run Android tests
./gradlew test

# Run backend tests
cd NyayaSetu-backend
npm test
```

## 📦 Deployment

### Backend Deployment (Heroku/Railway)
```bash
git subtree push --prefix=NyayaSetu-backend heroku main
```

### Android App (Google Play Store)
```bash
./gradlew bundleRelease
# Upload AAB to Play Console
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Avijit Singh** - Lead Developer

## 🙏 Acknowledgments

- Material Design Components
- Firebase Team
- Node.js Community
- Legal professionals who provided domain expertise

## 📞 Support

For support, email support@legalease.com or join our Slack channel.

---

**LegalEase** - Making legal services accessible to everyone 🚀
