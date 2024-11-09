# Usa una imagen base para un proyecto Maven
FROM maven:3.8.6-openjdk-17-slim as build

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo POM y otros archivos necesarios
COPY pom.xml .

# Copia el código fuente
COPY src ./src

# Compila el proyecto Maven
RUN mvn clean install

# Usa una imagen base más liviana para la ejecución
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos del contenedor de construcción
COPY --from=build /app/target/your-artifact.jar /app/your-artifact.jar

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "your-artifact.jar"]
