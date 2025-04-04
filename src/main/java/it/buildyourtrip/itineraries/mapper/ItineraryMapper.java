package it.buildyourtrip.itineraries.mapper;

import it.buildyourtrip.itineraries.domain.Itinerary;
import it.buildyourtrip.itineraries.dto.ItineraryDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItineraryMapper {

    /**
     * Converte un oggetto Itinerary in ItineraryDTO
     */
    public ItineraryDTO toDto(Itinerary itinerary) {
        if (itinerary == null) {
            return null;
        }

        ItineraryDTO dto = new ItineraryDTO();
        dto.setId(itinerary.getId());
        dto.setTitle(itinerary.getTitle());
        dto.setDescription(itinerary.getDescription());
        dto.setDestinations(itinerary.getDestinations());
        dto.setStatus(itinerary.getStatus());
        dto.setStartDate(itinerary.getStartDate());
        dto.setEndDate(itinerary.getEndDate());
        dto.setTotalBudget(itinerary.getTotalBudget());
        dto.setTags(itinerary.getTags());
        dto.setUserId(itinerary.getUserId());
        
        return dto;
    }

    /**
     * Converte un oggetto ItineraryDTO in Itinerary
     */
    public Itinerary toEntity(ItineraryDTO dto) {
        if (dto == null) {
            return null;
        }

        Itinerary itinerary = new Itinerary();
        itinerary.setId(dto.getId());
        itinerary.setTitle(dto.getTitle());
        itinerary.setDescription(dto.getDescription());
        itinerary.setDestinations(dto.getDestinations());
        itinerary.setStatus(dto.getStatus());
        itinerary.setStartDate(dto.getStartDate());
        itinerary.setEndDate(dto.getEndDate());
        itinerary.setTotalBudget(dto.getTotalBudget() != null ? dto.getTotalBudget() : 0.0);
        itinerary.setTags(dto.getTags());
        itinerary.setUserId(dto.getUserId());
        
        return itinerary;
    }

    /**
     * Converte una lista di Itinerary in una lista di ItineraryDTO
     */
    public List<ItineraryDTO> toDtoList(List<Itinerary> itineraries) {
        if (itineraries == null) {
            return null;
        }
        
        return itineraries.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Converte una lista di ItineraryDTO in una lista di Itinerary
     */
    public List<Itinerary> toEntityList(List<ItineraryDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
} 