# Usa una imagen base que tenga Maven y OpenJDK preinstalados
FROM maven:3.8.6-openjdk-17-slim

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo pom.xml y el código fuente al contenedor
COPY pom.xml ./
COPY src ./src

# Ejecuta Maven para compilar el proyecto
RUN mvn clean package -DskipTests

# Expone el puerto 8080
EXPOSE 8080

# Ejecuta la aplicación Spring Boot usando el archivo JAR generado
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]

