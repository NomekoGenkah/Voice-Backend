# Usa una imagen base de OpenJDK 17 (la versión recomendada para Spring Boot)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo pom.xml y el wrapper de Maven
COPY pom.xml ./
COPY .mvn/ .mvn
COPY mvnw ./
RUN ./mvnw dependency:resolve

# Copia el código fuente
COPY src ./src

# Compila el proyecto y genera el JAR
RUN ./mvnw clean package -DskipTests

# Expone el puerto 8080
EXPOSE 8080

# Ejecuta la aplicación Spring Boot usando el archivo JAR
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
