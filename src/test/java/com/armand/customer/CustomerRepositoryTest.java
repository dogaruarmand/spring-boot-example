package com.armand.customer;

import com.armand.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
//    private ApplicationContext applicationContext;

    private CustomerJPADataAccesService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJPADataAccesService(customerRepository);
//        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        customerRepository.save(customer);

        // When
        boolean actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        customerRepository.save(customer);

        Integer id =
                customerRepository.findAll().stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(
                        c -> {
                            assertThat(c.getId()).isEqualTo(id);
                            assertThat(c.getName()).isEqualTo(customer.getName());
                            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                            assertThat(c.getAge()).isEqualTo(customer.getAge());
                        });
    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        // Given
        Integer id = -1;

        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isFalse();
    }
}
