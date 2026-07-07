package tests;

import context.TestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.DynamicControlsPage;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DynamicControlsTest {
    private TestContext context;
    private DynamicControlsPage controlsPage;

    @BeforeEach
    public void setup() {
        context = new TestContext();
        controlsPage = new DynamicControlsPage(context.getPage());
    }

    @Test
    public void testCheckboxRemoval() {
        context.getPage().navigate("https://the-internet.herokuapp.com/dynamic_controls");
        controlsPage.clickRemoveButton();

        assertFalse(controlsPage.isCheckboxVisible());
    }

    @AfterEach
    public void teardown() {
        context.close();
    }
}
