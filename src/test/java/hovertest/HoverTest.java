package hovertest;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HoverTest {
    static Playwright playwright;
    static Browser browser;

    BrowserContext context;
    Page page;

    @BeforeAll
    static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void testHoverProfiles() {
        page.navigate(
                "https://the-internet.herokuapp.com/hovers",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

        Locator figures = page.locator(".figure");
        int count = figures.count();

        for (int i = 0; i < count; i++) {
            Locator figure = figures.nth(i);
            figure.hover();

            Locator profileLink = figure.locator("text=View profile");
            assertTrue(profileLink.isVisible());

            profileLink.click();

            assertTrue(page.url().contains("/users/"));

            page.goBack();
        }
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @AfterAll
    static void teardownClass() {
        browser.close();
        playwright.close();
    }
}