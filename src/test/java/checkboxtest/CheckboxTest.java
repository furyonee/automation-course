package checkboxtest;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Веб-интерфейс тестов")
@Feature("Операции с чекбоксами")
public class CheckboxTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    @Step("Инициализация браузера")
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    @Story("Проверка чекбоксов")
    @DisplayName("Проверка страницы Checkboxes")
    @Severity(SeverityLevel.CRITICAL)
    void testCheckboxes() {
        openPage();
        checkInitialState();
        toggleCheckboxes();
        checkFinalState();
    }

    @Step("Переход на страницу Checkboxes")
    void openPage() {
        page.navigate("https://the-internet.herokuapp.com/checkboxes");
    }

    @Step("Проверка начального состояния")
    void checkInitialState() {
        assertEquals(2, page.locator("input").count());
    }

    @Step("Переключение чекбоксов")
    void toggleCheckboxes() {
        page.locator("input").nth(0).click();
        page.locator("input").nth(1).click();
    }

    @Step("Проверка состояния после изменения")
    void checkFinalState() {
        assertTrue(page.locator("input").nth(0).isChecked());
        assertFalse(page.locator("input").nth(1).isChecked());
    }

    @AfterEach
    @Step("Закрытие браузера")
    void tearDown(TestInfo testInfo) {
        byte[] screenshot = page.screenshot();
        Allure.addAttachment(
                "Screenshot",
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png"
        );

        context.close();
        browser.close();
        playwright.close();
    }
}