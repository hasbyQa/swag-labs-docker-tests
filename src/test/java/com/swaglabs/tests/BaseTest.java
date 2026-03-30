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
// ALL @BeforeEach and @AfterEach logic lives here — test classes have none.
// Subclasses override prepare() to declare what state they need before each test.
public abstract class BaseTest {

    protected WebDriver driver;
    protected LoginPage     loginPage;
    protected InventoryPage inventoryPage;
    protected CartPage      cartPage;

    // Subclasses override this to declare their required starting state.
    // Default is no-op — LoginTest needs nothing beyond the login page.
    protected void prepare() {}

    @BeforeEach
    final void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();

        // Login page opened for every test — no test class needs to do this
        loginPage = new LoginPage(driver).open();

        // Runs the subclass-specific setup (inventory, cart, or nothing)
        prepare();
    }

    @AfterEach
    final void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Logs in and stores the inventory page — CartTest calls this via prepare()
    protected void setUpInventory() {
        inventoryPage = loginPage.loginAs(TestConfig.VALID_USER, TestConfig.VALID_PASSWORD);
    }

    // Logs in, adds one item, navigates to cart — CheckoutTest calls this via prepare()
    protected void setUpCart() {
        setUpInventory();
        inventoryPage.addItemToCart("sauce-labs-backpack");
        cartPage = inventoryPage.goToCart();
    }
}