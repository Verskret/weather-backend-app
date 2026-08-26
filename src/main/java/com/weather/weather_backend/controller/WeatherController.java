package com.weather.weatherbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @PostMapping("/upload")
    public ResponseEntity<String> receiveImage(@RequestBody WeatherRequest request) {
        // Tady uvidíš v logách na Renderu to, co ti pošle MacroDroid
        System.out.println("Přijat požadavek z MacroDroidu s obsahem: " + request.getImage());
        
        // Zde pak později doplníme zpracování a volání Gemini API
        
        return ResponseEntity.ok("Zpracováno v pořádku!");
    }
}

// Pomocná třída pro přijetí JSON struktury
class WeatherRequest {
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}