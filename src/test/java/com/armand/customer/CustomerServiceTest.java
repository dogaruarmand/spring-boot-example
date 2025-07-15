package com.armand.customer;

import com.armand.exception.DuplicateResourceException;
import com.armand.exception.RequestValidationException;
import com.armand.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomerById_Success() {
        int id = 1;
        Customer customer = getNewCustomer(id);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void getCustomerById_Not_Found() {
        int id = 1;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer_Success() {
        String email = "test@email.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = getNewCustomerRegistrationRequest(email, "", null);

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void addCustomer_Not_OK() {
        String email = "test@email.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = getNewCustomerRegistrationRequest(email, "", null);

        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById_Success() {
        int id = 1;
        when(customerDao.existsCustomerById(id)).thenReturn(true);

        underTest.deleteCustomerById(id);
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void deleteCustomerById_Not_Found() {
        int id = 1;
        when(customerDao.existsCustomerById(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
        verify(customerDao, never()).deleteCustomerById(any());
    }

    @Test
    void updateAllCustomerProperties_Success() {
        int id = 1;
        Customer customer = getNewCustomer(id);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "testName@newEmail.com";
        CustomerUpdateRequest updateRequest = getNewCustomerUpdateRequest(newEmail, "testName", 23);
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void updateNameCustomerProperties_Success() {
        int id = 1;
        Customer customer = getNewCustomer(id);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest = getNewCustomerUpdateRequest(null, "testName", null);

        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateEmailCustomerProperties_Success() {
        int id = 1;
        Customer customer = getNewCustomer(id);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "testName@newEmail.com";
        CustomerUpdateRequest updateRequest = getNewCustomerUpdateRequest(newEmail, null, null);
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateAgeCustomerProperties_Success() {
        int id = 1;
        Customer customer = getNewCustomer(id);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest = getNewCustomerUpdateRequest(null, null, 23);

        underTest.updateCustomer(id, updateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void updateAllCustomerProperties_EmailAlreadyExist() {
        int id = 1;
        Customer customer = getNewCustomer(id);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "testName@newEmail.com";
        CustomerUpdateRequest updateRequest = getNewCustomerUpdateRequest(newEmail, "testName", 23);
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void updateAllCustomerProperties_NoChanges() {
        int id = 1;
        Customer customer = getNewCustomer(id);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest = getNewCustomerUpdateRequest(customer.getEmail(), customer.getName(), customer.getAge());

        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        verify(customerDao, never()).updateCustomer(any());
    }

    private static CustomerRegistrationRequest getNewCustomerRegistrationRequest(String email, String name,
                                                                                 Integer age) {
        if (StringUtils.isNotBlank(email)) {
            return new CustomerRegistrationRequest("name", email, 2);
        }
        if (StringUtils.isNotBlank(name)) {
            return new CustomerRegistrationRequest(name, "email@email.com", 2);
        }
        if (Objects.nonNull(age)) {
            return new CustomerRegistrationRequest("name", "email@email.com", age);
        }
        return new CustomerRegistrationRequest(null, null, null);
    }

    private static CustomerUpdateRequest getNewCustomerUpdateRequest(String email, String name, Integer age) {
        if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(name) && Objects.nonNull(age) ) {
            return new CustomerUpdateRequest(name, email, age);
        }
        if (StringUtils.isNotBlank(email)) {
            return new CustomerUpdateRequest("name", email, 2);
        }
        if (StringUtils.isNotBlank(name)) {
            return new CustomerUpdateRequest(name, "email@email.com", 2);
        }
        if (Objects.nonNull(age)) {
            return new CustomerUpdateRequest("name", "email@email.com", age);
        }
        return new CustomerUpdateRequest(null, null, null);
    }

    private Customer getNewCustomer(int id) {
        return new Customer(
                id, "Ali", "ali@email.com", 2
        );
    }
}