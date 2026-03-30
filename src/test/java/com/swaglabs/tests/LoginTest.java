package com.swaglabs.tests;

import com.swaglabs.utils.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Login tests — loginPage is opened in BaseTest.setUp(), nothing to set up here
@DisplayName("Login Tests")
class LoginTest extends BaseTest {

    // Happy path

    @Test
    @DisplayName("Valid credentials redirect to Products page")
    void validLogin_shouldLandOnInventoryPage() {
        inventoryPage = loginPage.loginAs(TestConfig.VALID_USER, TestConfig.VALID_PASSWORD);

        assertTrue(inventoryPage.isLoaded(),
                "Inventory page should be visible after successful login");
        assertEquals(TestConfig.PRODUCTS_TITLE, inventoryPage.getPageTitle(),
                "Page title should read 'Products'");
    }

    @Test
    @DisplayName("URL contains /inventory.html after login")
    void validLogin_urlShouldContainInventory() {
        loginPage.loginAs(TestConfig.VALID_USER, TestConfig.VALID_PASSWORD);

        assertTrue(driver.getCurrentUrl().contains("inventory"),
                "URL should contain 'inventory' after login");
    }

    // Sad paths

    @Test
    @DisplayName("Wrong password shows error banner")
    void invalidPassword_shouldShowError() {
        loginPage.attemptLogin(TestConfig.VALID_USER, TestConfig.INVALID_PASSWORD);

        assertTrue(loginPage.isErrorDisplayed(),
                "Error message should appear for wrong password");
        assertTrue(loginPage.getErrorMessage().contains(TestConfig.LOGIN_ERROR_MESSAGE),
                "Error text should start with 'Epic sadface:'");
    }

    @Test
    @DisplayName("Wrong username shows error banner")
    void invalidUsername_shouldShowError() {
        loginPage.attemptLogin(TestConfig.INVALID_USER, TestConfig.VALID_PASSWORD);

        assertTrue(loginPage.isErrorDisplayed(),
                "Error message should appear for wrong username");
    }

    @Test
    @DisplayName("Locked-out user cannot log in")
    void lockedOutUser_shouldSeeError() {
        loginPage.attemptLogin(TestConfig.LOCKED_USER, TestConfig.VALID_PASSWORD);

        assertTrue(loginPage.isErrorDisplayed(),
                "Locked-out user should see an error");
        assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked"),
                "Error should mention 'locked'");
    }

    @Test
    @DisplayName("Empty credentials show error banner")
    void emptyCredentials_shouldShowError() {
        loginPage.attemptLogin("", "");

        assertTrue(loginPage.isErrorDisplayed(),
                "Submitting empty form should show an error");
    }
}