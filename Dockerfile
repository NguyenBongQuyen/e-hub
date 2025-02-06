FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} e-hub-service.jar

ENTRYPOINT ["java", "-jar", "e-hub-service.jar"]

EXPOSE 8083
