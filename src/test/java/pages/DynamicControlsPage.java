package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class DynamicControlsPage {
    private final Page page;

    public DynamicControlsPage(Page page) {
        this.page = page;
    }

    public void clickRemoveButton() {
        page.locator("button:has-text('Remove')").click();
        page.locator("#checkbox").waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED)
        );
    }

    public boolean isCheckboxVisible() {
        return page.locator("#checkbox").isVisible();
    }
}