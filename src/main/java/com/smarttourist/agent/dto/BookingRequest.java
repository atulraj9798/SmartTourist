package com.smarttourist.agent.dto;

public record BookingRequest(
    String startLocation,
    Long destinationId,
    String startDate,
    String endDate,
    Integer numAdults,
    Integer numChildren,
    Integer numSeniors,
    String specialRequirements,
    Long vehicleId,
    Long roomId,
    Long guideId
) {}
