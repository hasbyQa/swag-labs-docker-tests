package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Covers the full 3-step checkout flow:
 *   Step 1 → Fill personal info (/checkout-step-one.html)
 *   Step 2 → Order overview    (/checkout-step-two.html)
 *   Complete → Confirmation     (/checkout-complete.html)
 */
public class CheckoutPage extends BasePage {

    // Step 1 locators
    private static final By FIRST_NAME_INPUT = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME_INPUT  = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE      = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE_BUTTON  = By.cssSelector("[data-test='continue']");
    private static final By ERROR_MESSAGE    = By.cssSelector("[data-test='error']");

    // Step 2 locators
    private static final By FINISH_BUTTON    = By.cssSelector("[data-test='finish']");
    private static final By ITEM_TOTAL       = By.cssSelector("[data-test='subtotal-label']");

    // Confirmation locators
    private static final By COMPLETE_HEADER  = By.cssSelector("[data-test='complete-header']");
    private static final By BACK_HOME_BUTTON = By.cssSelector("[data-test='back-to-products']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Step 1

    // Fills in the checkout info form and clicks Continue
    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME_INPUT, firstName);
        type(LAST_NAME_INPUT, lastName);
        type(POSTAL_CODE, postalCode);
        click(CONTINUE_BUTTON);
        return this;
    }

    // Clicks Continue without filling any fields — triggers validation error
    public CheckoutPage submitEmptyForm() {
        click(CONTINUE_BUTTON);
        return this;
    }

    // Returns the form validation error message
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }

    // Step 2

    // Returns the item subtotal text (e.g., "Item total: $29.99")
    public String getItemTotal() {
        return getText(ITEM_TOTAL);
    }

    // Confirms the order — goes to the completion page
    public CheckoutPage finishOrder() {
        click(FINISH_BUTTON);
        return this;
    }

    // Confirmation

    // Returns "Thank you for your order!" confirmation text
    public String getConfirmationMessage() {
        return getText(COMPLETE_HEADER);
    }

    // True if the order completion header is visible
    public boolean isOrderComplete() {
        return isVisible(COMPLETE_HEADER);
    }

    // Goes back to the products page from confirmation screen
    public InventoryPage backToProducts() {
        click(BACK_HOME_BUTTON);
        return new InventoryPage(driver);
    }
}