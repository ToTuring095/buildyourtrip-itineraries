# BuildYourTrip Itineraries Service

Servizio microservizio che gestisce la creazione, modifica e gestione degli itinerari di viaggio per il progetto BuildYourTrip.

## Funzionalità

- Creazione di itinerari personalizzati
- Modifica e aggiornamento degli itinerari
- Gestione delle tappe e degli eventi
- Integrazione con il servizio Maps per la visualizzazione delle tappe
- Integrazione con il servizio AI per suggerimenti e ottimizzazioni
- Gestione delle preferenze di viaggio
- Condivisione degli itinerari
- Versionamento degli itinerari

## Tecnologie Utilizzate

- Java 17
- Spring Boot 3.2.3
- Spring Cloud OpenFeign
- SpringDoc OpenAPI
- Spring Data JPA
- PostgreSQL
- Caffeine Cache
- Lombok
- MapStruct

## Requisiti

- JDK 17 o superiore
- Maven 3.6 o superiore
- PostgreSQL 15 o superiore

## Configurazione

Il servizio richiede le seguenti variabili d'ambiente:

```yaml
spring:
  application:
    name: buildyourtrip-itineraries
  datasource:
    url: jdbc:postgresql://localhost:5432/buildyourtrip
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 8083

itineraries:
  cache:
    enabled: true
    ttl: 3600
    max-size: 1000
  maps-service:
    url: http://localhost:8081
  ai-service:
    url: http://localhost:8082
```

## API Endpoints

### Itinerari
- `POST /api/itineraries`
  - Crea un nuovo itinerario
- `GET /api/itineraries/{id}`
  - Recupera un itinerario specifico
- `PUT /api/itineraries/{id}`
  - Aggiorna un itinerario esistente
- `DELETE /api/itineraries/{id}`
  - Elimina un itinerario
- `GET /api/itineraries`
  - Lista tutti gli itinerari dell'utente

### Tappe
- `POST /api/itineraries/{itineraryId}/stops`
  - Aggiunge una tappa a un itinerario
- `PUT /api/itineraries/{itineraryId}/stops/{stopId}`
  - Aggiorna una tappa
- `DELETE /api/itineraries/{itineraryId}/stops/{stopId}`
  - Rimuove una tappa
- `PUT /api/itineraries/{itineraryId}/stops/reorder`
  - Riordina le tappe di un itinerario

### Eventi
- `POST /api/itineraries/{itineraryId}/events`
  - Aggiunge un evento a una tappa
- `PUT /api/itineraries/{itineraryId}/events/{eventId}`
  - Aggiorna un evento
- `DELETE /api/itineraries/{itineraryId}/events/{eventId}`
  - Rimuove un evento

### Condivisione
- `POST /api/itineraries/{id}/share`
  - Condivide un itinerario con altri utenti
- `DELETE /api/itineraries/{id}/share/{userId}`
  - Rimuove la condivisione con un utente

## Cache

Il servizio utilizza Caffeine Cache per:
- Cache degli itinerari: TTL di 1 ora
- Cache delle tappe: TTL di 1 ora
- Cache degli eventi: TTL di 1 ora

## Documentazione API

La documentazione OpenAPI è disponibile all'indirizzo:
- Swagger UI: `http://localhost:8083/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8083/v3/api-docs`

## Build e Deploy

```bash
# Build
mvn clean package

# Run
java -jar target/buildyourtrip-itineraries-0.0.1-SNAPSHOT.jar
```

## Test

```bash
# Esegui i test
mvn test

# Esegui i test con coverage
mvn verify
```

## Licenza

MIT License - vedere il file [LICENSE](LICENSE) per i dettagli. 