# Cacamical - A Fun Mapping Application

---

Authors: Quentin Ducoulombier, Noé Faucher  
Date: November 5, 2023  
Email: ducoulombi@cy-tech.fr, fauchernoe@cy-tech.fr

---

Cacamical is a mapping application that allows users to mark and share their favorite locations on a map. Whether it's a beautiful park, a great restaurant, or a hidden gem, Cacamical helps you keep track of your favorite places and share them with others.

## Prerequisites

Before running Cacamical, you need to have the following prerequisites:

- **Java Development Kit (JDK)**: Make sure you have Java JDK 17 or higher installed on your system.

## Getting Started

Follow these steps to get Cacamical up and running:

1. **Clone the Repository**: Clone this repository to your local machine using Git.

   ```bash
   git clone https://github.com/your-username/cacamical.git
   ```
2. **Set Up the Database**: Verify the database configuration in application.properties with your database settings.

3. **Run the application**:

    ```bash
    cd CacAmical
    ./mvnw spring-boot:run
    ```

4. **Access Cacamical**: Open your web browser and navigate to http://localhost:8080 to start using Cacamical.

## Features

Cacamical offers a range of exciting features to enhance your mapping experience:

- **User Authentication**: Create an account and log in securely to access all the application's features.

- **Place Points on the Map**: Easily place points of interest on the map by clicking directly on the location. The application provides full CRUD (Create, Read, Update, Delete) functionality for managing these points.

- **Point Details**: When adding a point, you can provide a description, note, and title, making it easy to share key information about your favorite locations.

- **Like Points**: Show your appreciation by sending likes to your favorite points on the map. Let others know which places you adore.

- **User Profiles**: Personalize your experience with a user profile, where you can view your added points, likes, and comments.

- **Place Points by Address**: Not sure where a location is? No problem! You can place a point on the map using an address, making it even more convenient to add your favorite spots.

- **Friend System**: Connect with friends and stay updated on their favorite places. Share points, likes, and comments with your friends.

- **Comments**: Engage with the community by leaving comments on various points. Share your thoughts and experiences with others.

Enjoy exploring, sharing, and connecting through Cacamical!
