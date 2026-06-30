package paralleltests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
public class ParallelTests {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();

        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void testLoginPage() {
        page.navigate("https://the-internet.herokuapp.com/login");

        assertEquals("Login Page", page.locator("h2").textContent());
    }

    @Test
    void testAddRemoveElements() {
        page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");

        page.click("text=Add Element");

        assertTrue(page.locator("button.added-manually").isVisible());
    }

    @AfterEach
    void teardown() {
        context.close();
        browser.close();
        playwright.close();
    }
}