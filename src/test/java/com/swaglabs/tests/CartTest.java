package com.swaglabs.tests;

import com.swaglabs.pages.CartPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Cart tests — no setup needed here, prepare() tells BaseTest to log in first
@DisplayName("Cart Tests")
class CartTest extends BaseTest {

    @Override
    protected void prepare() {
        setUpInventory();
    }

    // Adding items

    @Test
    @DisplayName("Adding an item shows badge on cart icon")
    void addItem_cartBadgeShouldAppear() {
        inventoryPage.addFirstItemToCart();

        assertTrue(inventoryPage.isCartBadgeVisible(),
                "Cart badge should be visible after adding an item");
    }

    @Test
    @DisplayName("Cart badge shows count of 1 after adding one item")
    void addOneItem_badgeShouldShowOne() {
        inventoryPage.addFirstItemToCart();

        assertEquals("1", inventoryPage.getCartBadgeCount(),
                "Badge should show '1' after adding one item");
    }

    @Test
    @DisplayName("Adding two items shows badge count of 2")
    void addTwoItems_badgeShouldShowTwo() {
        inventoryPage
                .addItemToCart("sauce-labs-backpack")
                .addItemToCart("sauce-labs-bike-light");

        assertEquals("2", inventoryPage.getCartBadgeCount(),
                "Badge should show '2' after adding two items");
    }

    // Cart page

    @Test
    @DisplayName("Cart page loads with the added item present")
    void cartPage_shouldContainAddedItem() {
        inventoryPage.addItemToCart("sauce-labs-backpack");
        CartPage cart = inventoryPage.goToCart();

        assertTrue(cart.isLoaded(), "Cart page should load successfully");
        assertTrue(cart.hasItems(), "Cart should contain the added item");
    }

    @Test
    @DisplayName("Item name in cart matches what was added")
    void cartItem_nameShouldMatchAddedProduct() {
        inventoryPage.addItemToCart("sauce-labs-backpack");
        CartPage cart = inventoryPage.goToCart();

        assertTrue(cart.getFirstItemName().toLowerCase().contains("backpack"),
                "Cart item name should contain 'backpack'");
    }

    // Removing items

    @Test
    @DisplayName("Removing the only item empties the cart")
    void removeItem_cartShouldBeEmpty() {
        inventoryPage.addFirstItemToCart();
        CartPage cart = inventoryPage.goToCart();
        cart.removeFirstItem();

        assertFalse(cart.hasItems(),
                "Cart should be empty after removing the only item");
    }

    @Test
    @DisplayName("Cart badge disappears when cart is emptied")
    void removeAllItems_badgeShouldDisappear() {
        inventoryPage.addFirstItemToCart();
        CartPage cart = inventoryPage.goToCart();
        cart.removeFirstItem();
        cart.continueShopping();

        assertFalse(inventoryPage.isCartBadgeVisible(),
                "Cart badge should not be visible when cart is empty");
    }
}