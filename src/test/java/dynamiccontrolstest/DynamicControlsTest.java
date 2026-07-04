package dynamiccontrolstest;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicControlsTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
        page = browser.newPage();
    }

    @Test
    void testDynamicCheckbox() {
        page.navigate("https://the-internet.herokuapp.com/dynamic_controls");

        Locator checkbox = page.locator("input[type='checkbox']");

        assertTrue(checkbox.isVisible());

        page.click("button:text('Remove')");
        checkbox.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.DETACHED));

        assertEquals("It's gone!", page.locator("#message").textContent());

        page.click("button:text('Add')");
        checkbox.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        assertTrue(checkbox.isVisible());
    }

    @AfterEach
    void tearDown() {
        page.close();
        browser.close();
        playwright.close();
    }
}