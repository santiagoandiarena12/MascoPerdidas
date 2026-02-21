# Usamos Java 21 que es la que tienes instalada
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Copiamos el archivo .jar que generaste con mvnw
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]