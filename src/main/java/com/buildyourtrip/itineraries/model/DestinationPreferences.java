package com.buildyourtrip.itineraries.model;

import lombok.Data;

import java.util.List;

@Data
public class DestinationPreferences {
    private String season;
    private String budget;
    private List<String> interests;
} 