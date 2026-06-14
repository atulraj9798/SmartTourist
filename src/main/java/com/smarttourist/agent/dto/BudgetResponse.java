package com.smarttourist.agent.dto;

public record BudgetResponse(
    Double vehicleCost,
    Double hotelCost,
    Double foodCost,
    Double guideCost,
    Double taxes,
    Double totalCost,
    Integer numberOfDays
) {}
