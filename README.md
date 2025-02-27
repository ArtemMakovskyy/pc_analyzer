# Parser Project

## Description

Parser is a local Java application with a web interface designed to collect, process and analyze data about computer components. The main functionality includes:

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
- **Docker** – browser automation.

### Database:
- **MySQL** – relational database.

### Frontend:
- **Thymeleaf** – server-side templating engine for rendering HTML.

### Development Tools:
- **Maven** – dependency management and project build system.
- **Checkstyle** – code quality control tool.
- **JUnit** – testing framework.

## Project Setup
### First option Using maven, JDK and a database MySQL

1. Ensure **JDK 17** and **Maven** are installed.
2. Clone the repository:
   ```sh
   git clone https://github.com/ArtemMakovskyy/pc_analyzer.git
   cd parser
   ```
3. Configure the MySQL database and update `application.properties`.
4. Start the project using Maven:
   ```sh
   mvn spring-boot:run
   ```
### Second option Using maven and Docker
- **Instalation**
   - Clone the repository from github:
  ```shell
  git clone https://github.com/ArtemMakovskyy/pc_analyzer.git
   ```
   - Start the Docker
   - Open a terminal and navigate to the root directory of your project
   - Into the terminal use command to build the container and start project.
  ```shell
    mvn clean package
    docker-compose up
   ```
  to stop use docker
  ```shell
    docker-compose stop
   ```
## API Endpoints

- `GET /gpus` – Retrieve a list of graphics cards.
- `POST /saveGpus` – Update power requirements for graphics cards.
- `GET /description` – Display the description page.
- `POST /operations/execute` – Execute various operations.

## Multi-threaded Processing

To improve performance during parsing, multithreading is used. The `parseAllMultiThread` method processes pages in separate threads using `ExecutorService`, speeding up data collection.

## Accessing the Application
To use the application, open your browser and navigate to http://localhost:8079/

When deploying the project to the internet, you need to change the link `localhost:8079` to the internet address in the file `src/main/resources/templates/operations.html`.

## Authors

Developed by **Artem Makovskyi**.

