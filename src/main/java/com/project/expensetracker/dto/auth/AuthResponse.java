package com.project.expensetracker.dto.auth;

public record AuthResponse(
        String token,
        String email,
        String fullName
) {}