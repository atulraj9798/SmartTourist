package com.smarttourist.agent.service;

import com.smarttourist.agent.dto.BudgetRequest;
import com.smarttourist.agent.dto.BudgetResponse;
import com.smarttourist.agent.model.*;
import com.smarttourist.agent.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TourService {

    private final TourRepository tourRepository;
    private final DestinationRepository destinationRepository;
    private final VehicleRepository vehicleRepository;
    private final RoomRepository roomRepository;
    private final GuideRepository guideRepository;

    public TourService(
            TourRepository tourRepository,
            DestinationRepository destinationRepository,
            VehicleRepository vehicleRepository,
            RoomRepository roomRepository,
            GuideRepository guideRepository
    ) {
        this.tourRepository = tourRepository;
        this.destinationRepository = destinationRepository;
        this.vehicleRepository = vehicleRepository;
        this.roomRepository = roomRepository;
        this.guideRepository = guideRepository;
    }

    public BudgetResponse calculateBudget(BudgetRequest request) {
        LocalDate start = LocalDate.parse(request.startDate());
        LocalDate end = LocalDate.parse(request.endDate());
        int days = (int) ChronoUnit.DAYS.between(start, end);
        if (days <= 0) days = 1; // Minimum 1 day tour

        int adults = request.numAdults() != null ? request.numAdults() : 1;
        int children = request.numChildren() != null ? request.numChildren() : 0;
        int seniors = request.numSeniors() != null ? request.numSeniors() : 0;
        int totalTravelers = adults + children + seniors;

        double vehicleCost = 0.0;
        if (request.vehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.vehicleId()).orElse(null);
            if (vehicle != null) {
                // Calculation: (fare per day * days) + (fare per km * flat simulated 100km per day)
                vehicleCost = (vehicle.getFarePerDay() * days) + (vehicle.getFarePerKm() * 100.0 * days);
            }
        }

        double hotelCost = 0.0;
        if (request.roomId() != null) {
            Room room = roomRepository.findById(request.roomId()).orElse(null);
            if (room != null) {
                // Room count estimation: 2 travelers per room
                int roomsNeeded = (int) Math.ceil((double) (adults + seniors) / 2.0);
                if (roomsNeeded == 0) roomsNeeded = 1;
                hotelCost = room.getPricePerNight() * days * roomsNeeded;
            }
        }

        double guideCost = 0.0;
        if (request.guideId() != null) {
            Guide guide = guideRepository.findById(request.guideId()).orElse(null);
            if (guide != null) {
                guideCost = guide.getChargesPerDay() * days;
            }
        }

        // Food cost estimation: 800 INR per person per day
        double foodCost = 800.0 * totalTravelers * days;

        // Tax calculation: 18% GST
        double subtotal = vehicleCost + hotelCost + guideCost + foodCost;
        double taxes = subtotal * 0.18;
        double totalCost = subtotal + taxes;

        return new BudgetResponse(
                Math.round(vehicleCost * 100.0) / 100.0,
                Math.round(hotelCost * 100.0) / 100.0,
                Math.round(foodCost * 100.0) / 100.0,
                Math.round(guideCost * 100.0) / 100.0,
                Math.round(taxes * 100.0) / 100.0,
                Math.round(totalCost * 100.0) / 100.0,
                days
        );
    }

    public List<Tour> getToursByUserId(Long userId) {
        return tourRepository.findByUserId(userId);
    }

    public Destination getDestinationById(Long id) {
        return destinationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Destination not found: " + id));
    }
}
