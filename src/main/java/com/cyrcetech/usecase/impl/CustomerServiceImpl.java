package com.cyrcetech.usecase.impl;

import com.cyrcetech.entity.Customer;
import com.cyrcetech.infrastructure.CustomerDAO;
import com.cyrcetech.usecase.CustomerService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    public Customer createCustomer(Customer customer) {
        try {
            return customerDAO.save(customer);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating customer", e);
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        try {
            return customerDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Customer getCustomerById(String id) {
        try {
            return customerDAO.findById(id).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        try {
            customerDAO.update(customer);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating customer", e);
        }
    }

    @Override
    public void deleteCustomer(String id) {
        try {
            customerDAO.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting customer", e);
        }
    }
}
