package com.armand.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @Test
    void selectAllCustomers() {
    }

    @Test
    void selectCustomerById() {
    }

    @Test
    void insertCustomer() {
    }

    @Test
    void existsCustomerByEmail() {
    }

    @Test
    void existsCustomerById() {
    }

    @Test
    void deleteCustomerById() {
    }

    @Test
    void updateCustomer() {
    }
}