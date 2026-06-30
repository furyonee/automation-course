package parallelnavigationtest;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Execution(ExecutionMode.CONCURRENT)
public class ParallelNavigationTest {

    @ParameterizedTest
    @CsvSource({
            "chromium, /",
            "chromium, /login",
            "chromium, /dropdown",
            "firefox, /",
            "firefox, /login",
            "firefox, /dropdown"
    })
    void testPageLoad(String browserName, String path) {

        Playwright playwright = Playwright.create();

        Browser browser;

        if (browserName.equals("firefox")) {
            browser = playwright.firefox().launch();
        } else {
            browser = playwright.chromium().launch();
        }

        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.navigate("https://the-internet.herokuapp.com" + path);

        assertFalse(page.title().isEmpty());

        context.close();
        browser.close();
        playwright.close();
    }
}