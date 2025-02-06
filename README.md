```text
,--.  ,--.                  ,--.   ,--.           ,--.                                          ,--.
|  ,'.|  | ,---. ,--.   ,--.|  |-. `--' ,---.     |  |    ,---.  ,--,--.,--.--.,--,--,          |  | ,--,--.,--.  ,--.,--,--.
|  |' '  || .-. :|  |.'.|  || .-. ',--.| .-. :    |  |   | .-. :' ,-.  ||  .--'|      \    ,--. |  |' ,-.  | \  `'  /' ,-.  |
|  | `   |\   --.|   .'.   || `-' ||  |\   --.    |  '--.\   --.\ '-'  ||  |   |  ||  |    |  '-'  /\ '-'  |  \    / \ '-'  |
`--'  `--' `----''--'   '--' `---' `--' `----'    `-----' `----' `--`--'`--'   `--''--'     `-----'  `--`--'   `--'   `--`--'
```
## Prerequisite
- Cài đặt JDK 17+ 
- Install Maven 3.5+ 
- Install IntelliJ

## Technical Stacks
- Java 17
- Spring Boot 3.4.1
- PostgresSQL
- Kafka
- Redis
- Maven 3.5+
- Lombok
- DevTools
- Docker, Docker compose

## Build application
```bash
mvn clean package -P dev|test|prod
```

## Run application
- Maven statement
```bash
./mvnw spring-boot:run
```
- Jar statement
```bash
java -jar target/*.jar
```

- Docker
```bash
docker build -t e-hub-service .
docker run -d e-hub-service:latest e-hub-service
```

## Package application
```bash
docker build -t e-hub-service .
```
