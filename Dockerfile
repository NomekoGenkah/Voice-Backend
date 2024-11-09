FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN ./mvnw spring-boot:run

FROM openjdk:17-slim
EXPOSE 8080
COPY --from=build /target/*.jar app.jar


# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
