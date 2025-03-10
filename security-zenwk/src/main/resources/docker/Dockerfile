# Usar OpenJDK 17 (ARM64) para reducir el tamaño de la imagen
#FROM openjdk:17-jdk
FROM eclipse-temurin:17

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el JAR generado en target al contenedor
COPY build/libs/security-zenwk-0.0.1-SNAPSHOT.jar security-zenwk.jar

# Exponer el puerto en el que corre el servicio
EXPOSE 8089

# Agregar opciones para permitir acceso a métricas del sistema
ENV JAVA_OPTS="--add-opens java.base/jdk.internal.platform=ALL-UNNAMED"

# Definir el comando para ejecutar la aplicación
# ENTRYPOINT ["java", "-jar", "security-zenwk.jar"]
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar security-zenwk.jar"]