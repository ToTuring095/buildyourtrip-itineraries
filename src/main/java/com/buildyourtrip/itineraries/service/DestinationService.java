package com.buildyourtrip.itineraries.service;

import com.buildyourtrip.itineraries.client.AiServiceClient;
import com.buildyourtrip.itineraries.model.Destination;
import com.buildyourtrip.itineraries.model.DestinationPreferences;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final AiServiceClient aiServiceClient;

    public List<Destination> getRecommendedDestinations(DestinationPreferences preferences) {
        // Costruiamo un prompt per l'AI basato sulle preferenze
        String prompt = buildPromptForRecommendations(preferences);
        
        // Chiamiamo l'AI service per ottenere le raccomandazioni
        return aiServiceClient.getDestinationRecommendations(prompt);
    }

    public Destination getDestinationDetails(String destination) {
        // Costruiamo un prompt per ottenere dettagli sulla destinazione
        String prompt = buildPromptForDetails(destination);
        
        // Chiamiamo l'AI service per ottenere i dettagli
        return aiServiceClient.getDestinationDetails(prompt);
    }

    private String buildPromptForRecommendations(DestinationPreferences preferences) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Suggerisci destinazioni di viaggio in Italia ");
        
        if (preferences.getSeason() != null) {
            prompt.append("per la stagione ").append(preferences.getSeason()).append(" ");
        }
        
        if (preferences.getBudget() != null) {
            prompt.append("con un budget ").append(preferences.getBudget()).append(" ");
        }
        
        if (preferences.getInterests() != null && !preferences.getInterests().isEmpty()) {
            prompt.append("con focus su: ").append(String.join(", ", preferences.getInterests()));
        }
        
        prompt.append(". Per ogni destinazione, includi: titolo, descrizione breve, punti di interesse, " +
                "valutazione, periodo migliore per visitare e un'immagine rappresentativa.");
        
        return prompt.toString();
    }

    private String buildPromptForDetails(String destination) {
        return String.format(
            "Fornisci informazioni dettagliate su %s come destinazione turistica, includendo: " +
            "descrizione completa, principali attrazioni, periodo migliore per visitare, " +
            "valutazione turistica e un'immagine rappresentativa.", destination);
    }
} 