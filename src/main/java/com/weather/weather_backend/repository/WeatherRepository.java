package com.weather.weather_backend.repository;

import com.weather.weather_backend.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, Long> {

    // Vrátí záznamy v daném časovém rozmezí (pro den, týden, měsíc, rok)
    List<WeatherData> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // Agregace pro statistiky s opraveným názvem proměnné w.outdoorTemp
    @Query("SELECT MIN(w.outdoorTemp), MAX(w.outdoorTemp), AVG(w.outdoorTemp), SUM(w.rain) " +
           "FROM WeatherData w WHERE w.timestamp BETWEEN :start AND :end")
    Object[] getOutdoorStatsByPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}