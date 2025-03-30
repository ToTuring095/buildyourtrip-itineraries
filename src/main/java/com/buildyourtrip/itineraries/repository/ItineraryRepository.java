package com.buildyourtrip.itineraries.repository;

import com.buildyourtrip.itineraries.domain.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByUserId(Long userId);

    @Query("SELECT i FROM Itinerary i WHERE i.userId = :userId AND " +
           "(:startDate BETWEEN i.startDate AND i.endDate OR " +
           ":endDate BETWEEN i.startDate AND i.endDate OR " +
           "i.startDate BETWEEN :startDate AND :endDate OR " +
           "i.endDate BETWEEN :startDate AND :endDate)")
    List<Itinerary> findByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Itinerary> findByDestinationContainingIgnoreCase(String destination);
} 