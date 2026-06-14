package com.smarttourist.agent.controller;

import com.smarttourist.agent.dto.BookingRequest;
import com.smarttourist.agent.dto.PaymentRequest;
import com.smarttourist.agent.model.Booking;
import com.smarttourist.agent.model.User;
import com.smarttourist.agent.service.AuthService;
import com.smarttourist.agent.service.BookingService;
import com.smarttourist.agent.service.PdfService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final AuthService authService;
    private final PdfService pdfService;

    public BookingController(BookingService bookingService, AuthService authService, PdfService pdfService) {
        this.bookingService = bookingService;
        this.authService = authService;
        this.pdfService = pdfService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authService.getUserByEmail(authentication.getName());

            Booking booking = bookingService.createBooking(request, user);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<?> payBooking(@RequestBody PaymentRequest request) {
        try {
            Booking booking = bookingService.processPayment(request);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authService.getUserByEmail(authentication.getName());
        List<Booking> bookings = bookingService.getBookingsByUserId(user.getId());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingDetails(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authService.getUserByEmail(authentication.getName());

            // Check authorization: must be owner or admin
            if (!"ADMIN".equals(user.getRole()) && !booking.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body(Map.of("success", false, "message", "Unauthorized access"));
            }

            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<?> downloadInvoice(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authService.getUserByEmail(authentication.getName());

            // Check authorization: must be owner or admin
            if (!"ADMIN".equals(user.getRole()) && !booking.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body(Map.of("success", false, "message", "Unauthorized access"));
            }

            if (!"CONFIRMED".equals(booking.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invoice only available for paid/confirmed bookings"));
            }

            ByteArrayInputStream bis = pdfService.generateInvoicePdf(booking);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=invoice-STA-" + id + ".pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
