# PathBuddy

## Short Description

A personalized travel assistant designed to streamline trip planning, provide real-time navigation and activity tracking and preserve your travel memories. This application will serve as an all-in-one tool for modern travelers, using cutting-edge technologies for a seamless journey.

## Team Members and Roles

| Name | Role |
| --- | --- |
| **Magnus** | **Lead Developer / Builder** – Leads the process of coding among the team. |
| **Hannes** | **Researcher** – Leads the process of gathering information, facts and background for the project. |
| **Reimo** | **Project Leader / Manager** – Keeps the team organized, assigns tasks and ensures deadlines are met. |
| **Tõnis** | **Editor** – Leads the process of writing the report or documentation, checks for grammar, flow and clarity. |
| **Andreas** | **Presenter** – Leads the presentation. |

While each team member has a designated lead role, all members will be actively involved in every aspect of the project, including research, development, writing and presenting.

---

## Project Progress (As of Step 2)

This milestone focused on building the core architectural foundation and implementing the essential local functionalities of the application. The project now has a robust, scalable structure based on modern Android development principles (MVVM, Repository Pattern, Dependency Injection via a manual container).

**Key achievements include:**
*   **Solid Local Database:** A fully functional local database using Room for storing and managing user-created trips.
*   **User Profile Storage:** Local storage for user profile data (name, home base, interests) using Jetpack DataStore.
*   **Reactive UI:** The UI is built entirely with Jetpack Compose and automatically updates when data in the database changes.
*   **Complete UI/UX Flow:** Users can now create, save, view,and manage trips and their own profile information within the app.

---

## Feature Implementation Status

The following is a breakdown of the originally planned features and their current implementation status.

*   **✅ Implemented - Trip Planning & Itinerary Management**
    *   A robust trip planning module has been created. Users can fill out a form to create a new trip, including destination, dates and interests. This input will serve as the prompt for the AI. All created trips are saved to a local Room database and displayed on the Home Screen.

*   **🟡 In Progress - User Profiles**
    *   **Implemented:** A local profile system is in place. Users can save their name, location and favorite interests, which are persisted on the device using Jetpack DataStore. The Profile Screen has a full UI for both viewing and editing this information.
    *   **Pending:** Secure cloud-based user authentication (Sign-up/Login with Firebase).

*   **⚫ Not Implemented - Real-Time Navigation & Geolocation**
    *   The UI contains placeholders for map integration, but the connection to the Google Maps API is a future task.

*   **⚫ Not Implemented - Integrated Camera for Photo Journaling**
    *   The in-app camera functionality has not yet been implemented.

*   **⚫ Not Implemented - Cloud Data Persistence with Firebase**
    *   All data persistence is currently local to the device using Room and DataStore. The migration to Firebase Firestore for cloud syncing is a future task.

*   **⚫ Not Implemented - Dynamic Weather Integration**
    *   The connection to the OpenWeather API has not yet been implemented.

*   **⚫ Not Implemented - AI-Powered Itinerary Generation**
    *   Leverage a powerful generative AI model (like **Google's Gemini**) to automatically create detailed, day-by-day itineraries. The AI will take the user's destination, travel dates, and interests as input to suggest activities, restaurants and logical routes, forming a complete, ready-to-use travel plan.

---

## Tools & Frameworks in Use

The project is currently being developed using the following technologies:

*   **Language:** Kotlin
*   **IDE:** Android Studio
*   **Architecture:** Model-View-ViewModel (MVVM), Repository Pattern
*   **UI & Design:** Jetpack Compose with Material Design 3
*   **Navigation:** Compose Navigation
*   **Local Database / Storage:**
    *   **Room Database:** For structured storage of trip data.
    *   **Jetpack DataStore:** For key-value storage of user profile preferences.
*   **Version Control:** Git & GitHub

*(The following tools from the original plan will be integrated in future development stages):*
*   **AI & Machine Learning:**
    *   **Google AI SDK for Gemini** (or Gemini API via REST)
*   **Cloud & Networking:**
    *   Firebase Firestore & Firebase Storage
    *   Retrofit
*   **APIs & Services:**
    *   Google Maps API
    *   OpenWeather API
    *   Android CameraX
