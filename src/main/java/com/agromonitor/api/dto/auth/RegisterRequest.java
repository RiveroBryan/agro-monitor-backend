package com.agromonitor.api.dto.auth;

public record RegisterRequest(
    String email, 
    String password, 
    String role // "AGRICULTOR" o "ADMIN"
) {}