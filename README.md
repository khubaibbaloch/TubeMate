# 📥 TubeMate

**TubeMate** is an all-in-one media downloader app for Android that allows users to download videos and statuses from popular platforms like **WhatsApp**, **Instagram**, **YouTube**, and **Facebook** with ease. Built using **Kotlin** and **Jetpack Compose**, TubeMate provides a smooth, modern UI and powerful backend functionalities for seamless downloads.

---

## ✨ Features

### 📱 WhatsApp Status Downloader
- View and download image & video statuses from WhatsApp.
- Automatically detects `.Statuses` folder on supported devices.
- Supports handling of Android Q and above with scoped storage.

### 📸 Instagram Downloader
- Download Instagram posts (images/videos), reels, and IGTV by URL.
- Automatically fetches media, captions, and usernames.
- No login required.

### ▶️ YouTube Downloader
- Download YouTube videos along with audio.
- Audio and video are downloaded separately and merged.
- High-quality download support.

### 📘 Facebook Video Downloader
- Download public videos from Facebook posts using URL.
- Parses and fetches video source from given post.

---

## 🛠️ Tech Stack

- **Kotlin** & **Jetpack Compose** – Modern UI framework.
- **ViewModel + MVVM Architecture** – For clean separation of concerns.
- **FFmpeg (optional)** – For merging audio and video.
- **DownloadManager** – For handling background downloads.
- **Firebase** – Optional analytics or crash logging.

---

## 🔒 Permissions Required

- `READ_EXTERNAL_STORAGE` / `MANAGE_EXTERNAL_STORAGE` (Android 10 and below)
- `WRITE_EXTERNAL_STORAGE`
- `INTERNET` – For downloading media from the internet.

---

## 🧪 Modules

| Module              | Description |
|---------------------|-------------|
| `InstagramDownloader` | Handles parsing and downloading from Instagram |
| `WhatsAppStatus`      | Displays and saves WhatsApp statuses |
| `YouTubeDownloader`   | Fetches and combines YouTube video/audio |
| `FacebookDownloader`  | Fetches and downloads Facebook public videos |
| `MediaHelper`         | Handles file saving, storage access |
| `TubeMateRepository`  | Repository layer for managing operations |

---

## 🚀 How to Use

1. Clone or download the repo.
2. Open in **Android Studio**.
3. Run the project on an emulator or real device.
4. Grant permissions when prompted.
5. Start downloading from your favorite platforms!

---

## 📜 License

This project is developed for **educational purposes only**. Downloading media from platforms may violate their terms of service. Use responsibly.

---

## 🙌 Credits

- Instagram scraping via **Instaloader** (Python backend or URL parsing).
- YouTube video/audio handling logic.
- Android Jetpack Compose & Kotlin Coroutines.
- FFmpeg for merging video + audio.

---


