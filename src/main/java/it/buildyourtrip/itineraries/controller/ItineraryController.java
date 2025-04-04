package it.buildyourtrip.itineraries.controller;

import it.buildyourtrip.itineraries.domain.Itinerary;
import it.buildyourtrip.itineraries.dto.ApiResponse;
import it.buildyourtrip.itineraries.dto.ItineraryDTO;
import it.buildyourtrip.itineraries.exception.ResourceNotFoundException;
import it.buildyourtrip.itineraries.mapper.ItineraryMapper;
import it.buildyourtrip.itineraries.service.ItineraryService;
import it.buildyourtrip.itineraries.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/itineraries")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;
    
    @Autowired
    private ItineraryMapper itineraryMapper;
    
    @Autowired
    private SecurityUtils securityUtils;

    // Ottiene tutti gli itinerari per un utente
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItineraryDTO>>> getAllItineraries(
            @RequestHeader("X-User-Id") Long userId) {
        List<Itinerary> itineraries = itineraryService.getAllItineraries(userId);
        List<ItineraryDTO> itineraryDTOs = itineraryMapper.toDtoList(itineraries);
        
        ApiResponse<List<ItineraryDTO>> response = new ApiResponse<>(
            true, itineraryDTOs, "Itinerari recuperati con successo");
        return ResponseEntity.ok(response);
    }

    // Ottiene un itinerario specifico
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItineraryDTO>> getItineraryById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        Itinerary itinerary = itineraryService.getItineraryById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Itinerario", "id", id));
        
        // Verifica che l'utente abbia accesso all'itinerario
        securityUtils.checkItineraryAccess(itinerary, userId);
        
        ItineraryDTO itineraryDTO = itineraryMapper.toDto(itinerary);
        
        ApiResponse<ItineraryDTO> response = new ApiResponse<>(
            true, itineraryDTO, "Itinerario recuperato con successo");
        return ResponseEntity.ok(response);
    }

    // Crea un nuovo itinerario
    @PostMapping
    public ResponseEntity<ApiResponse<ItineraryDTO>> createItinerary(
            @RequestBody ItineraryDTO itineraryDTO,
            @RequestHeader("X-User-Id") Long userId) {
        itineraryDTO.setUserId(userId); // Imposta l'ID utente del richiedente
        
        Itinerary itinerary = itineraryMapper.toEntity(itineraryDTO);
        Itinerary createdItinerary = itineraryService.createItinerary(itinerary);
        ItineraryDTO createdItineraryDTO = itineraryMapper.toDto(createdItinerary);
        
        ApiResponse<ItineraryDTO> response = new ApiResponse<>(
            true, createdItineraryDTO, "Itinerario creato con successo");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Aggiorna un itinerario esistente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItineraryDTO>> updateItinerary(
            @PathVariable Long id,
            @RequestBody ItineraryDTO itineraryDTO,
            @RequestHeader("X-User-Id") Long userId) {
        // Verifica che l'itinerario esista e che l'utente abbia accesso
        Itinerary existingItinerary = itineraryService.getItineraryById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Itinerario", "id", id));
        securityUtils.checkItineraryAccess(existingItinerary, userId);
        
        itineraryDTO.setId(id);
        itineraryDTO.setUserId(userId);
        
        Itinerary itinerary = itineraryMapper.toEntity(itineraryDTO);
        Itinerary updatedItinerary = itineraryService.updateItinerary(id, itinerary);
        ItineraryDTO updatedItineraryDTO = itineraryMapper.toDto(updatedItinerary);
        
        ApiResponse<ItineraryDTO> response = new ApiResponse<>(
            true, updatedItineraryDTO, "Itinerario aggiornato con successo");
        return ResponseEntity.ok(response);
    }

    // Elimina un itinerario
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItinerary(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        // Verifica che l'itinerario esista e che l'utente abbia accesso
        Itinerary existingItinerary = itineraryService.getItineraryById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Itinerario", "id", id));
        securityUtils.checkItineraryAccess(existingItinerary, userId);
        
        itineraryService.deleteItinerary(id);
        
        ApiResponse<Void> response = new ApiResponse<>(
            true, null, "Itinerario eliminato con successo");
        return ResponseEntity.ok(response);
    }

    // Cerca itinerari per destinazione
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ItineraryDTO>>> searchByDestination(
            @RequestParam String destination,
            @RequestHeader("X-User-Id") Long userId) {
        List<Itinerary> itineraries = itineraryService.searchItinerariesByDestination(destination);
        // Filtra solo gli itinerari dell'utente
        itineraries = itineraries.stream()
            .filter(itinerary -> itinerary.getUserId().equals(userId))
            .toList();
            
        List<ItineraryDTO> itineraryDTOs = itineraryMapper.toDtoList(itineraries);
        
        ApiResponse<List<ItineraryDTO>> response = new ApiResponse<>(
            true, itineraryDTOs, "Ricerca completata con successo");
        return ResponseEntity.ok(response);
    }

    // Filtra itinerari per intervallo di date
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<ItineraryDTO>>> filterByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestHeader("X-User-Id") Long userId) {
        List<Itinerary> itineraries = itineraryService.getItinerariesByDateRange(userId, startDate, endDate);
        List<ItineraryDTO> itineraryDTOs = itineraryMapper.toDtoList(itineraries);
        
        ApiResponse<List<ItineraryDTO>> response = new ApiResponse<>(
            true, itineraryDTOs, "Filtro applicato con successo");
        return ResponseEntity.ok(response);
    }
} 