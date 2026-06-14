package com.smarttourist.agent.controller;

import com.smarttourist.agent.model.*;
import com.smarttourist.agent.repository.*;
import com.smarttourist.agent.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PublicController {

    private final DestinationRepository destinationRepository;
    private final VehicleRepository vehicleRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final GuideRepository guideRepository;
    private final AgentRepository agentRepository;
    private final WeatherService weatherService;

    public PublicController(
            DestinationRepository destinationRepository,
            VehicleRepository vehicleRepository,
            HotelRepository hotelRepository,
            RoomRepository roomRepository,
            GuideRepository guideRepository,
            AgentRepository agentRepository,
            WeatherService weatherService
    ) {
        this.destinationRepository = destinationRepository;
        this.vehicleRepository = vehicleRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.guideRepository = guideRepository;
        this.agentRepository = agentRepository;
        this.weatherService = weatherService;
    }

    @GetMapping("/destinations")
    public List<Destination> getDestinations() {
        return destinationRepository.findAll();
    }

    @GetMapping("/vehicles")
    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/hotels")
    public List<Hotel> getHotels(@RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty() && !category.equalsIgnoreCase("ALL")) {
            return hotelRepository.findByCategory(category.toUpperCase());
        }
        return hotelRepository.findAll();
    }

    @GetMapping("/hotels/{hotelId}/rooms")
    public List<Room> getHotelRooms(@PathVariable Long hotelId) {
        return roomRepository.findByHotelIdAndIsAvailable(hotelId, true);
    }

    @GetMapping("/guides")
    public List<Guide> getGuides(@RequestParam(required = false) String destination) {
        if (destination != null && !destination.isEmpty()) {
            return guideRepository.findByCurrentLocationAndIsAvailable(destination, true);
        }
        return guideRepository.findByIsAvailable(true);
    }

    @GetMapping("/agents")
    public ResponseEntity<?> getAgentProfile() {
        List<Agent> agents = agentRepository.findAll();
        if (agents.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No agent profile found"));
        }
        return ResponseEntity.ok(agents.get(0)); // Return first agent profile seeded
    }

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestParam String destination) {
        return ResponseEntity.ok(weatherService.getWeather(destination));
    }
}
