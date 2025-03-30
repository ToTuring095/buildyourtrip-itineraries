package com.buildyourtrip.itineraries.model;

import lombok.Data;

import java.util.List;

@Data
public class Destination {
    private String title;
    private String description;
    private String image;
    private List<String> highlights;
    private double rating;
    private String bestTimeToVisit;
} 