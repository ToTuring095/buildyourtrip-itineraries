package com.buildyourtrip.itineraries.controller;

import com.buildyourtrip.itineraries.domain.Itinerary;
import com.buildyourtrip.itineraries.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/itineraries")
@RequiredArgsConstructor
public class ItineraryController {
    private final ItineraryService itineraryService;

    @GetMapping
    public ResponseEntity<List<Itinerary>> getAllItineraries(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(itineraryService.getAllItineraries(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Itinerary> getItineraryById(@PathVariable Long id) {
        return itineraryService.getItineraryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Itinerary> createItinerary(@RequestBody Itinerary itinerary) {
        return ResponseEntity.ok(itineraryService.createItinerary(itinerary));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Itinerary> updateItinerary(
            @PathVariable Long id,
            @RequestBody Itinerary itineraryDetails) {
        return ResponseEntity.ok(itineraryService.updateItinerary(id, itineraryDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItinerary(@PathVariable Long id) {
        itineraryService.deleteItinerary(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Itinerary>> searchItineraries(
            @RequestParam String destination) {
        return ResponseEntity.ok(itineraryService.searchItinerariesByDestination(destination));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Itinerary>> getItinerariesByDateRange(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(itineraryService.getItinerariesByDateRange(userId, startDate, endDate));
    }
} 