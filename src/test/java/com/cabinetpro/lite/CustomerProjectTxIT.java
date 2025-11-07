package com.cabinetpro.lite;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class CustomerProjectTxIT {

    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("cabinetpro")
            .withUsername("cabinetuser")
            .withPassword("cabinetpass");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        pg.start();
        r.add("spring.datasource.url", pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);
    }

    @Test
    void contextLoads() { /* حداقل: بالا بیاد کافیست؛ بعداً سناریو اتمیک را اضافه می‌کنیم */ }
}
