package fakergenertest;

import com.github.javafaker.Faker;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FakerGenerTest {
    @Test
    void testDynamicContent() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );

            Page page = browser.newPage();
            Faker faker = new Faker();
            String userName = faker.name().fullName();

            page.route("**/dynamic_content", route -> {
                route.fulfill(new Route.FulfillOptions()
                        .setStatus(200)
                        .setContentType("text/html")
                        .setBody("<html><body><h1>" + userName + "</h1></body></html>"));
            });
            page.navigate("https://the-internet.herokuapp.com/dynamic_content");

            assertTrue(page.content().contains(userName));

            browser.close();
        }
    }
}