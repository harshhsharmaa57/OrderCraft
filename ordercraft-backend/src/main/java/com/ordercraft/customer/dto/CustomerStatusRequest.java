package com.ordercraft.customer.dto;

import jakarta.validation.constraints.NotNull;

public record CustomerStatusRequest(
        @NotNull(message = "Active flag is required") Boolean active) {
}