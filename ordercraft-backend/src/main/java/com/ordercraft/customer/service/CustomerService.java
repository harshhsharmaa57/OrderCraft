package com.ordercraft.customer.service;

import com.ordercraft.common.exception.EntityNotFoundException;
import com.ordercraft.common.util.SecurityUtils;
import com.ordercraft.customer.dto.*;
import com.ordercraft.customer.entity.Customer;
import com.ordercraft.customer.mapper.CustomerMapper;
import com.ordercraft.customer.repository.CustomerRepository;
import com.ordercraft.customer.repository.CustomerSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private static final int QUICK_SEARCH_LIMIT = 10;
    private static final String DEFAULT_COUNTRY = "India";

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> listCustomers(String q, Boolean active, Pageable pageable) {
        return customerRepository.findAll(CustomerSpecifications.filter(q, active), pageable)
                .map(CustomerMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<CustomerSummary> quickSearch(String q) {
        Pageable top = PageRequest.of(0, QUICK_SEARCH_LIMIT, Sort.by("name"));
        return customerRepository.findAll(CustomerSpecifications.quickSearch(q), top)
                .map(CustomerMapper::toSummary)
                .getContent();
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(Long id) {
        return CustomerMapper.toResponse(findCustomer(id));
    }

    @Transactional
    public CustomerSaveResponse createCustomer(CustomerRequest request) {
        String email = normalizeEmail(request.email());
        String phone = blankToNull(request.phone());

        List<String> warnings = new ArrayList<>();
        if (email != null && customerRepository.existsByEmailIgnoreCase(email)) {
            warnings.add("Another customer already uses this email address");
        }
        if (phone != null && customerRepository.existsByPhone(phone)) {
            warnings.add("Another customer already uses this phone number");
        }

        Customer customer = Customer.builder()
                .customerCode(generateCustomerCode())
                .isActive(true)
                .build();
        applyRequest(customer, request, email, phone);
        customer.setCreatedBy(SecurityUtils.getCurrentUserId());

        Customer saved = customerRepository.save(customer);
        return new CustomerSaveResponse(CustomerMapper.toResponse(saved), warnings);
    }

    @Transactional
    public CustomerSaveResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = findCustomer(id);
        String email = normalizeEmail(request.email());
        String phone = blankToNull(request.phone());

        List<String> warnings = new ArrayList<>();
        if (email != null && customerRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            warnings.add("Another customer already uses this email address");
        }
        if (phone != null && customerRepository.existsByPhoneAndIdNot(phone, id)) {
            warnings.add("Another customer already uses this phone number");
        }

        applyRequest(customer, request, email, phone);
        customer.setUpdatedBy(SecurityUtils.getCurrentUserId());

        Customer saved = customerRepository.save(customer);
        return new CustomerSaveResponse(CustomerMapper.toResponse(saved), warnings);
    }

    @Transactional
    public CustomerResponse updateStatus(Long id, boolean active) {
        Customer customer = findCustomer(id);
        customer.setIsActive(active);
        customer.setUpdatedBy(SecurityUtils.getCurrentUserId());
        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    /**
     * Sales orders arrive in Module 7. Until then this validates the customer
     * exists and returns an empty page; replace the body when SalesOrder exists.
     */
    @Transactional(readOnly = true)
    public Page<Object> getCustomerOrders(Long id, Pageable pageable) {
        findCustomer(id);
        return Page.empty(pageable);
    }

    // ---------- helpers ----------

    private Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer", id));
    }

    private void applyRequest(Customer c, CustomerRequest r, String email, String phone) {
        c.setName(r.name().trim());
        c.setEmail(email);
        c.setPhone(phone);
        c.setAddressLine1(blankToNull(r.addressLine1()));
        c.setAddressLine2(blankToNull(r.addressLine2()));
        c.setCity(blankToNull(r.city()));
        c.setState(blankToNull(r.state()));
        c.setPostalCode(blankToNull(r.postalCode()));
        String country = blankToNull(r.country());
        c.setCountry(country != null ? country : DEFAULT_COUNTRY);
    }

    private String generateCustomerCode() {
        long next = customerRepository.nextCustomerCodeNumber().longValue();
        return String.format("CUST-%04d", next);
    }

    private String normalizeEmail(String email) {
        String value = blankToNull(email);
        return value == null ? null : value.toLowerCase();
    }

    private String blankToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}