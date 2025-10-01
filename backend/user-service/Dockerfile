FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

# Copier le jar de user-service
COPY build/libs/user-service-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port de l'API User
EXPOSE 8082

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
