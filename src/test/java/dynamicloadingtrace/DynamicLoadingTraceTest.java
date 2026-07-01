package dynamicloadingtrace;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicLoadingTraceTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @Test
    void testDynamicLoadingWithTrace() {

        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true));

        page = context.newPage();

        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        page.click("button");
        page.locator("#finish").waitFor();

        assertEquals("Hello World!", page.locator("#finish").textContent().trim());

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace-dynamic-loading.zip")));
    }

    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}