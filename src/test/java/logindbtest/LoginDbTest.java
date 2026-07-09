package logindbtest;

import com.microsoft.playwright.*;
import config.DbConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class LoginDbTest {
    private Connection connection;
    private Playwright playwright;
    private Browser browser;
    private Page page;

    private static DbConfig dbConfig;

    @BeforeAll
    static void loadConfig() {
        dbConfig = ConfigFactory.create(DbConfig.class);
    }

    @BeforeEach
    void setup() throws SQLException {
        connection = DriverManager.getConnection(
                dbConfig.dbUrl(),
                dbConfig.dbUser(),
                dbConfig.dbPassword()
        );

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "INSERT INTO users(username,password) VALUES('test_user','test_pass')"
            );
        }

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
        page = browser.newPage();
    }

    @Test
    void testLoginWithDbUser() throws SQLException {
        String username = "";
        String password = "";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT username,password FROM users WHERE username='test_user'"
             )) {

            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
            }
        }

        assertNotNull(username);
        assertNotNull(password);

        page.navigate("https://the-internet.herokuapp.com/login");

        page.locator("#username").fill(username);
        page.locator("#password").fill(password);
        page.locator("button[type='submit']").click();

        assertTrue(page.locator("#flash").isVisible());
        assertTrue(page.url().contains("/secure"));
    }

    @AfterEach
    void tearDown() throws SQLException {

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "DELETE FROM users WHERE username='test_user'"
            );
        }

        if (connection != null)
            connection.close();

        if (page != null)
            page.close();

        if (browser != null)
            browser.close();

        if (playwright != null)
            playwright.close();
    }
}