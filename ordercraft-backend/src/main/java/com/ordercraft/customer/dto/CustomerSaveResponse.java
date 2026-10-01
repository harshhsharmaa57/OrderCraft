package com.ordercraft.customer.dto;

import java.util.List;

/** Returned by create/update: the saved customer plus any duplicate warnings. */
public record CustomerSaveResponse(CustomerResponse customer, List<String> warnings) {
}