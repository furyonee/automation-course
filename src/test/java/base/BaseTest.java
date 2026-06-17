package base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class BaseTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    public Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
        );

        page = context.newPage();
    }

    @Test
    void testCartActions() {
        Locator cart = page.locator("#elements");

        page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");
        page.click("button:text('Add Element')");
        cart.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        cart.screenshot(new Locator.ScreenshotOptions()
                .setPath(getTimestampPath("cart_after_add.png")));
        page.click("button:text('Delete')");

        page.screenshot(new Page.ScreenshotOptions().setPath(getTimestampPath("cart_after_action.png"))
        );
    }

    private Path getTimestampPath(String filename) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        return Paths.get("artifacts", timestamp, filename);
    }

    @AfterEach
    void teardown() {
        context.close();
    }
}