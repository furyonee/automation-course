package com.example.tests;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.extension.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CustomReportExtension implements BeforeEachCallback, TestWatcher {
    private static final List<TestResult> results = new ArrayList<>();
    private long startTime;

    @Override
    public void beforeEach(ExtensionContext context) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        results.add(new TestResult(
                context.getDisplayName(),
                "Passed",
                System.currentTimeMillis() - startTime,
                null,
                null
        ));
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        try {
            Files.createDirectories(Paths.get("screenshots"));
            BaseTest test = (BaseTest) context.getRequiredTestInstance();
            String screenshot =
                    "screenshots/" + context.getDisplayName() + ".png";
            test.getPage().screenshot(
                    new Page.ScreenshotOptions()
                            .setPath(Paths.get(screenshot))
            );

            results.add(new TestResult(
                    context.getDisplayName(),
                    "Failed",
                    System.currentTimeMillis() - startTime,
                    cause.getMessage(),
                    screenshot
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<TestResult> getResults() {
        return results;
    }
}