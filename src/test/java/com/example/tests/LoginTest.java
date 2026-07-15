package com.example.tests;

import com.example.helper.AuthHelper;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void createPage() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
        context = browser.newContext();
        page = context.newPage();
    }

    @ParameterizedTest
    @CsvSource({
            "tomsmith, SuperSecretPassword!, true",
            "tomsmith, wrongpassword, false"
    })
    void loginTest(String username, String password, boolean success) {
        AuthHelper.login(page, username, password);

        if (success) {
            assertTrue(page.locator(".flash.success").isVisible());
        } else {
            assertTrue(page.locator(".flash.error").isVisible());
        }
    }

    @AfterEach
    void closePage() {
        context.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }
}