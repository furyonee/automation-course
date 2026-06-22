package testhomepagevisual;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHomePageVisual {
    static Playwright playwright;
    static Browser browser;

    BrowserContext context;
    Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void testHomePageVisual() throws IOException {
        page.navigate("https://the-internet.herokuapp.com");

        Path actual = Paths.get("actual.png");
        Path expected = Paths.get("expected.png");

        page.screenshot(new Page.ScreenshotOptions()
                .setPath(actual));

        long mismatch = Files.mismatch(actual, expected);

        if (mismatch != -1) {
            Files.copy(actual, Paths.get("diff.png"));
        }

        assertEquals(-1, mismatch);
    }

    @AfterAll
    static void teardownAll() {
        browser.close();
        playwright.close();
    }
}
