# Use the official Maven image as the base image
FROM maven:3.8.4-openjdk-17

# Set maintainer label
LABEL maintainer="Pasindu_Gunawardana"

# Explicitly set MAVEN_HOME
ENV MAVEN_HOME /usr/share/maven

# Set the timezone to India Standard Time
ENV TZ Asia/Kolkata

# Copy the JAR file
COPY target/*.jar app.jar

# Set entry point
ENTRYPOINT ["java", "-jar", "/app.jar"]

