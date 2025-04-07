# Etapa de compilación
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiamos el pom.xml y resolvemos dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Compilamos y generamos el .jar (ajusta si usas test)
RUN mvn clean package -DskipTests

# Etapa final: imagen ligera para ejecutar
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiamos el jar compilado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto donde corre tu app (ej: 8080)
EXPOSE 8080

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
