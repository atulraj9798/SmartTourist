package com.smarttourist.agent.dto;

public record RegisterRequest(
    String name,
    String email,
    String mobileNumber,
    String password,
    String idType,
    String idNumber
) {}
