FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

# Copier le jar de gateway-service
COPY build/libs/gateway-service-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port du Gateway
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
