package it.buildyourtrip.itineraries.controller;

import it.buildyourtrip.itineraries.model.Destination;
import it.buildyourtrip.itineraries.model.DestinationPreferences;
import it.buildyourtrip.itineraries.service.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/itineraries")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    @PostMapping("/recommended-destinations")
    public ResponseEntity<List<Destination>> getRecommendedDestinations(
            @RequestBody DestinationPreferences preferences) {
        return ResponseEntity.ok(destinationService.getRecommendedDestinations(preferences));
    }

    @GetMapping("/destinations/{destination}")
    public ResponseEntity<Destination> getDestinationDetails(
            @PathVariable String destination) {
        return ResponseEntity.ok(destinationService.getDestinationDetails(destination));
    }
} 