# Usamos una imagen base de OpenJDK 17 con soporte para Java 17
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado por tu aplicación en el contenedor
COPY target/ipApp-0.0.1-SNAPSHOT.jar /app/ip-app.jar

# Exponemos el puerto en el que la aplicación correrá (configurado en tu Spring Boot application.properties o .yml)
EXPOSE 8080

# Comando para ejecutar la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "ip-app.jar"]