# Library Management System

Welcome to the **Library Management System** — a modern, secure, and efficient Spring Boot application designed to simplify library operations. Whether you're a librarian managing book inventories or a user borrowing your favorite reads, this system empowers you with seamless control and accessibility.


## 🚀 Features

- **User Management**
  - Register as a user or librarian
  - Secure login with JWT-based authentication
  - Role-based access control to protect librarians' administrative actions

- **Library Operations**
  - Create and manage multiple libraries with geolocation support
  - Define opening and closing times with validation to prevent scheduling conflicts
  - View libraries near your current location thanks to geospatial querying

- **Book Management**
  - Add, update, or delete books within your library
  - Track total available quantity and borrowing duration constraints
  - Search books by name, author, or publisher with fuzzy matching

- **Book Borrowing & Returning**
  - Borrow books with enforced borrowing limits and overdue checks
  - Return one or multiple books efficiently
  - Automatic updates of book availability and borrowing records

- **Robust Security**
  - Secure endpoints with Spring Security and JWT authentication
  - Password hashing with BCrypt
  - Fine-grained method-level security annotations for sensitive operations

- **Exception Handling**
  - Consistent error responses with meaningful messages
  - Handling of common errors such as entity not found, invalid credentials, and data conflicts

## API Endpoints

### User
- `POST /user/register` — Register a new user or librarian
- `POST /user/login` — Login and receive JWT token
- `GET /user/get` — Get authenticated user profile
- `PATCH /user/update` — Update user info
- `DELETE /user/delete/{uid}` — Delete a user (librarian only)

### Library
- `POST /library/add` — Add a new library (librarian only)
- `GET /library/get` — List nearby libraries
- `GET /library/id/{id}` — Get library by ID
- `PATCH /library/update` — Update library details (librarian only)
- `GET /library/unique-id/{id}` — Get library by unique ID
- `DELETE /library/delete/{id}` — Delete a library (librarian only)

### Book
- `POST /book/add/{libraryId}` — Add book to library (librarian only)
- `GET /book/get` — List all books
- `GET /book/library/{id}` — Get books by library
- `GET /book/name/{name}` — Search books by name
- `GET /book/id/{id}` — Get book by ID
- `POST /book/borrow/{bookId}` — Borrow a book (user only)
- `POST /book/return` — Return books (user only)
- `PATCH /book/update` — Update book info (librarian only)

## Project Structure

Library-Management/
├── MultipleFiles/
│   ├── .gitignore
│   ├── .mvn/
│   │   ├── wrapper/
│   │   │   ├── maven-wrapper.properties
│   │   │   └── mvnw
│   │   └── mvnw.cmd
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── lbm/
│   │   │   │           ├── config/
│   │   │   │           │   ├── GlobalExceptionHandler.java
│   │   │   │           │   ├── JacksonConfig.java
│   │   │   │           │   ├── MongoConfig.java
│   │   │   │           │   └── SecurityConfig.java
│   │   │   │           ├── controller/
│   │   │   │           │   ├── BookController.java
│   │   │   │           │   ├── LibraryController.java
│   │   │   │           │   └── UserController.java
│   │   │   │           ├── dto/
│   │   │   │           │   ├── ApiResponse.java
│   │   │   │           │   ├── BookDto.java
│   │   │   │           │   ├── BookStatusDto.java
│   │   │   │           │   ├── LibraryDto.java
│   │   │   │           │   ├── LocationDto.java
│   │   │   │           │   └── UserDto.java
│   │   │   │           ├── entity/
│   │   │   │           │   ├── Book.java
│   │   │   │           │   ├── BookStatus.java
│   │   │   │           │   ├── Library.java
│   │   │   │           │   ├── User.java
│   │   │   │           │   └── core/
│   │   │   │           │       ├── BaseEntity.java
│   │   │   │           │       └── LocationData.java
│   │   │   │           ├── enum/
│   │   │   │           │   ├── Days.java
│   │   │   │           │   └── Role.java
│   │   │   │           ├── exception/
│   │   │   │           │   └── EntityNotFoundException.java
│   │   │   │           ├── filter/
│   │   │   │           │   └── JwtAuthenticationFilter.java
│   │   │   │           ├── mapper/
│   │   │   │           │   ├── BookMapper.java
│   │   │   │           │   ├── BookStatusMapper.java
│   │   │   │           │   ├── LibraryMapper.java
│   │   │   │           │   └── LocationMapper.java
│   │   │   │           ├── repository/
│   │   │   │           │   ├── BookRepository.java
│   │   │   │           │   ├── BookStatusRepository.java
│   │   │   │           │   ├── LibraryRepository.java
│   │   │   │           │   └── UserRepository.java
│   │   │   │           ├── services/
│   │   │   │           │   ├── BookService.java
│   │   │   │           │   ├── JwtService.java
│   │   │   │           │   ├── LibraryService.java
│   │   │   │           │   └── UserService.java
│   │   │   │           └── utils/
│   │   │   │               ├── AppTimeZoneUtil.java
│   │   │   │               ├── AuthFilterHeader.java
│   │   │   │               ├── GlobalMapping.java
│   │   │   │               ├── MongoErrorMessageUtil.java
│   │   │   │               ├── SearchUtil.java
│   │   │   │               └── SecurityUtil.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── static/
│   │   │           └── (static files if any)
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── lbm/
│   │                   └── (test classes)
│   ├── pom.xml
│   └── README.md
└── LICENSE

### For Set Jwt Key
Click on your project Configuration -> Edit Configuration -> 
Set below key to your Environment variables:
spring.jwt.secret=Your_Key

## 🛠️ Technology Stack

- Spring Boot 3.4.4
- Java 17
- Spring Security with JWT Authentication
- Spring Data MongoDB for flexible document storage
- Joda-Time for advanced date/time handling
- MapStruct for clean and maintainable DTO-to-entity mapping
- Lombok for boilerplate code reduction
- Maven for project and dependency management
- RESTful API architecture

### Prerequisites

- Java 17 SDK installed
- Maven 3.8+ installed
- MongoDB instance running and accessible
