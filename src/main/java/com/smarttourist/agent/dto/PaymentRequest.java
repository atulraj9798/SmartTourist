package com.smarttourist.agent.dto;

public record PaymentRequest(
    Long bookingId,
    String paymentMethod,
    String transactionId
) {}
