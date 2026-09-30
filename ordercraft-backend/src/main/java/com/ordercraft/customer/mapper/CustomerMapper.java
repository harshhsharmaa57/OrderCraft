package com.ordercraft.customer.mapper;

import com.ordercraft.customer.dto.CustomerResponse;
import com.ordercraft.customer.dto.CustomerSummary;
import com.ordercraft.customer.entity.Customer;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(
                c.getId(), c.getCustomerCode(), c.getName(), c.getEmail(), c.getPhone(),
                c.getAddressLine1(), c.getAddressLine2(), c.getCity(), c.getState(),
                c.getPostalCode(), c.getCountry(), c.getIsActive(),
                c.getCreatedAt(), c.getUpdatedAt());
    }

    public static CustomerSummary toSummary(Customer c) {
        return new CustomerSummary(c.getId(), c.getCustomerCode(), c.getName());
    }
}