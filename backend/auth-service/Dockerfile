FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

# Copier le jar de auth-service
COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port de l'API Auth
EXPOSE 8081

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
