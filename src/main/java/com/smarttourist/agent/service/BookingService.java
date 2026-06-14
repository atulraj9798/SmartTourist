package com.smarttourist.agent.service;

import com.smarttourist.agent.dto.BookingRequest;
import com.smarttourist.agent.dto.BudgetRequest;
import com.smarttourist.agent.dto.BudgetResponse;
import com.smarttourist.agent.dto.PaymentRequest;
import com.smarttourist.agent.model.*;
import com.smarttourist.agent.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final PaymentRepository paymentRepository;
    private final DestinationRepository destinationRepository;
    private final VehicleRepository vehicleRepository;
    private final RoomRepository roomRepository;
    private final GuideRepository guideRepository;
    private final TourService tourService;

    public BookingService(
            BookingRepository bookingRepository,
            TourRepository tourRepository,
            PaymentRepository paymentRepository,
            DestinationRepository destinationRepository,
            VehicleRepository vehicleRepository,
            RoomRepository roomRepository,
            GuideRepository guideRepository,
            TourService tourService
    ) {
        this.bookingRepository = bookingRepository;
        this.tourRepository = tourRepository;
        this.paymentRepository = paymentRepository;
        this.destinationRepository = destinationRepository;
        this.vehicleRepository = vehicleRepository;
        this.roomRepository = roomRepository;
        this.guideRepository = guideRepository;
        this.tourService = tourService;
    }

    @Transactional
    public Booking createBooking(BookingRequest request, User user) {
        // Find destination
        Destination destination = destinationRepository.findById(request.destinationId())
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

        LocalDate start = LocalDate.parse(request.startDate());
        LocalDate end = LocalDate.parse(request.endDate());
        int days = (int) ChronoUnit.DAYS.between(start, end);
        if (days <= 0) days = 1;

        // Calculate dynamic cost
        BudgetRequest budgetRequest = new BudgetRequest(
                request.startLocation(),
                request.destinationId(),
                request.startDate(),
                request.endDate(),
                request.numAdults(),
                request.numChildren(),
                request.numSeniors(),
                request.vehicleId(),
                request.roomId(),
                request.guideId()
        );
        BudgetResponse budget = tourService.calculateBudget(budgetRequest);

        // Create Tour
        Tour tour = new Tour();
        tour.setUser(user);
        tour.setStartLocation(request.startLocation());
        tour.setDestination(destination);
        tour.setStartDate(start);
        tour.setEndDate(end);
        tour.setNumberOfDays(days);
        tour.setNumAdults(request.numAdults());
        tour.setNumChildren(request.numChildren());
        tour.setNumSeniors(request.numSeniors());
        tour.setTotalTravelers(request.numAdults() + request.numChildren() + request.numSeniors());
        tour.setSpecialRequirements(request.specialRequirements());
        tour.setTotalCost(budget.totalCost());
        tour.setStatus("DRAFT");
        Tour savedTour = tourRepository.save(tour);

        // Resolve details
        Vehicle vehicle = request.vehicleId() != null ? vehicleRepository.findById(request.vehicleId()).orElse(null) : null;
        Room room = request.roomId() != null ? roomRepository.findById(request.roomId()).orElse(null) : null;
        Guide guide = request.guideId() != null ? guideRepository.findById(request.guideId()).orElse(null) : null;

        // Create Booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTour(savedTour);
        booking.setVehicle(vehicle);
        booking.setRoom(room);
        booking.setGuide(guide);
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalAmount(budget.totalCost());
        booking.setStatus("PENDING");

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking processPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!"PENDING".equals(booking.getStatus())) {
            throw new IllegalStateException("Booking is already processed or cancelled");
        }

        // Generate custom transaction ID if not provided
        String transactionId = request.transactionId() != null && !request.transactionId().isEmpty()
                ? request.transactionId()
                : "TXN-" + UUID.randomUUID().toString().substring(0, 13).toUpperCase();

        // Save Payment details
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTransactionId(transactionId);
        payment.setPaymentMethod(request.paymentMethod());
        payment.setAmount(booking.getTotalAmount());
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // Update Booking Status
        booking.setStatus("CONFIRMED");
        Booking savedBooking = bookingRepository.save(booking);

        // Update linked Tour Status
        Tour tour = booking.getTour();
        tour.setStatus("BOOKED");
        tourRepository.save(tour);

        // Simulate Notification Dispatching
        System.out.println("=================================================");
        System.out.println("NOTIFYING EMAIL & SMS: Booking CONFIRMED");
        System.out.println("Recipient: " + booking.getUser().getEmail());
        System.out.println("Booking Ref: #" + booking.getId());
        System.out.println("Total Paid: INR " + booking.getTotalAmount());
        System.out.println("=================================================");

        return savedBooking;
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
    }
}
