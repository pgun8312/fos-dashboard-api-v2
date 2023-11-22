# Use the official OpenJDK image as a base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/order-service-1.0-SNAPSHOT.jar /app/order-service.jar
COPY src/main/resources/application.properties /app/application.properties
# Specify the command to run on container startup
CMD ["java", "-jar", "order-service.jar"]
