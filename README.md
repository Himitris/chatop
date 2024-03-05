# Chatop

Chatop API is a Java-based application developed using Spring Boot and Spring Security. It provides authentication, registration, access to rental listings, and messaging functionalities. JWT tokens are utilized for secure communication.

## Technologies

- Spring Boot
- Java
- Spring Security
- JWT (JSON Web Token)
- Springdoc (Swagger)

## Database configuration

- If you haven't already, installed MySQL on your machine, download it from the official MySQL website (https://dev.mysql.com/downloads/installer/) and follow the installation instructions for your operating system.
- After MySQL installation,
- Create database chatop with command : "CREATE DATABASE chatop;"
- Then type command : "USE chatop;" to go inside the database
  initialize your database by running the provided script located at chatop/resources/sql. This script creates the chatop database and all necessary tables. You can run it with the command:
  mysql -u your_username -p < script_name.sql
- In your application.properties file, replace in this 2 lines to use your database username and password infos:
  spring.datasource.username=<YOURUSERNAME>
  spring.datasource.password=<YOURPASSWORD>

## Run the application

Clone the repository: git clone https://github.com/Himitris/chatop.git
Modify your storage folder by changing in application.properties, the line: storage.location=C:\\Users\\Himitris\\Documents\\OpenClassRoom\\P3\\chatop\\storage
Finally create your secret jwt key and replace in application.properties the line : app.jwt-secret=<YOURSECRETKEY> with your key.
The `YOURSECRETKEY` must contain a 64 random hexadecimal characters password
Open a terminal in the project directory.
Run the application: mvn spring-boot:run
Access the API at http://localhost:3001 via Postman for example.

## API Endpoint

Rental Controller
GET /api/rentals/{id}: Retrieve details of a specific rental.
PUT /api/rentals/{id}: Update information for a specific rental.
POST /api/rentals/{id}: Create a new rental.
GET /api/rentals: Retrieve a list of all rentals.

Message Controller
POST /api/messages: Send a new message related to rental listings.

User Controller
POST /api/auth/register: Register a new user.
POST /api/auth/login: Authenticate a user.
GET /api/auth/me: Retrieve details of the authenticated user.

## Swagger Documentation

Access Swagger documentation at http://localhost:3001/swagger-ui/index.html for detailed information about each API endpoint, request parameters, and response structures.

## Author

Antoine Gautier
