# -- FASE 1: BUILD --
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# 1. Copiamos solo lo necesario para descargar dependencias (Caché de Docker)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# 2. Copiamos el código fuente y compilamos
COPY src ./src
RUN ./mvnw clean package -DskipTests

# -- FASE 2: RUNTIME --
# Usamos alpine si buscas ligereza extrema, o la estándar para máxima compatibilidad
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 3. Flags de rendimiento para contenedores
# -XX:+UseContainerSupport: Hace que la JVM respete los límites de Cloud Run
# MaxRAMPercentage: Evita errores de memoria (OOM)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# 4. ENTRYPOINT flexible
# Usamos "sh -c" para que las variables de entorno se expandan correctamente
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]