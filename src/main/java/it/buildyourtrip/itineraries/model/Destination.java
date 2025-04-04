package it.buildyourtrip.itineraries.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String image;
    
    @ElementCollection
    private List<String> highlights;
    
    private double rating;
    private String bestTimeToVisit;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
} 