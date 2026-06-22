package carttest;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CartTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    public Page page;
    private boolean testFailed = false;

    @BeforeAll
    static void setupAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
        );

        page = context.newPage();
    }

    @Test
    void testCartActions() {
        try {
            page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");

            page.click("text=Add Element");
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(getTimestampPath("cart_after_add.png")));

            page.click("text=Delete");
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(getTimestampPath("cart_after_remove.png")));

        } catch (Exception e) {
            testFailed = true;
            throw e;
        }
    }

    private Path getTimestampPath(String filename) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        return Paths.get("artifacts", timestamp, filename);
    }

    @AfterEach
    void attachScreenshotOnFailure() {
        if (testFailed) {
            byte[] screenshot = page.screenshot();

            Allure.addAttachment(
                    "Screenshot on Failure",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
        }

        context.close();
    }
}