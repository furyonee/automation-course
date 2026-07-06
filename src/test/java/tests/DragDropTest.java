package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pages.DragDropPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DragDropTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
        page = browser.newPage();
    }

    @Test
    void testDragAndDrop() {
        DragDropPage dragDropPage = new DragDropPage(page);

        dragDropPage.navigateTo("https://the-internet.herokuapp.com/drag_and_drop");
        dragDropPage
                .dragDropArea()
                .dragAToB();

        assertEquals("A", dragDropPage.dragDropArea().getTextB());
    }

    @AfterEach
    void tearDown() {
        page.close();
        browser.close();
        playwright.close();
    }
}