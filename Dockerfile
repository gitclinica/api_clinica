# Usar una imagen base de Java
FROM openjdk:21-jdk

# Copiar el archivo .jar al contenedor
COPY deploy/clinica.jar /app/mi-app.jar

# Establecer el directorio de trabajo
WORKDIR /app

# Comando para ejecutar el archivo .jar
CMD ["java", "-jar", "mi-app.jar"]
