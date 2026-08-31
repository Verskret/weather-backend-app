package com.weather.weather_backend.controller;

import com.weather.weather_backend.dto.WeatherStatsDto;
import com.weather.weather_backend.model.WeatherData;
import com.weather.weather_backend.repository.WeatherRepository;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherRepository weatherRepository;

    @org.springframework.beans.factory.annotation.Value("${GEMINI_API_KEY:}")
    private String geminiApiKey;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestParam("photo") MultipartFile photo) {
        if (photo.isEmpty()) {
            return ResponseEntity.badRequest().body("Chyba: Nebyla odeslána žádná fotka.");
        }

        try {
            File tempFile = File.createTempFile("weather_upload_", ".jpg");
            photo.transferTo(tempFile);

            Client client = Client.builder().apiKey(geminiApiKey).build();
            byte[] imageBytes = Files.readAllBytes(tempFile.toPath());

            String prompt = "Přečti z této fotky meteostanice následující hodnoty a vrať je POUZE jako JSON objekt (bez markdown značek, jen čistý text) s těmito klíči:\n" +
                    "indoorTemp (číslo, např. 21.1),\n" +
                    "indoorHumidity (číslo, např. 57),\n" +
                    "outdoorTemp (číslo, např. 11.1),\n" +
                    "outdoorHumidity (číslo, např. 95),\n" +
                    "pressure (číslo, např. 1005),\n" +
                    "rain (číslo, např. 86.8),\n" +
                    "windSpeed (číslo, např. 1),\n" +
                    "windDirection (text - EXTRÉMNĚ DŮLEŽITÉ PRO VÍTR: Na větrné růžici úplně ignoruj čárku napravo. Podívej se na kruhový ciferník: dole na pozici 6 hodin (přesně pod středem) je bílý trojúhelník/šipka ukazující ven na písmeno S. Vrať přesně písmeno S),\n" +
                    "stationTime (text, např. 17:00),\n" +
                    "stationDate (text, např. 10.6),\n" +
                    "stationDay (text, např. WED).\n" +
                    "Dbej na maximální přesnost čísel. Pokud hodnota chybí, dej 0 nebo null.";

            GenerateContentResponse response = client.models.generateContent(
                "gemini-3.6-flash",
                com.google.genai.types.Content.fromParts(
                    com.google.genai.types.Part.fromText(prompt),
                    com.google.genai.types.Part.fromBytes(imageBytes, "image/jpeg")
                ),
                null
            );

            String jsonResponse = response.text().trim();
            jsonResponse = jsonResponse.replaceAll("```json", "").replaceAll("```", "").trim();

            System.out.println("AI JSON: " + jsonResponse);

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(jsonResponse);

            WeatherData data = new WeatherData();
            data.setTimestamp(LocalDateTime.now());
            data.setIndoorTemp(root.path("indoorTemp").asDouble());
            data.setIndoorHumidity(root.path("indoorHumidity").asDouble());
            data.setOutdoorTemp(root.path("outdoorTemp").asDouble());
            data.setOutdoorHumidity(root.path("outdoorHumidity").asDouble());
            data.setPressure(root.path("pressure").asDouble());
            data.setRain(root.path("rain").asDouble());
            data.setWindSpeed(root.path("windSpeed").asDouble());
            data.setWindDirection(root.path("windDirection").asText());
            data.setStationTime(root.path("stationTime").asText());
            data.setStationDate(root.path("stationDate").asText());
            data.setStationDay(root.path("stationDay").asText());

            weatherRepository.save(data);

            tempFile.delete();
            return ResponseEntity.ok("Všechna data včetně opraveného směru větru uložena!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Chyba při zpracování: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<java.util.List<WeatherData>> getAllWeatherData() {
        java.util.List<WeatherData> allData = weatherRepository.findAll();
        return ResponseEntity.ok(allData);
    }

    @GetMapping("/stats")
    public WeatherStatsDto getStats(
            @RequestParam("period") String period, // "day", "week", "month", "year"
            @RequestParam("date") String dateStr   // např. "2026-08-31"
    ) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalDateTime start;
        LocalDateTime end = date.atTime(23, 59, 59);

        switch (period.toLowerCase()) {
            case "week":
                start = date.minusDays(6).atStartOfDay();
                break;
            case "month":
                start = date.withDayOfMonth(1).atStartOfDay();
                end = date.withDayOfMonth(date.lengthOfMonth()).atTime(23, 59, 59);
                break;
            case "year":
                start = date.withDayOfYear(1).atStartOfDay();
                end = date.withDayOfYear(date.lengthOfYear()).atTime(23, 59, 59);
                break;
            case "day":
            default:
                start = date.atStartOfDay();
                break;
        }

        Object[] result = (Object[]) weatherRepository.getOutdoorStatsByPeriod(start, end);
        
        Double minTemp = (result != null && result[0] != null) ? (Double) result[0] : 0.0;
        Double maxTemp = (result != null && result[1] != null) ? (Double) result[1] : 0.0;
        Double avgTemp = (result != null && result[2] != null) ? (Double) result[2] : 0.0;
        Double rain = (result != null && result[3] != null) ? (Double) result[3] : 0.0;

        return new WeatherStatsDto(minTemp, maxTemp, avgTemp, rain, 1013.2);
    }
}