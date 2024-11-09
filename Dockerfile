# Use a base image with Java to build the project
FROM openjdk:17 as build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml to download dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download the dependencies (this will be cached if there are no changes to the pom.xml)
RUN ./mvnw dependency:go-offline

# Copy the entire src folder into the container
COPY src ./src

# Build the project (creates the jar file)
RUN ./mvnw clean package -DskipTests

# Use the same base image for the final image to ensure compatibility
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the build stage (replace with the correct name of your jar)
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on (usually 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
