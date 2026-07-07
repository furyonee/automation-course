package context;

import com.microsoft.playwright.*;

public class TestContext {
    private final Playwright playwright;
    private final Browser browser;
    private final Page page;

    public TestContext() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
        page = browser.newPage();
    }

    public Page getPage() {
        return page;
    }

    public void close() {
        page.close();
        browser.close();
        playwright.close();
    }
}