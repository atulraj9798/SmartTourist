package com.smarttourist.agent.dto;

public record AuthResponse(
    String token,
    String email,
    String name,
    String role
) {}
