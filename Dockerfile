# -- FASE 1: BUILD (Compilación con JDK 21) --
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
# Copia archivos de Maven y fuente
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
# Compila el proyecto
RUN ./mvnw clean install -DskipTests

# -- FASE 2: RUNTIME (Ejecución con JRE 21) --
# Usa una imagen JRE ligera compatible con Java 21
FROM eclipse-temurin:21-jre
WORKDIR /app
# Cloud Run espera que el contenedor escuche en el puerto 8080 por defecto
EXPOSE 8080
# Copia el JAR final de la fase de construcción
COPY --from=builder /app/target/*.jar app.jar
# Comando de ejecución
ENTRYPOINT ["java", "-jar", "/app/app.jar"]