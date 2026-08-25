package com.weather.weather_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private Double indoorTemp;
    private Double indoorHumidity;
    private Double outdoorTemp;
    private Double outdoorHumidity;
    private Double pressure;
    private Double rain;
    private Double windSpeed;
    private String windDirection;
    private String stationTime;
    private String stationDate;
    private String stationDay; // Nové pole pro den v týdnu (např. WED)

    // Gettery a settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Double getIndoorTemp() { return indoorTemp; }
    public void setIndoorTemp(Double indoorTemp) { this.indoorTemp = indoorTemp; }

    public Double getIndoorHumidity() { return indoorHumidity; }
    public void setIndoorHumidity(Double indoorHumidity) { this.indoorHumidity = indoorHumidity; }

    public Double getOutdoorTemp() { return outdoorTemp; }
    public void setOutdoorTemp(Double outdoorTemp) { this.outdoorTemp = outdoorTemp; }

    public Double getOutdoorHumidity() { return outdoorHumidity; }
    public void setOutdoorHumidity(Double outdoorHumidity) { this.outdoorHumidity = outdoorHumidity; }

    public Double getPressure() { return pressure; }
    public void setPressure(Double pressure) { this.pressure = pressure; }

    public Double getRain() { return rain; }
    public void setRain(Double rain) { this.rain = rain; }

    public Double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }

    public String getWindDirection() { return windDirection; }
    public void setWindDirection(String windDirection) { this.windDirection = windDirection; }

    public String getStationTime() { return stationTime; }
    public void setStationTime(String stationTime) { this.stationTime = stationTime; }

    public String getStationDate() { return stationDate; }
    public void setStationDate(String stationDate) { this.stationDate = stationDate; }

    public String getStationDay() { return stationDay; }
    public void setStationDay(String stationDay) { this.stationDay = stationDay; }
}