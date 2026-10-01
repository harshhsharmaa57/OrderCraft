package com.ordercraft.customer.repository;

import com.ordercraft.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    /** Next number for the CUST-0001 style customer code. */
    @Query(value = "SELECT oc_customer_code_seq.NEXTVAL FROM dual", nativeQuery = true)
    Number nextCustomerCodeNumber();
}