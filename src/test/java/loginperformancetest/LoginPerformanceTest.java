package loginperformancetest;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPerformanceTest {

    static Playwright playwright;
    static Browser browser;

    BrowserContext context;
    Page page;

    @BeforeAll
    static void setUpClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
        );
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext();
        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        page = context.newPage();
    }

    @Test
    void testLoginPerformance() {
        long start = System.currentTimeMillis();

        page.navigate("https://the-internet.herokuapp.com/login");
        page.fill("#username", "tomsmith");
        page.fill("#password", "SuperSecretPassword!");
        page.click("button[type='submit']");

        assertTrue(page.locator(".flash.success").isVisible());

        long duration = System.currentTimeMillis() - start;

        Allure.addAttachment("Login time", duration + " ms");

        if (duration > 3000) {
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("slow-login-trace.zip")));
        } else {
            context.tracing().stop();
        }

        assertTrue(duration < 3000, "Login took " + duration + " ms (limit is 3000 ms)");
    }

    @AfterEach
    void tearDown() {
        page.close();
        context.close();
    }

    @AfterAll
    static void tearDownClass() {
        browser.close();
        playwright.close();
    }
}