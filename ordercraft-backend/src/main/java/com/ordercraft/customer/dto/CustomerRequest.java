package com.ordercraft.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
        @NotBlank(message = "Customer name is required")
        @Size(max = 150, message = "Name must be at most 150 characters")
        String name,

        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must be at most 100 characters")
        String email,

        @Pattern(regexp = "^$|^[0-9+()\\-\\s]{7,20}$",
                message = "Phone must be 7-20 characters (digits, +, -, spaces, brackets)")
        String phone,

        @Size(max = 255, message = "Address line 1 must be at most 255 characters")
        String addressLine1,

        @Size(max = 255, message = "Address line 2 must be at most 255 characters")
        String addressLine2,

        @Size(max = 100, message = "City must be at most 100 characters")
        String city,

        @Size(max = 100, message = "State must be at most 100 characters")
        String state,

        @Size(max = 20, message = "Postal code must be at most 20 characters")
        String postalCode,

        @Size(max = 100, message = "Country must be at most 100 characters")
        String country) {
}