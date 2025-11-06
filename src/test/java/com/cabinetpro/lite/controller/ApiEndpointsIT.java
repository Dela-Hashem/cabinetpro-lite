package com.cabinetpro.lite.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestPropertySource(properties = {
        "logging.level.com.zaxxer.hikari=ERROR",
        "logging.level.org.springframework.jdbc=ERROR"
})
@Sql(scripts = "/db/init.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ApiEndpointsIT {

    // Postgres 16 سبک و سریع
    @Container
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("cabinetpro")
            .withUsername("cabinetuser")
            .withPassword("cabinetpass");

    // تزریق تنظیمات دیتابیس کانتینر به Spring
    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);
        r.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Autowired
    MockMvc mvc;

    String json(Object s) { return s.toString(); } // برای خوانایی تست‌ها

    @BeforeEach
    void quiet() { /* محل مناسب برای هر setup اضافی */ }

    @Test
    void createCustomer_withProject_thenListProjects() throws Exception {
        String body = """
          {
            "customer": { "fullName": "Maryam A.", "phone": "0400 555 123", "email": "maryam@example.com" },
            "project":  { "title": "Laundry fitout", "address": "Nollamara WA" }
          }
        """;

        MvcResult res = mvc.perform(post("/api/customers/with-project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        // customerId از بدنه‌ی پاسخ
        long customerId = Long.parseLong(res.getResponse().getContentAsString());

        // باید پروژه ثبت شده باشد
        mvc.perform(get("/api/projects/by-customer/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Laundry fitout")));
    }

    @Test
    void createCustomer_thenSearchByName() throws Exception {
        // ساخت دو مشتری
        mvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "fullName":"Zac H.","phone":"0400 111 222","email":"zac@example.com" }
                """)).andExpect(status().isCreated());

        mvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "fullName":"Mary Graham","phone":"0400 333 444","email":"mg@example.com" }
                """)).andExpect(status().isCreated());

        // جستجو
        mvc.perform(get("/api/customers/search").param("q", "mary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].fullName", hasItem("Mary Graham")));
    }

    @Test
    void badJson_returns400() throws Exception {
        // JSON خراب
        mvc.perform(post("/api/customers/with-project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"customer\": /*oops*/ }"))
                .andExpect(status().isBadRequest()); // با هندلری که قبلاً دادی
    }
}
