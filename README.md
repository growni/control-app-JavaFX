# 🚀 Control App

A desktop app for power users who want to **install**, **uninstall**, **clean**, and **optimize** their Windows environment — with a connected backend and a responsive frontend.

### 📦 Download the latest release from the releases tab or visit my website https://control-app-free.netlify.app/

---

## ✨ Features

- **User Authentication** — Register and Login.
- **Secure Password Handling** — Passwords stored hashed (BCrypt).
- **Password Reset** — Time-limited, expiring tokens (15 minutes).
- **Feedback Form** — Send feedback via the app.
- **Responsive Frontend** — Simple landing page with download links.
- **Install/Uninstall Programs** — Silent execution of installers (uses Winget).
- **Debloat** — Detect unneeded apps and offer to remove them.

---

## 🖥️ Tech Stack

| Layer         | Technology                             |
|---------------|-----------------------------------------|
| Desktop App   | JavaFX                                  |
| Backend API   | Java 21 + Spring Boot                   |
| Database      | PostgreSQL (Render.com)                 |
| Email Service | Spring MailSender (SMTP)                |
| Web Page      | React.js                                |
| Hosting       | Render.com (Backend & DB), Netlify (Web page) |

---

## ⚙️ Installation Instructions

### 1. Backend (Spring Boot)

#### 1.1. Clone the backend repository:
   - bash: git clone https://github.com/growni/control-app-backend.git
#### 1.2 Update application.properties for your local PostgreSQL setup:
  - spring.datasource.url=jdbc:postgresql://localhost:5432/control_app
  - spring.datasource.username=your_db_user
  - spring.datasource.password=your_db_password
  - spring.jpa.hibernate.ddl-auto=update
  - spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
#### 1.3 Run the backend:
  - bash: ./mvnw spring-boot:run

### 2. JavaFX Desktop App
  - 2.1 Ensure you have Java 21+ installed.
  - 2.2 Open the project in an IDE like IntelliJ or Eclipse.
  - 2.3 Check that the backend URL is correctly set in your service/API handler class.
  - 2.4 Run the app using the IDE or build scripts.

### 3. Web Frontend (React.js)
#### 3.1 Clone the frontend repository (if public/shared).
#### 3.2 Install dependencies:
  - bash: npm install
#### 3.3 Run the dev server:
  - bash: npm run dev
## 📦 API Overview
| Method | Endpoint                             | Description                                       |
| ------ | ------------------------------------ | ------------------------------------------------- |
| POST   | `/api/auth/register`                 | Register a user (username, password, email).      |
| POST   | `/api/auth/login`                    | Authenticate a user (username, password).         |
| POST   | `/api/auth/send-password-reset-link` | Send password reset link (email).                 |
| POST   | `/api/auth/reset-password`           | Reset password via token (token, newPassword).    |
| PUT    | `/api/profile/update-password`       | Change password (username, newPassword).          |
| POST   | `/api/profile/subscribe`             | Subscribe to patch notes (email).                 |
| POST   | `/api/profile/unsubscribe`           | Unsubscribe from patch notes (email).             |
| GET    | `/api/profile/is-subscribed`         | Check subscription status (email as query param). |
| POST   | `/api/profile/feedback`              | Submit feedback (email, message).                 |

## 📱 Responsive Frontend
  - Automatically adjusts layout depending on screen size.

## 🛡 Security
  - Passwords hashed using BCrypt.
  - Token-based password resets using UUID.
  - CORS enabled for secure cross-origin requests.

## 📧 Contact
For questions, ideas, or bugs, contact:
 - 📬 control.desktop.app@gmail.com
