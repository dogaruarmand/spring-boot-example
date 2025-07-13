package com.armand;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

public class DockerBasicTest {
    @Test
    void testDockerWorks() {
        try (GenericContainer<?> container = new GenericContainer<>("alpine:latest")
                .withCommand("sleep", "2")) {
            container.start();
            System.out.println("Container running: " + container.isRunning());
        }
    }
}
