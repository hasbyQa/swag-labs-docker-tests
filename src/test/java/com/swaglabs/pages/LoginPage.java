package com.swaglabs.pages;

import com.swaglabs.utils.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

// Represents the Swag Labs login page.

public class LoginPage extends BasePage {

    // Locators (data-test attrs are least likely to change)
    private static final By USERNAME_INPUT  = By.id("user-name");
    private static final By PASSWORD_INPUT  = By.id("password");
    private static final By LOGIN_BUTTON    = By.id("login-button");
    private static final By ERROR_MESSAGE   = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Navigates directly to the login page
    public LoginPage open() {
        driver.get(TestConfig.BASE_URL);
        return this;
    }

    // Types credentials and clicks login — returns InventoryPage on success
    public InventoryPage loginAs(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        return new InventoryPage(driver);
    }

    // Attempts login without returning a page — used for failure scenarios
    public void attemptLogin(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }

    // Returns the error banner text shown after a failed login
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    // True if the error banner is displayed
    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }
}