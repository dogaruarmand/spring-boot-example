package com.armand.customer;

import com.armand.exception.DuplicateResourceException;
import com.armand.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
  private final CustomerDao customerDao;

  public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  public List<Customer> getAllCustomers() {
    return customerDao.selectAllCustomers();
  }

  public Customer getCustomer(Integer id) {
    return customerDao
        .selectCustomerById(id)
        .orElseThrow(() -> new ResourceNotFoundException("customer with id [%s] not found".formatted(id)));
  }

  public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
    //check if email exists
    String email = customerRegistrationRequest.email();
    if (customerDao.existsPersonWithEmail(email)) {
      throw new DuplicateResourceException("Email already taken");
    }
    // add
    Customer customer = new Customer(
            customerRegistrationRequest.name(),
            customerRegistrationRequest.email(),
            customerRegistrationRequest.age()
    );
    customerDao.insertCustomer(customer);
  }

  public void deleteCustomerById(Integer customerId) {
    if (!customerDao.existsPersonWithId(customerId)) {
      throw new ResourceNotFoundException(
              "customer with id [%s] not found".formatted(customerId)
      );
    }

    customerDao.deleteCustomerById(customerId);
  }
}
