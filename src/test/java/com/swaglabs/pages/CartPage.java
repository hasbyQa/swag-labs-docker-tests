package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

//Represents the Shopping Cart page (/cart.html)

public class CartPage extends BasePage {

    // Locators
    private static final By PAGE_TITLE       = By.cssSelector("[data-test='title']");
    private static final By CART_ITEM        = By.cssSelector("[data-test='inventory-item']");
    private static final By ITEM_NAME        = By.cssSelector("[data-test='inventory-item-name']");
    private static final By CHECKOUT_BUTTON  = By.cssSelector("[data-test='checkout']");
    private static final By CONTINUE_BUTTON  = By.cssSelector("[data-test='continue-shopping']");
    private static final By REMOVE_BUTTON    = By.cssSelector("[data-test^='remove']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    // True if cart page header is visible
    public boolean isLoaded() {
        return isVisible(PAGE_TITLE);
    }

    // True if at least one cart item is present
    public boolean hasItems() {
        return isVisible(CART_ITEM);
    }

    // Returns the name of the first item in the cart
    public String getFirstItemName() {
        return getText(ITEM_NAME);
    }

    // Removes the first item in the cart
    public CartPage removeFirstItem() {
        click(REMOVE_BUTTON);
        return this;
    }

    // Proceeds to the checkout information form
    public CheckoutPage proceedToCheckout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutPage(driver);
    }

    // Goes back to the products page
    public InventoryPage continueShopping() {
        click(CONTINUE_BUTTON);
        return new InventoryPage(driver);
    }
}