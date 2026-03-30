package com.swaglabs.tests;

import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.utils.TestConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

// Single parent for all test classes.
// Driver lifecycle, shared page state, and navigation helpers all live here.
// Test classes contain ONLY @Test methods and a single @BeforeEach setup call.
public abstract class BaseTest {

    protected WebDriver driver;

    // Shared page state — subclasses access whichever they need
    protected LoginPage     loginPage;
    protected InventoryPage inventoryPage;
    protected CartPage      cartPage;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");           // headless mode, Chrome 112+
        options.addArguments("--no-sandbox");             // required inside Docker
        options.addArguments("--disable-dev-shm-usage");  // prevents shared memory issues
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();

        // Every test suite starts on the login page — no test class needs to do this
        loginPage = new LoginPage(driver).open();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Logs in with valid credentials and stores result in inventoryPage
    // CartTest calls this as its full setup
    protected void setUpInventory() {
        inventoryPage = loginPage.loginAs(TestConfig.VALID_USER, TestConfig.VALID_PASSWORD);
    }

    // Logs in, adds one item, and navigates to the cart
    // CheckoutTest calls this as its full setup
    protected void setUpCart() {
        setUpInventory();
        inventoryPage.addItemToCart("sauce-labs-backpack");
        cartPage = inventoryPage.goToCart();
    }
}