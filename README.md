# Parser Project

## Description

Parser is a web application designed for collecting, processing, and analyzing data about computer components. The main functionality includes:

- Parsing data about processors and graphics cards.
- Updating data from external sources.
- Saving reports in Excel format.
- Managing component records through a web interface.

## Technologies Used

### Backend:
- **Java 17** – main programming language.
- **Spring Boot 3.4.1** – primary framework for creating REST API and MVC.
- **Spring Data JPA** – database management.
- **Flyway** – database migration management.
- **MapStruct** – automatic conversion of DTOs and entities.
- **Lombok** – reducing boilerplate code.
- **Apache POI** – generating reports in Excel format.
- **JSoup** – parsing HTML pages.
- **Selenium** – browser automation.

### Database:
- **MySQL** – relational database.

### Frontend:
- **Thymeleaf** – server-side templating engine for rendering HTML.

### Development Tools:
- **Maven** – dependency management and project build system.
- **Checkstyle** – code quality control tool.
- **JUnit** – testing framework.

## Project Setup

1. Ensure **JDK 17** and **Maven** are installed.
2. Clone the repository:
   ```sh
   git clone https://github.com/your-repository/parser.git
   cd parser
   ```
3. Configure the MySQL database and update `application.properties`.
4. Start the project using Maven:
   ```sh
   mvn spring-boot:run
   ```

## API Endpoints

- `GET /gpus` – Retrieve a list of graphics cards.
- `POST /saveGpus` – Update power requirements for graphics cards.
- `GET /description` – Display the description page.
- `POST /operations/execute` – Execute various operations.

## Multi-threaded Processing

To improve performance during parsing, multithreading is used. The `parseAllMultiThread` method processes pages in separate threads using `ExecutorService`, speeding up data collection.

## Accessing the Application
To use the application, open your browser and navigate to http://localhost:8080/

## Authors

Developed by **Artem Makovskyi**.

