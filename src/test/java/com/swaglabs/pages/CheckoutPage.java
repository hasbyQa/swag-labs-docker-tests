package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

// Covers all 3 checkout steps:
//   Step 1 - fill info  (/checkout-step-one.html)
//   Step 2 - overview   (/checkout-step-two.html)
//   Done   - confirmed  (/checkout-complete.html)
public class CheckoutPage extends BasePage {

    // Confirmed from live DOM: this site uses camelCase data-test values
    private static final By FIRST_NAME_INPUT = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME_INPUT  = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE      = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE_BUTTON  = By.cssSelector("[data-test='continue']");
    private static final By ERROR_MESSAGE    = By.cssSelector("[data-test='error']");

    // Step 2 — only present on /checkout-step-two.html
    private static final By FINISH_BUTTON = By.cssSelector("[data-test='finish']");
    private static final By ITEM_TOTAL    = By.cssSelector("[data-test='subtotal-label']");

    // Confirmation — only present on /checkout-complete.html
    private static final By COMPLETE_HEADER  = By.cssSelector("[data-test='complete-header']");
    private static final By BACK_HOME_BUTTON = By.cssSelector("[data-test='back-to-products']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Form is already rendered when we arrive (CartPage.proceedToCheckout guarantees it)
    // Double wait: URL first (fast), then element (confirms React rendered step-two DOM)
    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME_INPUT, firstName);
        type(LAST_NAME_INPUT, lastName);
        type(POSTAL_CODE, postalCode);
        click(CONTINUE_BUTTON);
        waitForUrlToContain("checkout-step-two");
        waitForElementVisible(FINISH_BUTTON);
        return this;
    }

    // Submits with nothing filled — stays on step 1 with a validation error banner
    public CheckoutPage submitEmptyForm() {
        click(CONTINUE_BUTTON);
        waitForElementVisible(ERROR_MESSAGE);
        return this;
    }

    // Types only first name then submits — triggers "Last Name is required" error
    public CheckoutPage fillFirstNameOnly(String firstName) {
        type(FIRST_NAME_INPUT, firstName);
        click(CONTINUE_BUTTON);
        waitForElementVisible(ERROR_MESSAGE);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }

    // Returns the subtotal line e.g. "Item total: $29.99"
    public String getItemTotal() {
        return getText(ITEM_TOTAL);
    }

    // Clicks Finish — double wait: URL then confirmation header
    public CheckoutPage finishOrder() {
        click(FINISH_BUTTON);
        waitForUrlToContain("checkout-complete");
        waitForElementVisible(COMPLETE_HEADER);
        return this;
    }

    public String getConfirmationMessage() {
        return getText(COMPLETE_HEADER);
    }

    public boolean isOrderComplete() {
        return isVisible(COMPLETE_HEADER);
    }

    // Clicks Back Home and waits for inventory URL
    public InventoryPage backToProducts() {
        click(BACK_HOME_BUTTON);
        waitForUrlToContain("inventory");
        return new InventoryPage(driver);
    }
}