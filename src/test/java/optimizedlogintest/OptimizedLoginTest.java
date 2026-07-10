package optimizedlogintest;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OptimizedLoginTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    static List<Cookie> authCookies;

    @BeforeAll
    static void setUpClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        BrowserContext loginContext = browser.newContext();
        Page loginPage = loginContext.newPage();
        authCookies = performLogin(loginPage);
        loginContext.close();
    }


    @BeforeEach
    void setUp() {
        context = browser.newContext();
        context.addCookies(authCookies);
        page = context.newPage();
        page.setDefaultTimeout(5000);
    }


    @Test
    void testSecureArea() {
        page.navigate("https://the-internet.herokuapp.com/secure");

        assertTrue(
                page.locator("h2")
                        .textContent()
                        .contains("Secure Area")
        );
    }


    private static List<Cookie> performLogin(Page page) {
        page.navigate("https://the-internet.herokuapp.com/login");
        page.locator("#username").fill("tomsmith");
        page.locator("#password").fill("SuperSecretPassword!");
        page.locator("button[type='submit']").click();
        page.waitForURL("**/secure");

        return page.context().cookies();
    }


    @AfterEach
    void tearDown() {
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
    }


    @AfterAll
    static void tearDownClass() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}