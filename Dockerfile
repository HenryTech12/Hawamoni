# Stage 1: Build the app
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/PayStackIntegration-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
