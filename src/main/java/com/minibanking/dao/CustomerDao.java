package com.minibanking.dao;

import com.minibanking.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    void save(Customer customer);
    Optional<Customer> findById(Long id);
    List<Customer> findAll(); // Add this line
}

