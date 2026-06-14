package com.smarttourist.agent.dto;

public record BudgetRequest(
    String startLocation,
    Long destinationId,
    String startDate,
    String endDate,
    Integer numAdults,
    Integer numChildren,
    Integer numSeniors,
    Long vehicleId,
    Long roomId,
    Long guideId
) {}
