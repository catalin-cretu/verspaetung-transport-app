package com.github.catalin.cretu.verspaetung;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;

@AppTestExtension
class AppIT {

    private final App app;

    @Autowired
    AppIT(final App app) {
        this.app = app;
    }

    @Test
    @DisplayName("SpringBoot is configured")
    void main() {
        assertThat(app).isNotNull();
    }
}