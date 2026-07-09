package statuscodecombinedtest;

import com.microsoft.playwright.*;
import config.EnvConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusCodeCombinedTest {
    private Playwright playwright;
    private APIRequestContext apiRequest;
    private Browser browser;
    private Page page;
    private static EnvConfig config;

    @BeforeAll
    static void loadConfig() {
        config = ConfigFactory.create(EnvConfig.class, System.getProperties());
    }

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        apiRequest = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(config.baseUrl())
        );
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setSlowMo(100)
        );
        page = browser.newPage();
        page.setDefaultTimeout(40000);
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 404})
    void testStatusCodeCombined(int statusCode) {
        int apiStatus = getApiStatusCode(statusCode);
        int uiStatus = getUiStatusCode(statusCode);

        assertEquals(apiStatus, uiStatus);
    }

    private int getApiStatusCode(int code) {
        APIResponse response = apiRequest.get("/status_codes/" + code);
        assertEquals(code, response.status(),
                "API: Неверный статус код для " + code);
        return response.status();
    }

    private int getUiStatusCode(int code) {
        try {
            page.navigate(config.baseUrl() + "/status_codes");
            page.waitForSelector("div.example");
            Locator link = page.locator(
                    String.format("a[href*='status_codes/%d']", code)
            ).first();

            Response response = page.waitForResponse(
                    res -> res.url().endsWith("/status_codes/" + code),
                    () -> link.click(new Locator.ClickOptions().setTimeout(10000))
            );

            return response.status();

        } catch (Exception e) {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("error-" + code + ".png")));

            throw new RuntimeException("UI проверка упала для кода " + code, e);
        }
    }

    @AfterEach
    void teardown() {
        page.close();
        browser.close();
        apiRequest.dispose();
        playwright.close();
    }
}