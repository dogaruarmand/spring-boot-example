package com.armand.customer;

import static org.junit.jupiter.api.Assertions.*;

import com.armand.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

  private CustomerJDBCDataAccessService underTest;
  private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

  @BeforeEach
  void setUp() {
    underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
  }

  @Test
  void selectAllCustomers() {}

  @Test
  void selectCustomerById() {}

  @Test
  void insertCustomer() {}

  @Test
  void existsPersonWithEmail() {}

  @Test
  void existsPersonWithId() {}

  @Test
  void deleteCustomerById() {}

  @Test
  void updateCustomer() {}
}
