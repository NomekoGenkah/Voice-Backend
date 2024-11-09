FROM openjdk:17-jdk-slim

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos del proyecto
COPY pom.xml .
COPY src ./src

# Ejecutar la compilación de Maven
RUN mvn clean install

# Definir el contenedor base para la ejecución
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/target/your-artifact.jar /app/your-artifact.jar

CMD ["java", "-jar", "your-artifact.jar"]
