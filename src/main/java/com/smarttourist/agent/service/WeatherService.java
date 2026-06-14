package com.smarttourist.agent.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class WeatherService {

    // Simple cache record for mock weather
    public record WeatherInfo(String destination, String status, String temperature, String humidity, String windSpeed) {}

    private final Map<String, WeatherInfo> weatherDatabase = new HashMap<>();

    public WeatherService() {
        // Seed mock weather for destinations
        weatherDatabase.put("goa", new WeatherInfo("Goa", "Sunny & Tropical", "31°C", "72%", "14 km/h"));
        weatherDatabase.put("shimla", new WeatherInfo("Shimla", "Cool & Clear", "16°C", "45%", "8 km/h"));
        weatherDatabase.put("kerala backwaters", new WeatherInfo("Kerala Backwaters", "Humid & Rainy", "28°C", "85%", "11 km/h"));
        weatherDatabase.put("paris", new WeatherInfo("Paris", "Overcast & Cloudy", "19°C", "60%", "15 km/h"));
        weatherDatabase.put("tokyo", new WeatherInfo("Tokyo", "Clear Sky", "22°C", "50%", "10 km/h"));
    }

    public WeatherInfo getWeather(String destinationName) {
        if (destinationName == null) {
            return new WeatherInfo("Default", "Pleasant", "24°C", "55%", "12 km/h");
        }
        String key = destinationName.trim().toLowerCase();
        return weatherDatabase.getOrDefault(key, new WeatherInfo(destinationName, "Mild & Sunny", "25°C", "58%", "12 km/h"));
    }
}
