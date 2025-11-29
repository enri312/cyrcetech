package com.cyrcetech.usecase;

import com.cyrcetech.entity.Customer;
import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    List<Customer> getAllCustomers();

    Customer getCustomerById(String id);
}
