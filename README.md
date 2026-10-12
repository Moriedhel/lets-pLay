# LetsPlay - Product Management API

A Spring Boot REST API for managing products and users with JWT-based authentication, role-based access control, and MongoDB integration.

## Features

- ✅ **User Authentication**: JWT token-based authentication
- ✅ **Role-Based Access Control**: ADMIN and USER roles with fine-grained permissions
- ✅ **Product Management**: Create, read, update, and delete products
- ✅ **User Management**: User registration and profile management
- ✅ **MongoDB Integration**: Persistent data storage
- ✅ **Input Validation**: Comprehensive data validation
- ✅ **Exception Handling**: Global exception handling with meaningful error messages
- ✅ **Security**: Secured endpoints with Spring Security

## Tech Stack

- **Framework**: Spring Boot 4.1.0
- **Language**: Java 21
- **Database**: MongoDB
- **Authentication**: JWT (JSON Web Tokens)
- **Build Tool**: Maven
- **Security**: Spring Security

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- MongoDB (local or remote instance)

## Installation

### 1. Clone the repository

```bash
git clone <repository-url>
cd lets-play
```

### 2. Configure MongoDB

Update `application.properties` or `application.yml` with your MongoDB connection details:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/lets-play
# or
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=lets-play
```

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

## API Endpoints

### Authentication

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Products

#### List All Products (Public)
```http
GET /api/products
```

#### Get Product by ID (Public)
```http
GET /api/products/{id}
```

#### Get Products by Owner (Public)
```http
GET /api/products/owner/{ownerId}
```

#### Create Product (Requires Authentication)
```http
POST /api/products
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Product Name",
  "description": "Product Description",
  "price": 99.99,
  "quantity": 10
}
```

#### Update Product (Owner or Admin)
```http
PUT /api/products/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Updated Name",
  "description": "Updated Description",
  "price": 79.99,
  "quantity": 5
}
```

#### Delete Product (Owner or Admin)
```http
DELETE /api/products/{id}
Authorization: Bearer <token>
```

### Users

#### Get All Users (Admin only)
```http
GET /api/users
Authorization: Bearer <admin-token>
```

#### Get User by ID (Admin only)
```http
GET /api/users/{id}
Authorization: Bearer <admin-token>
```

#### Create User
```http
POST /api/users
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123"
}
```

## Architecture

### Layer Structure

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Business logic (if applicable)
- **Repository Layer**: Database operations using Spring Data JPA/MongoDB
- **Model Layer**: Entity classes (User, Product, Role)
- **DTO Layer**: Data Transfer Objects for request/response
- **Security Layer**: JWT token generation and validation

### Directory Structure

```
src/main/java/com/letsplay/
├── controller/          # REST API endpoints
├── model/              # Entity classes
├── dto/                # Data Transfer Objects
├── repository/         # Database access layer
├── security/           # JWT and authentication
├── config/             # Configuration classes
├── exception/          # Custom exceptions
└── LetsPlayApplication.java
```

## Security

### JWT Token

- Tokens are generated upon successful login
- Include the token in the `Authorization` header as `Bearer <token>`
- Tokens have an expiration time configured in the application

### Role-Based Access Control

- **ADMIN**: Full access to all endpoints and operations
- **USER**: Can create/update/delete their own products

## Error Handling

The API returns meaningful error responses:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found: invalid-id"
}
```

## Default Admin Account

The application seeds a default admin account on startup. Check `AdminAccountSeeder.java` for credentials.

## Development

### Running Tests

```bash
mvn test
```

### Code Structure Tips

- **Controllers**: Use annotations like `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- **DTOs**: Transfer data between layers and API clients
- **Repositories**: Extend `MongoRepository` for database operations
- **Records**: Java records are used for simple POJOs with auto-generated getters/setters

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit your changes: `git commit -m 'Add some feature'`
3. Push to the branch: `git push origin feature/your-feature`
4. Open a Pull Request

## License

This project is open source and available under the MIT License.

## Support

For issues, questions, or suggestions, please open an issue on GitHub.

---

**Happy coding! 🚀**
