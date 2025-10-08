# Step 2 Report – Data Storage, UI & Navigation

## 1. Project Overview

The PathBuddy application is an Android travel planning app built using Jetpack Compose, Room, and Navigation Compose.  
Its goal is to allow users to create, manage, and view trip plans in a clean and modern UI.  
This stage focused on setting up the navigation between screens, implementing Room-based local data storage, and integrating ViewModels for state management.

---

## 2. UI & Navigation

The app currently consists of the following screens:

- **HomeScreen** – Displays a welcome message, saved trips, and navigation buttons to plan a new trip or view the About Us page.
- **PlanningScreen** – Allows the user to create a new trip with destination, dates, interests, and budget fields.
- **TripPageScreen** – Shows trip details, daily breakdown, and placeholders for real-time information.
- **ProfileScreen** – Displays and manages user profile information (name, preferences).
- **AboutUsScreen** – Provides information about the app and the team.

Navigation is handled through a NavGraph using `NavHost` and `NavController`. Each screen has a unique route defined in a sealed `Screen` class. Arguments (e.g., `tripId`) are passed through routes for TripPage navigation.

Material 3 components are used throughout the UI for consistency, including:

- `Scaffold`, `TopAppBar`, `Button`, `OutlinedTextField`, `Card`, `LazyColumn`
- Material color scheme (primary blue)
- Rounded buttons and cards for a modern look

---

## 3. Data Storage

Local storage is implemented with Room:

- **Entity**:
    - `TripEntity` defines the schema for storing trip details (id, destination, start/end dates, budget, interests).

- **DAO**:
    - `TripDao` provides methods to insert new trips and retrieve planned and past trips.

- **Database**:
    - `PathBuddyDatabase` sets up the Room database and provides the DAO.

- **Repository**:
    - `TripRepository` and `ProfileRepository` provide a clean API for data access and encapsulate DAO usage.

- **AppContainer**:
    - Handles dependency injection, making repositories available to ViewModels.

Additionally, ProfileData uses SharedPreferences to store simple user information such as their display name.

---

## 4. ViewModels & State Handling

The app uses Jetpack ViewModel and StateFlow for UI state management:

- `HomeViewModel` – Fetches and exposes planned and past trips from the database.
- `PlanningViewModel` – Handles trip form input, validation, and saving new trips.
- `TripViewModel` – Prepares and exposes data for the TripPage screen.
- `ProfileViewModel` – Loads and updates profile data via ProfileRepository.

These ViewModels are instantiated using factories through the AppContainer to ensure proper dependency injection.

---

## 5. Current Functionality

- Navigation between all main screens (Home, Planning, TripPage, Profile, About Us) works.
- Room database is functional: trips added through the Planning screen are saved and displayed on Home.
- Basic Material UI implemented and visually consistent.
- About Us screen created and integrated into navigation.
- Profile information persists using SharedPreferences.

---

## 6. Next Steps

Planned tasks for the next development stage:

- API integration: Weather and attractions data for TripPage.
- Real-time data display and error handling for network calls.
- Add automated tests for navigation and database.
- Implement search/filter for trips on HomeScreen.
- Additional UI polish (dark mode, splash screen).

---

## Step 2 Deliverables

- Complete navigation flow between all screens
- Working Room database and ViewModels
- Clean Material 3 UI
- Source code available in GitHub repository
