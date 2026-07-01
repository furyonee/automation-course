package advancedreportingtest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Тесты для the-internet.herokuapp.com")
@Feature("Работа с JavaScript-алертами")
public class AdvancedReportingTest {
    private static ExtentReports extent;

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private ExtentTest test;

    @BeforeAll
    static void setupExtent() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("target/extent-report.html");

        reporter.config().setDocumentTitle("Playwright Extent Report");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();

        test = extent.createTest(testInfo.getDisplayName());
        logExtent(Status.INFO, "Начало теста");
    }

    @Test
    @Story("Проверка JS Alert")
    @Description("Тест взаимодействия с JS Alert и проверка результата")
    @Severity(SeverityLevel.NORMAL)
    void testJavaScriptAlert() throws Exception {
        try {
            navigateToAlertsPage();
            String alertMessage = foJsAlert();
            verifyResultText();
            captureSuccessScreenshot();
            logExtent(Status.PASS, "Тест успешно завершен. Alert: " + alertMessage);
        } catch (Exception e) {
            foTestFailure(e);
            throw e;
        }
    }

    @Step("Открыть страницу с алертами")
    private void navigateToAlertsPage() {
        page.navigate("https://the-internet.herokuapp.com/javascript_alerts");
        assertEquals("JavaScript Alerts", page.locator("h3").textContent());

        logExtent(Status.INFO, "Страница открыта");
    }

    @Step("Обработать JS Alert")
    private String foJsAlert() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        page.onDialog(dialog -> {
            future.complete(dialog.message());
            dialog.accept();
        });
        page.click("button[onclick='jsAlert()']");

        logExtent(Status.INFO, "Нажата кнопка JS Alert");

        return future.get(5, TimeUnit.SECONDS);
    }

    @Step("Проверить текст результата")
    private void verifyResultText() {
        page.waitForCondition(() -> page.locator("#result").textContent().contains("successfully"));
        String resultText = page.locator("#result").textContent();
        assertEquals("You successfully clicked an alert", resultText);

        logExtent(Status.INFO, "Результат проверен");
    }

    private void captureSuccessScreenshot() throws Exception {
        String screenshotName = "success-screenshot.png";
        Path screenshotPath = Paths.get("target", screenshotName);
        page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));
        byte[] screenshot = page.screenshot();
        try (InputStream stream = new ByteArrayInputStream(screenshot)) {
            Allure.addAttachment("Success Screenshot", "image/png", stream, ".png");
        }

        test.pass("Скриншот успешного выполнения", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath.toString()).build());
    }

    private void logExtent(Status status, String message) {
        test.log(status, message);
    }

    private void foTestFailure(Exception e) {
        byte[] failureScreenshot = page.screenshot();
        try (InputStream stream = new ByteArrayInputStream(failureScreenshot)) {
            Allure.addAttachment("Ошибка теста", "image/png", stream, ".png");
        } catch (Exception ignored) {
        }
        test.fail(e);
    }

    @AfterEach
    void tearDownEach() {
        context.close();
        browser.close();
        playwright.close();
    }

    @AfterAll
    static void tearDown() {
        extent.flush();
    }
}