package com.example;

import com.example.config.EnvironmentConfig;
import com.microsoft.playwright.*;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatusCodeTest {
    private EnvironmentConfig config;
    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeEach
    void setup() {
        config = ConfigFactory.create(EnvironmentConfig.class, System.getenv());
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @Test
    void testStatusCode200() {
        page.navigate(config.baseUrl() + "/status_codes/200");

        assertTrue(page.url().contains("/status_codes/200"));
    }

    @AfterEach
    void tearDown() {
        page.close();
        browser.close();
        playwright.close();
    }
}