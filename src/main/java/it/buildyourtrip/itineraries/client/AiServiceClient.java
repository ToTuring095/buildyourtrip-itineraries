package it.buildyourtrip.itineraries.client;

import it.buildyourtrip.itineraries.model.Destination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ai-service", url = "${ai.service.url}")
public interface AiServiceClient {

    @PostMapping("/api/v1/ai/destinations/recommend")
    List<Destination> getDestinationRecommendations(@RequestBody String prompt);

    @PostMapping("/api/v1/ai/destinations/details")
    Destination getDestinationDetails(@RequestBody String prompt);
} 