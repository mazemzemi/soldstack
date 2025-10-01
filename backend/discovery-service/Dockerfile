# Base image avec Java 24
FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

# Copier le jar de discovery-service
COPY build/libs/discovery-service-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port Eureka
EXPOSE 8761

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
