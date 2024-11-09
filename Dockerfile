# Usa una imagen base de OpenJDK 17 (la versión recomendada para Spring Boot)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo mvnw al contenedor
COPY mvnw ./

# Copia el archivo pom.xml
COPY pom.xml ./

# Da permisos de ejecución al archivo mvnw
RUN chmod +x mvnw

# Copia el código fuente
COPY src ./src

# Compila el proyecto y genera el JAR usando mvnw
RUN ./mvnw clean package -DskipTests

# Expone el puerto 8080
EXPOSE 8080

# Ejecuta la aplicación Spring Boot usando el archivo JAR
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
