package com.armand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TestcontainersTest extends AbstractTestcontainers {

  @Test
  void canStartPostrgesDB() {
    assertThat(postgreSQLContainer.isRunning()).isTrue();
    assertThat(postgreSQLContainer.isCreated()).isTrue();
  }
}
