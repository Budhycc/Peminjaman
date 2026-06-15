# Peminjaman App

A modern Android application for managing asset/equipment borrowing (Peminjaman) and returns, built with Kotlin and Jetpack Compose.

## 🚀 Features

- **User Authentication**: Secure login system with token-based authentication.
- **Asset Management**: 
  - Browse a list of available items for borrowing.
  - View detailed information for each item.
- **QR Code Scanner**: Integrated camera functionality using **CameraX** and **Google ML Kit** for quick asset identification.
- **Borrowing Process**: Streamlined flow for borrowing items via QR code or manual selection.
- **Asset Return**: Dedicated screen for managing the return of borrowed items.
- **Borrowing History**: Track all previous borrowing activities in the "Riwayat" screen.
- **User Profile**: View and manage user account details.

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **QR Scanning**: [CameraX](https://developer.android.com/training/camerax) & [ML Kit](https://developers.google.com/ml-kit)
- **Serialization**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Storage**: [TokenManager](app/src/main/java/com/pinjam/peminjaman/TokenManager.kt) for SharedPreferences session management.

## 📂 Project Structure

- `com.pinjam.peminjaman`: Root package containing Screens and core logic.
  - `api/`: Retrofit client, API service interfaces, and data models.
  - `ui/theme/`: Compose theme configurations (Color, Type, Theme).
  - `*Screen.kt`: Composable UI screens (Login, BarangList, Scan, etc.).

## 🏁 Getting Started

### Prerequisites
- Android Studio Ladybug | 2024.2.1 or later.
- Android SDK 34 (Upside Down Cake).
- Gradle 8.10.2+.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/peminjaman.git
   ```
2. Open the project in Android Studio.
3. Update the `BASE_URL` in `RetrofitClient.kt` if needed:
   ```kotlin
   private const val BASE_URL = "https://api.budhycc.my.id/api/"
   ```
4. Build and run the application on your device or emulator.

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
