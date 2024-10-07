# Proyecto de Aplicación Spring Boot

## Descripción
Esta es una aplicación desarrollada en **Java 17** con **Spring Boot**, utilizando **Maven** como herramienta de construcción. Está configurada para conectarse a una base de datos **MySQL** en un entorno de pruebas.

## Requisitos
- **Java 17**
- **Maven 3.x**
- **Git** (opcional, si se descarga desde un repositorio)


## Instalación y Ejecución

1. Clonar el repositorio:
   ```bash
   git clone <https://github.com/AmayaSantos/Ip-App> IpApp
   cd IpApp
   ```
2. Empaquetar la aplicación con Maven:
    ```bash
    mvn clean package -DskipTests
   ```
3. Construir la imagen Docker:
    ```bash
    docker build -t ip-app .
    ```

4. Ejecutar el contenedor:
    ```bash
    docker run -p 8080:8080 ip-app
    ```