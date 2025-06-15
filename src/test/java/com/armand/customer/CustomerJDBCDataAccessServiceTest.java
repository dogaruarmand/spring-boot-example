package com.armand.customer;

import com.armand.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void selectAllCustomers() {
        Customer customer =
                new Customer(
                        FAKER.name().fullName(),
                        FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                        20);
        underTest.insertCustomer(customer);

        List<Customer> customers = underTest.selectAllCustomers();

        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.insertCustomer(customer);

        Integer id =
                underTest.selectAllCustomers().stream()
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
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        int id = -1;

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.insertCustomer(customer);

        Integer id =
                underTest.selectAllCustomers().stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(name, email, 20);
        underTest.insertCustomer(customer);

        // When
        boolean actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.insertCustomer(customer);

        Integer id =
                underTest.selectAllCustomers().stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        // When
        boolean actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdWillReturnFalseWhenIdNotPresent() {
        int id = -1;
        boolean actual = underTest.existsCustomerById(id);
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.insertCustomer(customer);

        Integer id =
                underTest.selectAllCustomers().stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        //When
        underTest.deleteCustomerById(id);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.insertCustomer(customer);

        Integer id =
                underTest.selectAllCustomers().stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        var newName = "foo";

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(
                        c -> {
                            assertThat(c.getId()).isEqualTo(id);
                            assertThat(c.getName()).isEqualTo(newName);
                            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                            assertThat(c.getAge()).isEqualTo(customer.getAge());
                        });

    }

    @Test
    void updateCustomerEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.insertCustomer(customer);

        Integer id =
                underTest.selectAllCustomers().stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(
                        c -> {
                            assertThat(c.getId()).isEqualTo(id);
                            assertThat(c.getName()).isEqualTo(customer.getName());
                            assertThat(c.getEmail()).isEqualTo(newEmail);
                            assertThat(c.getAge()).isEqualTo(customer.getAge());
                        });

    }

    @Test
    void updateCustomerAge() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.insertCustomer(customer);

        Integer id =
                underTest.selectAllCustomers().stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        int newAge = 100;

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(
                        c -> {
                            assertThat(c.getId()).isEqualTo(id);
                            assertThat(c.getName()).isEqualTo(customer.getName());
                            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                            assertThat(c.getAge()).isEqualTo(newAge);
                        });

    }

    @Test
    void updateCustomerAllProperties() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.insertCustomer(customer);

        Integer id =
                underTest.selectAllCustomers().stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        var newName = "foo";
        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        int newAge = 100;

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        update.setEmail(newEmail);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void updateCustomerWithoutPropertiesChange() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20);
        underTest.insertCustomer(customer);

        Integer id =
                underTest.selectAllCustomers().stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        //When
        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

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
}
