package com.smarttourist.agent.controller;

import com.smarttourist.agent.model.*;
import com.smarttourist.agent.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final VehicleRepository vehicleRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final GuideRepository guideRepository;

    public AdminController(
            UserRepository userRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            VehicleRepository vehicleRepository,
            HotelRepository hotelRepository,
            RoomRepository roomRepository,
            GuideRepository guideRepository
    ) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.vehicleRepository = vehicleRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.guideRepository = guideRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getAdminStats() {
        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.count();
        List<Booking> allBookings = bookingRepository.findAll();
        long completedBookings = allBookings.stream().filter(b -> "CONFIRMED".equals(b.getStatus())).count();
        long pendingBookings = allBookings.stream().filter(b -> "PENDING".equals(b.getStatus())).count();

        double totalRevenue = allBookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .mapToDouble(Booking::getTotalAmount)
                .sum();

        // Calculate popular destinations
        Map<String, Long> destinationCounts = allBookings.stream()
                .collect(Collectors.groupingBy(b -> b.getTour().getDestination().getName(), Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalBookings", totalBookings);
        stats.put("completedBookings", completedBookings);
        stats.put("pendingBookings", pendingBookings);
        stats.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
        stats.put("popularDestinations", destinationCounts);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Vehicle Management
    @PostMapping("/vehicles")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle details) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + id));
        vehicle.setName(details.getName());
        vehicle.setType(details.getType());
        vehicle.setSeatingCapacity(details.getSeatingCapacity());
        vehicle.setFarePerKm(details.getFarePerKm());
        vehicle.setFarePerDay(details.getFarePerDay());
        vehicle.setIsAvailable(details.getIsAvailable());
        if (details.getImageUrl() != null) {
            vehicle.setImageUrl(details.getImageUrl());
        }
        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        vehicleRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Vehicle deleted successfully"));
    }

    // Hotel Management
    @PostMapping("/hotels")
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @PostMapping("/hotels/{hotelId}/rooms")
    public Room addRoomToHotel(@PathVariable Long hotelId, @RequestBody Room room) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found: " + hotelId));
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    // Guide Management
    @PostMapping("/guides")
    public Guide createGuide(@RequestBody Guide guide) {
        return guideRepository.save(guide);
    }

    @PutMapping("/guides/{id}")
    public ResponseEntity<Guide> updateGuide(@PathVariable Long id, @RequestBody Guide details) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Guide not found: " + id));
        guide.setName(details.getName());
        guide.setLanguages(details.getLanguages());
        guide.setExperienceYears(details.getExperienceYears());
        guide.setChargesPerDay(details.getChargesPerDay());
        guide.setIsAvailable(details.getIsAvailable());
        guide.setCurrentLocation(details.getCurrentLocation());
        if (details.getPhotoUrl() != null) {
            guide.setPhotoUrl(details.getPhotoUrl());
        }
        return ResponseEntity.ok(guideRepository.save(guide));
    }

    @DeleteMapping("/guides/{id}")
    public ResponseEntity<?> deleteGuide(@PathVariable Long id) {
        guideRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Guide deleted successfully"));
    }
}
