package base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.Collections;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BaseTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    public Page page;

    @BeforeAll
    static void setupAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void simpleInterceptionTest() {
        page.route("**/authenticate", route -> {
            System.out.println("Запрос перехвачен!");
            String postData = route.request().postData();
            System.out.println("Было: " + postData);
            String modifiedBody = postData.replace("tomsmith", "HACKED_USER");
            System.out.println("Стало: " + modifiedBody);
            route.resume(new Route.ResumeOptions().setPostData(modifiedBody));
        });

        page.navigate("https://the-internet.herokuapp.com/login");

        page.fill("#username", "tomsmith");
        page.fill("#password", "SuperSecretPassword!");
        page.click("button[type='submit']");

        assertThat(page.getByText("Your username is invalid!")).isVisible();
    }

    @AfterAll
    static void tearDownAll() {
        browser.close();
        playwright.close();
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

}