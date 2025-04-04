package it.buildyourtrip.itineraries.dto;

import it.buildyourtrip.itineraries.model.Destination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryDTO {
    private Long id;
    private String title;
    private String description;
    private List<Destination> destinations;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalBudget;
    private List<String> tags;
    private Long userId;
} 