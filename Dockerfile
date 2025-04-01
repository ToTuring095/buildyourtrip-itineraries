# Stage di build
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copia il file pom.xml
COPY buildyourtrip-itineraries/pom.xml .

# Copia i file del Maven wrapper
COPY buildyourtrip-itineraries/.mvn/ .mvn/
COPY buildyourtrip-itineraries/mvnw .

# Copia il codice sorgente
COPY buildyourtrip-itineraries/src/ src/

# Copia la libreria di logging
COPY buildyourtrip-logging /app/buildyourtrip-logging

# Installa la libreria di logging nel repository locale di Maven
RUN --mount=type=cache,target=/root/.m2 \
    cd /app/buildyourtrip-logging && \
    mvn clean install -DskipTests

# Esegue il build del progetto
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests

# Stage runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Installa curl per il health check
RUN apk add --no-cache curl

# Copia il jar buildato
COPY --from=builder /app/target/*.jar app.jar

# Configura le opzioni della JVM
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8082

CMD ["java", "-jar", "app.jar"] 