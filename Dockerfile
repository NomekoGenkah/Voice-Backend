# Usar una imagen base de Maven o OpenJDK para la construcción
FROM maven:latest as build

WORKDIR /app

# Copiar los archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compilar el proyecto con Maven
RUN mvn clean install

# Usar una imagen base de OpenJDK para ejecutar la aplicación
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR generado desde el contenedor de construcción
COPY --from=build /app/target/your-artifact.jar /app/your-artifact.jar

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "your-artifact.jar"]
