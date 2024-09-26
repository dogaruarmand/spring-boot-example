package com.armand;

import com.armand.customer.Customer;
import com.armand.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Bean
  CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
    return args -> {
      var faker = new Faker();
      Name name = faker.name();
      String firstName = name.firstName();
      String lastName = name.lastName();
      String fullName = firstName + " " + lastName;
      var email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";
      Random random = new Random();
      var age = random.nextInt(16, 99);
      Customer customer = new Customer(fullName, email, age);

      List<Customer> customers = List.of(customer);
      customerRepository.save(customer);
    };
  }
}
