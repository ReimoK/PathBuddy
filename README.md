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

## Planned Features

Our goal is to create a comprehensive and user-friendly travel application. The core features we plan to implement are:

1.  **User Authentication & Profiles:** Secure sign-up and login for users. Profiles will store personalized preferences like preferred travel modes, dietary restrictions and interests to provide tailored recommendations.
2.  **Trip Planning & Itinerary Management:** A robust trip planning module that allows users to create and manage detailed itineraries. Users will be able to add destinations, schedule activities and visualize their travel plans.
3.  **Real-Time Navigation & Geolocation:** Integration with the Google Maps API for live location tracking, turn-by-turn navigation and discovery of nearby points of interest.
4.  **Integrated Camera for Photo Journaling:** An in-app camera feature that allows users to capture photos of sights and automatically tag them to their current location and itinerary item. This creates a visual travel diary, directly linking memories to the trip timeline.
5.  **Cloud Data Persistence with Firebase:** Utilization of Firebase Firestore to store user profiles, trip logs (including photo references) and preferences in the cloud. This ensures data is synced and accessible across multiple devices.
6.  **Dynamic Weather Integration:** The app will fetch real-time weather data from the OpenWeather API and dynamically adjust itineraries, suggesting indoor alternatives on rainy days, for example.

## Tools & Frameworks

The project will be developed using the following technologies:

*   **Language:** Kotlin
*   **IDE:** Android Studio
*   **Database:**
    *   **Cloud Storage:** Firebase Firestore & Firebase Storage (for images)
    *   **Local Caching:** Room DB
*   **Networking:** Retrofit
*   **APIs:**
    *   Google Maps API
    *   OpenWeather API
    *   Android CameraX
*   **UI & Design:**
    *   Material Design 3
    *   Custom Animations & Transitions
*   **Version Control:** Git & GitHub
