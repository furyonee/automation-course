package com.example.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CustomReportExtension.class)
public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
        );
    }

    @BeforeEach
    void createPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        if (context != null)
            context.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();

        HtmlReportGenerator.generateReport(
                CustomReportExtension.getResults(),
                "test-report.html"
        );
    }

    public Page getPage() {
        return page;
    }
}