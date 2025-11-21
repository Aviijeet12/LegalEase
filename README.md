# LawClientAuth Android App

Simple Android app in Kotlin with XML that provides a combined Login/Sign Up screen where the user can toggle between **Lawyer** and **Client** roles.

## Features
- Single screen with a toggle for Lawyer / Client role
- Mode switch between Login and Sign Up
- Shows confirm password field only in Sign Up mode
- Ready to plug in your own backend auth logic later

## How to Run
1. Open this folder in **Android Studio** (recommended).
2. Let Gradle sync the project.
3. Run the `app` configuration on an emulator or device.

Alternatively, from a terminal with Android SDK/Gradle set up:

```bash
./gradlew :app:assembleDebug
```

Then install the generated APK from `app/build/outputs/apk/debug/` on your device/emulator.
