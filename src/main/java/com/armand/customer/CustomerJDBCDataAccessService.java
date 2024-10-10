package com.armand.customer;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

  private final JdbcTemplate jdbcTemplate;

  public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Customer> selectAllCustomers() {
    var sql =
        """
                SELECT id, name, email, age
                FROM customer
              """;

    return jdbcTemplate.query(
        sql,
        (rs, rowNum) ->
            new Customer(
                rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getInt("age")));
  }

  @Override
  public Optional<Customer> selectCustomerById(Integer id) {
    return Optional.empty();
  }

  @Override
  public void insertCustomer(Customer customer) {
    var sql =
        """
                INSERT INTO customer (name, email, age)
                VALUES (?, ?, ?)
              """;
    int result =
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());

    System.out.println("jdbcTemplate.result = " + result);
  }

  @Override
  public boolean existsPersonWithEmail(String email) {
    return false;
  }

  @Override
  public boolean existsPersonWithId(Integer id) {
    return false;
  }

  @Override
  public void deleteCustomerById(Integer customerId) {}

  @Override
  public void updateCustomer(Customer update) {}
}
