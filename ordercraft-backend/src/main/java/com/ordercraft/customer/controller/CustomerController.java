package com.ordercraft.customer.controller;

import com.ordercraft.common.dto.ApiResponse;
import com.ordercraft.common.dto.PagedResponse;
import com.ordercraft.customer.dto.*;
import com.ordercraft.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GET   /api/customers               — list (paginated; ?q=&active=)
 * GET   /api/customers/search?q=     — quick search (active only, max 10)
 * POST  /api/customers               — create
 * GET   /api/customers/{id}          — get by id
 * PUT   /api/customers/{id}          — update
 * PATCH /api/customers/{id}/status   — activate / deactivate
 * GET   /api/customers/{id}/orders   — sales orders for the customer
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW')")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> listCustomers(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.of(customerService.listCustomers(q, active, pageable)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW')")
    public ResponseEntity<ApiResponse<List<CustomerSummary>>> quickSearch(
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(ApiResponse.success(customerService.quickSearch(q)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE')")
    public ResponseEntity<ApiResponse<CustomerSaveResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(customerService.createCustomer(request),
                        "Customer created successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW')")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomer(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE')")
    public ResponseEntity<ApiResponse<CustomerSaveResponse>> updateCustomer(
            @PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(customerService.updateCustomer(id, request),
                "Customer updated successfully"));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE')")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateStatus(
            @PathVariable Long id, @Valid @RequestBody CustomerStatusRequest request) {
        String message = request.active() ? "Customer activated successfully"
                : "Customer deactivated successfully";
        return ResponseEntity.ok(ApiResponse.success(
                customerService.updateStatus(id, request.active()), message));
    }

    @GetMapping("/{id}/orders")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW')")
    public ResponseEntity<ApiResponse<List<Object>>> getCustomerOrders(
            @PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.of(customerService.getCustomerOrders(id, pageable)));
    }
}