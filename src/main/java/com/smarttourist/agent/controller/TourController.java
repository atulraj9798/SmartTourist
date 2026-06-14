package com.smarttourist.agent.controller;

import com.smarttourist.agent.dto.BudgetRequest;
import com.smarttourist.agent.dto.BudgetResponse;
import com.smarttourist.agent.model.Tour;
import com.smarttourist.agent.model.User;
import com.smarttourist.agent.service.AuthService;
import com.smarttourist.agent.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;
    private final AuthService authService;

    public TourController(TourService tourService, AuthService authService) {
        this.tourService = tourService;
        this.authService = authService;
    }

    @PostMapping("/calculate-budget")
    public ResponseEntity<BudgetResponse> calculateBudget(@RequestBody BudgetRequest request) {
        BudgetResponse response = tourService.calculateBudget(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-tours")
    public ResponseEntity<List<Tour>> getMyTours() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authService.getUserByEmail(authentication.getName());
        List<Tour> tours = tourService.getToursByUserId(user.getId());
        return ResponseEntity.ok(tours);
    }
}
