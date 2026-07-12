package com.example.context;

import com.microsoft.playwright.*;

public class TestContext {
    private final Playwright playwright;
    private final Browser browser;

    public TestContext() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
        );
    }

    public Browser getBrowser() {
        return browser;
    }

    public void close() {
        browser.close();
        playwright.close();
    }
}