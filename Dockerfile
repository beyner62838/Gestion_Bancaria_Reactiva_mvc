# Imagen base con Java
FROM eclipse-temurin:21-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copiamos el JAR generado
COPY target/*.jar app.jar
# Puerto del backend
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java","-jar","app.jar"]
