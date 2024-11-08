# Usa una imagen base de Java 17 (ajusta si necesitas otra versión)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml y el wrapper de Maven
COPY pom.xml ./
COPY .mvn/ .mvn
COPY mvnw ./
RUN ./mvnw dependency:resolve

# Copia el código fuente
COPY src ./src

# Compila la aplicación
RUN ./mvnw clean package -DskipTests

# Expone el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]

