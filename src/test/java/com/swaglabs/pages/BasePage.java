package com.swaglabs.pages;

import com.swaglabs.utils.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Parent class for all page objects — wraps raw Selenium calls into clean reusable methods
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.TIMEOUT_SECONDS));
    }

    // Waits until the element can be clicked, then clicks it
    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    // Waits for the element, clears it, then types the text
    protected void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    // Returns the visible text of an element, trimmed
    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
    }

    // Returns true if the element is visible on the page, false otherwise
    protected boolean isVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Blocks until the element is visible in the DOM — use as a page-load gate
    protected void waitForElementVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Blocks until the browser URL contains the given string — confirms navigation happened
    protected void waitForUrlToContain(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}