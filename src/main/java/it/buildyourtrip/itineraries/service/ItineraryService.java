package it.buildyourtrip.itineraries.service;

import it.buildyourtrip.itineraries.domain.Itinerary;
import it.buildyourtrip.itineraries.model.Destination;
import it.buildyourtrip.itineraries.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;

    public List<Itinerary> getAllItineraries(Long userId) {
        return itineraryRepository.findByUserId(userId);
    }

    public Optional<Itinerary> getItineraryById(Long id) {
        return itineraryRepository.findById(id);
    }

    @Transactional
    public Itinerary createItinerary(Itinerary itinerary) {
        calculateItineraryDates(itinerary);
        return itineraryRepository.save(itinerary);
    }

    @Transactional
    public Itinerary updateItinerary(Long id, Itinerary itineraryDetails) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        itinerary.setTitle(itineraryDetails.getTitle());
        itinerary.setDescription(itineraryDetails.getDescription());
        itinerary.setDestinations(itineraryDetails.getDestinations());
        itinerary.setStatus(itineraryDetails.getStatus());
        itinerary.setTotalBudget(itineraryDetails.getTotalBudget());
        itinerary.setTags(itineraryDetails.getTags());

        calculateItineraryDates(itinerary);

        return itineraryRepository.save(itinerary);
    }

    @Transactional
    public void deleteItinerary(Long id) {
        itineraryRepository.deleteById(id);
    }

    public List<Itinerary> searchItinerariesByDestination(String destination) {
        return itineraryRepository.findByTitleContainingIgnoreCase(destination);
    }

    public List<Itinerary> getItinerariesByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return itineraryRepository.findByUserIdAndStartDateBetweenAndEndDateBetween(
            userId, startDate, endDate, startDate, endDate
        );
    }

    private void calculateItineraryDates(Itinerary itinerary) {
        if (itinerary.getDestinations() == null || itinerary.getDestinations().isEmpty()) {
            itinerary.setStartDate(LocalDateTime.now());
            itinerary.setEndDate(LocalDateTime.now());
            return;
        }

        LocalDateTime earliestStart = itinerary.getDestinations().stream()
            .map(Destination::getStartDate)
            .filter(date -> date != null)
            .min(LocalDateTime::compareTo)
            .orElse(LocalDateTime.now());

        LocalDateTime latestEnd = itinerary.getDestinations().stream()
            .map(Destination::getEndDate)
            .filter(date -> date != null)
            .max(LocalDateTime::compareTo)
            .orElse(LocalDateTime.now());

        itinerary.setStartDate(earliestStart);
        itinerary.setEndDate(latestEnd);
    }
} 