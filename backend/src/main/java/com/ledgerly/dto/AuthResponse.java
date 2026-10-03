package com.ledgerly.dto;

public record AuthResponse(String token, UserResponse user) {
}