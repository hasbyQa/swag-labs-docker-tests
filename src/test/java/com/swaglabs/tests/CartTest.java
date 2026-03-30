package com.swaglabs.tests;

import com.swaglabs.pages.CartPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Cart tests — uses inventoryPage from BaseTest, setup is a single delegated call
@DisplayName("Cart Tests")
class CartTest extends BaseTest {

    @BeforeEach
    void setup() {
        setUpInventory(); // login and land on inventory — logic lives in BaseTest
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
        CartPage cartPage = inventoryPage.goToCart();

        assertTrue(cartPage.isLoaded(), "Cart page should load successfully");
        assertTrue(cartPage.hasItems(), "Cart should contain the added item");
    }

    @Test
    @DisplayName("Item name in cart matches what was added")
    void cartItem_nameShouldMatchAddedProduct() {
        inventoryPage.addItemToCart("sauce-labs-backpack");
        CartPage cartPage = inventoryPage.goToCart();

        assertTrue(cartPage.getFirstItemName().toLowerCase().contains("backpack"),
                "Cart item name should contain 'backpack'");
    }

    // Removing items

    @Test
    @DisplayName("Removing the only item empties the cart")
    void removeItem_cartShouldBeEmpty() {
        inventoryPage.addFirstItemToCart();
        CartPage cartPage = inventoryPage.goToCart();
        cartPage.removeFirstItem();

        assertFalse(cartPage.hasItems(),
                "Cart should be empty after removing the only item");
    }

    @Test
    @DisplayName("Cart badge disappears when cart is emptied")
    void removeAllItems_badgeShouldDisappear() {
        inventoryPage.addFirstItemToCart();
        CartPage cartPage = inventoryPage.goToCart();
        cartPage.removeFirstItem();
        cartPage.continueShopping();

        assertFalse(inventoryPage.isCartBadgeVisible(),
                "Cart badge should not be visible when cart is empty");
    }
}