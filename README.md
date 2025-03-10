# **Event Management App**

## **Overview**
The **Event Management App** is an Android application that provides fair and accessible event registration through a **lottery-based system**. The app ensures equal opportunities for participants by allowing them to join waiting lists and be randomly selected for events.

The application supports **multi-user roles** _(Entrant, Organizer, Admin)_ with distinct functionalities, **QR code scanning**, and **real-time event management** using **Firebase**.

### 🎥 **Demo Video**  
For a comprehensive walkthrough of the Event Management App's features, watch the full demo video hosted on Google Drive:  
[View Full Demo Video](https://drive.google.com/file/d/156Mpkwj-IH8tMCLBpqfOBlLRKLpZoo8e/view?usp=sharing)

### **Screenshots:**

| **Dashboard Overview**                                                   | **Event Details**                                                   |
|:------------------------------------------------------------------------:|:-------------------------------------------------------------------:|
| ![Dashboard Overview](https://github.com/user-attachments/assets/914afe19-60ce-4e06-ac23-8d12eee1c4a9) | ![Event Details](https://github.com/user-attachments/assets/58ce11f5-b5c5-4dff-9f9a-a722702b3b18) |


---

## **Features**
### ✅ **Entrant Features**
- 📌 **Join or leave an event waiting list**
- 🔔 **Receive notifications** if selected or not
- 🔄 **Get another chance** if a selected entrant declines
- 📷 **Upload, update, or remove profile pictures**
- 📍 _Optional:_ **Geolocation-based sign-ups**
- 🔍 **View event details** using QR code scanning
- 🚀 **Auto-login** via device authentication _(no username/password needed)_

### 🎟️ **Organizer Features**
- ✨ **Create and manage events**
- 🎫 **Generate unique QR codes** for event registration
- 📊 **View and manage waiting lists**
- 🌍 **Track entrants' registration locations on a map**
- 🏆 **Run a fair lottery system** to select event participants
- 🖼️ **Upload, update, and manage event posters**
- 🔔 **Send targeted notifications** to entrants and participants

### 🔧 **Admin Features**
- ❌ **Remove events, profiles, and QR code data**
- 🔍 **Browse events, profiles, and uploaded images**
- 🏢 **Remove facilities that violate policies**

---

## **Technology Stack**
- **Programming Language**: Java (Core)
- **Development Tools**: Android Studio, Firebase
- **Database & Cloud Storage**: Firebase Firestore, Firebase Storage
- **UI Testing**: Espresso, JUnit
- **Dependency Management**: Gradle

---

## **Installation & Setup**
### **Prerequisites**
1. Install [Android Studio](https://developer.android.com/studio)
2. Set up [Firebase](https://firebase.google.com/)
3. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/event-lottery-app.git
   cd event-lottery-app
   ```
4. **Open the project in Android Studio**
5. **Connect Firebase:**
   - Go to **Tools > Firebase** and follow the integration guide
   - Add your `google-services.json` file (from Firebase Console)
6. **Sync Gradle and build the project**
7. **Run the app on an emulator or a real device**

---

## **Usage Guide**

###  **Starting the App**

#### **Sign Up / Log In:**
- Users can register as **Entrants**, **Organizers**, or **Admins**
- Firebase Authentication handles secure logins

#### **Entrants:**
- Browse events
- Scan a QR code to get event details
- Join or leave an event’s waiting list
- Receive lottery selection notifications

#### **Organizers:**
- Create events and generate QR codes
- Manage a waiting list of interested participants
- Notify winners when the lottery draw is completed
- Upload event posters and details

#### **Admins:**
- Remove events, profiles, and images
- Monitor event and user activities
- Ensure fair use by removing policy-violating facilities

---

## **Testing**

### **Unit Tests**
- We use **JUnit** for backend logic testing (event selection, user management, and QR code processing).
- Run unit tests with:

### **UI Tests (Espresso)**
- We use **Espresso** to test user interactions such as:
  - Navigating between screens
  - Registering for an event
  - Scanning a QR code
  - Receiving notifications
## **Repository**

This repository is a **fork** of our original project. You can find the main repository here:

[**CMPUT301F24beacon/beacon-techies**](https://github.com/CMPUT301F24beacon/beacon-techies)

Feel free to explore the main repo for additional context, contributions, and issue tracking.

