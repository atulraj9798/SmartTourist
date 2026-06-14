package com.smarttourist.agent.controller;

import com.smarttourist.agent.model.Booking;
import com.smarttourist.agent.model.User;
import com.smarttourist.agent.service.AuthService;
import com.smarttourist.agent.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;
    private final BookingService bookingService;

    public UserController(AuthService authService, BookingService bookingService) {
        this.authService = authService;
        this.bookingService = bookingService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authService.getUserByEmail(authentication.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("mobileNumber", user.getMobileNumber());
        response.put("role", user.getRole());
        response.put("createdAt", user.getCreatedAt());

        if (user.getGovernmentIdDetail() != null) {
            Map<String, Object> idDetails = new HashMap<>();
            idDetails.put("idType", user.getGovernmentIdDetail().getIdType());
            idDetails.put("idNumber", user.getGovernmentIdDetail().getIdNumber());
            idDetails.put("isVerified", user.getGovernmentIdDetail().getIsVerified());
            response.put("governmentId", idDetails);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getUserDashboardStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authService.getUserByEmail(authentication.getName());

        List<Booking> bookings = bookingService.getBookingsByUserId(user.getId());

        long totalBookings = bookings.size();
        long activeBookings = bookings.stream().filter(b -> "CONFIRMED".equals(b.getStatus())).count();
        long pendingBookings = bookings.stream().filter(b -> "PENDING".equals(b.getStatus())).count();
        double totalSpent = bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .mapToDouble(Booking::getTotalAmount)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", totalBookings);
        stats.put("activeBookings", activeBookings);
        stats.put("pendingBookings", pendingBookings);
        stats.put("totalSpent", Math.round(totalSpent * 100.0) / 100.0);
        stats.put("isGovIdVerified", user.getGovernmentIdDetail() != null && user.getGovernmentIdDetail().getIsVerified());

        return ResponseEntity.ok(stats);
    }
}
