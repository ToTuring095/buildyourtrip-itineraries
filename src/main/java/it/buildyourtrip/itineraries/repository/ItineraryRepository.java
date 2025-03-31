package it.buildyourtrip.itineraries.repository;

import it.buildyourtrip.itineraries.domain.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByUserId(Long userId);
    
    List<Itinerary> findByUserIdAndStartDateBetweenAndEndDateBetween(
        Long userId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime startDate2,
        LocalDateTime endDate2
    );
    
    List<Itinerary> findByTitleContainingIgnoreCase(String title);
} 