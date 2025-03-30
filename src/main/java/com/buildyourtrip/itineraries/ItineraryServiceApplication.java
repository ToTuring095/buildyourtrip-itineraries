package com.buildyourtrip.itineraries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ItineraryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItineraryServiceApplication.class, args);
    }
} 