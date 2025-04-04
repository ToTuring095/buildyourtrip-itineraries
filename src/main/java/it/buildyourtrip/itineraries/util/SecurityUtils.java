package it.buildyourtrip.itineraries.util;

import it.buildyourtrip.itineraries.exception.UnauthorizedException;
import it.buildyourtrip.itineraries.domain.Itinerary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityUtils {

    /**
     * Verifica se l'utente ha accesso all'itinerario
     * @param itinerary L'itinerario da verificare
     * @param userId L'ID dell'utente che richiede l'accesso
     * @throws UnauthorizedException Se l'utente non ha accesso
     */
    public void checkItineraryAccess(Itinerary itinerary, Long userId) {
        if (itinerary == null) {
            log.error("Errore di sicurezza: tentativo di accesso a un itinerario nullo");
            throw new UnauthorizedException("Itinerario non trovato");
        }
        
        if (!itinerary.getUserId().equals(userId)) {
            log.error("Accesso non autorizzato: L'utente {} ha tentato di accedere all'itinerario {} di proprietà dell'utente {}", 
                userId, itinerary.getId(), itinerary.getUserId());
            throw new UnauthorizedException("Non hai i permessi per accedere a questo itinerario");
        }
    }
} 