package com.ordercraft.customer.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String customerCode,
        String name,
        String email,
        String phone,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String postalCode,
        String country,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}