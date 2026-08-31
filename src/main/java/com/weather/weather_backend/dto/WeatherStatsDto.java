package com.weather.weather_backend.dto;

public class WeatherStatsDto {
    private Double minTempOutdoor;
    private Double maxTempOutdoor;
    private Double avgTempOutdoor;
    private Double totalRain;
    private Double avgPressure;
    
    public WeatherStatsDto(Double minTempOutdoor, Double maxTempOutdoor, Double avgTempOutdoor, Double totalRain, Double avgPressure) {
        this.minTempOutdoor = minTempOutdoor;
        this.maxTempOutdoor = maxTempOutdoor;
        this.avgTempOutdoor = avgTempOutdoor;
        this.totalRain = totalRain;
        this.avgPressure = avgPressure;
    }

    public Double getMinTempOutdoor() { return minTempOutdoor; }
    public Double getMaxTempOutdoor() { return maxTempOutdoor; }
    public Double getAvgTempOutdoor() { return avgTempOutdoor; }
    public Double getTotalRain() { return totalRain; }
    public Double getAvgPressure() { return avgPressure; }
}