# Spendora
<img width="1254" height="1254" alt="spendora_logo" src="https://github.com/user-attachments/assets/b7edf302-4ae1-4bb3-bd42-14ddb2766a22" />



Spendora is a modern Android expense tracking application built with **Kotlin**, **Jetpack Compose**, and **RoomDB**. It is designed to help users manage personal finances through expense tracking, category management, budget setting, history filtering, and analytics dashboards.

### Features

### Responsibilities & Roles
- Amilie Dlamini - ST10456326 - Backend
- Njabulo Nxumalo - ST10442968 - Frontend
- Sbusiso Nzimande - ST10184958 - Backend
- Thivar Munien - ST10271490 - Frontend
- Ahilya Surujpal - ST10285098 - Documentation

### Authentication
- User registration with:
  - Username
  - Password
  - Confirm password
- Login system with RoomDB-backed local account storage
- Separate custom-designed Login and Register screens

### Dashboard
- Styled dashboard homepage
- Monthly budget overview
- Total spent and remaining budget display
- Category spending summary
- Quick navigation to app sections

### Expense Management
- Add expenses with:
  - Expense name
  - Amount
  - Category
  - Start date
  - End date
  - Description
  - Expense icon
  - Receipt image
- Image selection with local storage handling
- Styled Add Expense screen

### Category Management
- Create categories with:
  - Category name/type
  - Category icon image
- Styled Add Category screen

### Budget Management
- Set a monthly total budget goal
- Set individual category budget limits
- View saved category limits
- Styled budget settings screen

### History
- Filter expenses by user-selected date range
- View expense history in a styled list
- Access saved receipt images from history

### Analytics
- Total spending overview
- Daily average spending
- Category spending breakdown
- Daily spending chart
- Monthly comparison analytics
- Styled analytics screen

## Tech Stack

- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose
- **Architecture:** MVVM
- **Database:** RoomDB
- **Navigation:** Navigation Compose
- **Charts:** MPAndroidChart
- **State Management:** StateFlow
- **Image Handling:** Android content picker + internal storage copy

