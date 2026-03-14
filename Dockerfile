# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-25 AS build

WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN mvn clean package -DskipTests


# ---------- Run Stage ----------
FROM eclipse-temurin:25-jdk

WORKDIR /app

# Copy the generated jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render requires the app to run on PORT env variable
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]