package com.armand;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestcontainers {

  @Container
  protected static final PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:latest")
          .withDatabaseName("postgres-dao-unit-test")
          .withUsername("armand")
          .withPassword("password");

  public static final String PASSWORD = postgreSQLContainer.getPassword();
  public static final String USERNAME = postgreSQLContainer.getUsername();
  public static final String JDBC_URL = postgreSQLContainer.getJdbcUrl();

  //  @Autowired
  //  private ApplicationContext applicationContext;

  @BeforeAll
  static void beforeAll() {
    Flyway flyway = Flyway.configure().dataSource(JDBC_URL, USERNAME, PASSWORD).load();
    flyway.migrate();
    //    System.out.println(applicationContext.getBeanDefinitionCount());
    //    for (String name : applicationContext.getBeanDefinitionNames()) {
    //      System.out.println(name);
    //    }
  }

  @DynamicPropertySource
  private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  private static DataSource getDataSource() {
    return DataSourceBuilder.create()
        .driverClassName(postgreSQLContainer.getDriverClassName())
        .url(JDBC_URL)
        .username(USERNAME)
        .password(PASSWORD)
        .build();
  }

  protected static JdbcTemplate getJdbcTemplate() {
    return new JdbcTemplate(getDataSource());
  }
}
