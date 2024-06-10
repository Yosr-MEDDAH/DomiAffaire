# Project Name

## Table of Contents
- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
  - [Angular](#angular)
  - [Spring Boot](#spring-boot)
  - [Flask](#flask)
  - [Flutter](#flutter)
  - [MongoDB](#mongodb)
- [Running the Application](#running-the-application)
- [Running Tests](#running-tests)
## Introduction
A brief introduction about your project. Describe the purpose and functionalities provided by the project.

## Prerequisites
Before you begin, ensure you have met the following requirements:
- Node.js and npm installed
- Java JDK and Maven installed
- Python installed
- Flutter SDK installed
- MongoDB installed and running

## Setup Instructions

### Angular
1. Navigate to the Angular project directory:
    ```sh
    cd path/to/angular/project
    ```
2. Install the dependencies:
    ```sh
    npm install
    # If you encounter issues, use:
    npm install --force
    ```

### Spring Boot
1. Open the project in your IDE.
2. If the IDE fails to install the project dependencies, go to the `pom.xml` file and reload the Maven project.
3. Update the path parameters in the `exportToCSV` method located in `AdminController` and `UserController` to your local path.

### Flask
1. Navigate to the `room-recommendation-system` directory:
    ```sh
    cd path/to/room-recommendation-system
    ```
2. Run the Flask API:
    ```sh
    python FLASK_API.py
    ```

### Flutter
1. Ensure Flutter is installed and properly configured.
2. Navigate to the Flutter project directory:
    ```sh
    cd path/to/flutter/project
    ```
3. Get the dependencies:
    ```sh
    flutter pub get
    ```
4. Run the application:
    ```sh
    flutter run
    ```

### MongoDB
1. Start MongoDB:
    ```sh
    mongod
    ```

## Running the Application

### Angular
To run the Angular application:
```sh
ng serve
```
Navigate to http://localhost:4200 to see the application running.

### Spring Boot
Run the Spring Boot application from your IDE or use the following command:

```sh
mvn spring-boot:run
```
### Flask
Ensure the Flask API is running as mentioned in the Flask setup instructions.

### Flutter
Run the Flutter application using:

```sh
flutter run
```

## Running Tests

### Angular
To run Cypress tests:

```sh
cypress run
```
Or to use the Cypress interface:

```sh
cypress open
```
