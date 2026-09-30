package com.ordercraft.customer.dto;

/** Lightweight shape for dropdowns / quick search. */
public record CustomerSummary(Long id, String customerCode, String name) {
}