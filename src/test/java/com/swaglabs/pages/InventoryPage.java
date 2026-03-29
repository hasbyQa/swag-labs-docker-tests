package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

// Represents the Products/Inventory page (post-login landing page).

public class InventoryPage extends BasePage {

    // Locators
    private static final By PAGE_TITLE        = By.cssSelector("[data-test='title']");
    private static final By CART_ICON         = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE        = By.cssSelector("[data-test='shopping-cart-badge']");

    // Add-to-cart buttons use data-test="add-to-cart-{product-name}"
    // We target the first item generically for flexibility
    private static final By FIRST_ADD_TO_CART = By.cssSelector("[data-test^='add-to-cart']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // Returns the "Products" heading text
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }

    // True if the inventory page title is showing — confirms successful login
    public boolean isLoaded() {
        return isVisible(PAGE_TITLE);
    }

    //Adds the first product on the page to the cart
    public InventoryPage addFirstItemToCart() {
        click(FIRST_ADD_TO_CART);
        return this;
    }

    // Adds a specific product by name.
    public InventoryPage addItemToCart(String productSlug) {
        click(By.cssSelector("[data-test='add-to-cart-" + productSlug + "']"));
        return this;
    }

    // Returns the cart badge count as a string (e.g., "1", "2")
    public String getCartBadgeCount() {
        return getText(CART_BADGE);
    }

    // True if the cart badge is visible (at least one item in cart)
    public boolean isCartBadgeVisible() {
        return isVisible(CART_BADGE);
    }

    // Navigates to the cart page
    public CartPage goToCart() {
        click(CART_ICON);
        return new CartPage(driver);
    }
}