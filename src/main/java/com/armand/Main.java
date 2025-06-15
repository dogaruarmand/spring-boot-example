package com.armand;

import com.armand.customer.Customer;
import com.armand.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            var faker = new Faker();
            Random random = new Random();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            String fullName = firstName + " " + lastName;
            var email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";
            var age = random.nextInt(16, 99);
            Customer customer = new Customer(
                    fullName, email, age);

            customerRepository.save(customer);
        };
    }
}
