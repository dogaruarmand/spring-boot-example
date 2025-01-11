package com.armand.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

  @Autowired private CustomerRepository customerRepository;

  @Autowired private ApplicationContext applicationContext;

  @BeforeEach
  void setUp() {
    System.out.println(applicationContext.getBeanDefinitionCount());
  }

  @Test
  void existsCustomerByEmail() {}

  @Test
  void existsCustomerById() {}
}
