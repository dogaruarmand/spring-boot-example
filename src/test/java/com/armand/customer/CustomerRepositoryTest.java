package com.armand.customer;

import com.armand.AbstractTestContainers;
import org.jetbrains.annotations.NotNull;
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
    private CustomerRepository underTest;

    private record Result(String email, Customer customer) {
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        Result result = getCustomer();

        // When
        boolean actual = underTest.existsCustomerByEmail(result.email());

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerById() {
        // Given
        Result result = getCustomer();

        Integer id =
                underTest.findAll().stream()
                        .filter(c -> c.getEmail().equals(result.email()))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        // When
        Optional<Customer> actual = underTest.findById(id);

        // Then
        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(
                        c -> {
                            assertThat(c.getId()).isEqualTo(id);
                            assertThat(c.getName()).isEqualTo(result.customer().getName());
                            assertThat(c.getEmail()).isEqualTo(result.customer().getEmail());
                            assertThat(c.getAge()).isEqualTo(result.customer().getAge());
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

    private @NotNull Result getCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.save(customer);
        return new Result(email, customer);
    }
}
