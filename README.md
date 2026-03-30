# Swag Labs Test Suite

A comprehensive Selenium-based test automation suite for the [Swag Labs](https://www.saucedemo.com/) e-commerce platform. This project demonstrates best practices in test framework design, Docker containerization, and CI/CD integration with GitHub Actions, Slack, and Email notifications.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [Running Tests](#running-tests)
- [Test Coverage](#test-coverage)
- [Architecture](#architecture)
- [Docker Deployment](#docker-deployment)
- [CI/CD Pipeline](#cicd-pipeline)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## Overview

This test suite automates functional testing for the Swag Labs demo application, covering:
- **Login workflows** (valid/invalid credentials, error handling)
- **Inventory browsing** (product listing, sorting, filtering)
- **Shopping cart** (add/remove items, quantity management)
- **Checkout process** (multi-step form, validation, order confirmation)

The suite is designed to run reliably in headless Chrome environments (Docker, CI/CD) with intelligent waits and JavaScript-based interactions to handle rendering quirks.

## Features

- **Page Object Model (POM)**: Clean separation between test logic and page interactions
- **BasePage abstraction**: Reusable methods for common Selenium operations
- **Docker containerization**: Fully reproducible test environment with Chrome included
- **Headless Chrome optimization**: JavaScript-based interactions optimized for headless rendering
- **CI/CD integration**: GitHub Actions workflow with Slack & email notifications
- **Test reports**: Maven Surefire reports with detailed test results
- **Configurable timeouts**: Environment-based timeout management for Docker vs. local testing

## Prerequisites

### Local Development
- **Java 17+** (JDK)
- **Maven 3.9.6+**
- **Chrome/Chromium** browser
- **ChromeDriver** (matching your Chrome version)

### Docker
- **Docker** (all dependencies included in image)

### CI/CD
- GitHub repository with Actions enabled
- Slack webhook URL (optional)
- SMTP credentials for email notifications (optional)

## Project Structure

```
swag-labs-tests/
├── src/
│   ├── main/
│   │   └── java/com/swaglabs/          # Main application code (if needed)
│   └── test/
│       ├── java/com/swaglabs/
│       │   ├── pages/                  # Page Object Model classes
│       │   │   ├── BasePage.java       # Parent class with reusable methods
│       │   │   ├── LoginPage.java      # Login page interactions
│       │   │   ├── InventoryPage.java  # Product listing page
│       │   │   ├── CartPage.java       # Shopping cart page
│       │   │   └── CheckoutPage.java   # Checkout process (3 steps)
│       │   ├── tests/                  # Test classes
│       │   │   ├── BaseTest.java       # Test setup & teardown
│       │   │   ├── LoginTest.java      # Login tests
│       │   │   ├── CartTest.java       # Cart management tests
│       │   │   └── CheckoutTest.java   # Checkout workflow tests
│       │   └── utils/
│       │       └── TestConfig.java     # Configuration & constants
│       └── resources/                  # Test resources (if any)
├── pom.xml                            # Maven configuration & dependencies
├── Dockerfile                         # Docker image definition
├── .github/
│   └── workflows/
│       └── ci.yml                     # GitHub Actions CI/CD pipeline
└── README.md                          # This file
```

## Installation & Setup

### Local Environment

1. **Clone the repository**
   ```bash
   git clone https://github.com/hasbyQa/swag-labs-docker-tests.git
   cd swag-labs-tests
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure ChromeDriver**
   - Download ChromeDriver matching your Chrome version from [chromedriver.chromium.org](https://chromedriver.chromium.org/)
   - Add to system PATH or configure in test setup

4. **Run tests**
   ```bash
   mvn test
   ```

### Docker Environment

1. **Build the Docker image**
   ```bash
   docker build -t swag-labs-tests .
   ```

2. **Run tests in container**
   ```bash
   docker run swag-labs-tests
   ```

3. **Extract test reports**
   ```bash
   docker run --name test-run swag-labs-tests
   docker cp test-run:/app/target/surefire-reports ./reports
   docker rm test-run
   ```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=LoginTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=LoginTest#validLogin_shouldNavigateToInventory
```

### Run with Custom Timeout (Docker environments)
```bash
SELENIUM_TIMEOUT=60 mvn test
```

### Run with Debug Output
```bash
mvn test -X
```

## Test Coverage

| Module | Test Class | Test Count | Coverage |
|--------|-----------|-----------|----------|
| **Login** | `LoginTest.java` | 4 | Valid/invalid credentials, error handling |
| **Cart** | `CartTest.java` | 6 | Add/remove items, quantity, persistence |
| **Checkout** | `CheckoutTest.java` | 9 | Form validation, multi-step flow, confirmation |
| **Total** | - | **19** | Core e-commerce workflows |

### Example Tests

#### Login
- Valid credentials → navigates to inventory
- Invalid credentials → displays error message
- Empty fields → validation error
- Locked account → account locked error

#### Cart
- Add item to cart → item appears in cart
- Remove item from cart → item removed
- Cart persists → items remain after navigation
- Quantity management → correct totals

#### Checkout
- Fill valid info → proceeds to overview
- Empty form → validation error
- Complete order → confirmation page
- Order total calculation → correct subtotal display

## Architecture

### Page Object Model (POM)

Each page is represented by a dedicated class:

```java
public class LoginPage extends BasePage {
    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    
    public InventoryPage login(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        return new InventoryPage(driver);
    }
}
```

### BasePage - Reusable Methods

```java
protected void click(By locator)              // Standard click
protected void jsClick(By locator)            // JavaScript click (for headless)
protected void type(By locator, String text)  // Type with clear
protected String getText(By locator)          // Get visible text
protected boolean isVisible(By locator)       // Check visibility
protected void waitForElementVisible(By)      // Wait for visibility
protected void waitForElementPresent(By)      // Wait for presence in DOM
protected void waitForUrlToContain(String)    // Wait for URL change
```

### Test Structure

Each test class extends `BaseTest` which handles:
- WebDriver initialization
- Browser setup
- Login/logout lifecycle
- Cleanup after tests

```java
public class CheckoutTest extends BaseTest {
    @Test
    public void fullCheckout_shouldShowConfirmation() {
        loginPage
            .login("standard_user", "secret_sauce")
            .selectAndAddToCart()
            .goToCart()
            .checkout("John", "Doe", "12345")
            .finishOrder();
        
        assertTrue(checkoutPage.isOrderComplete());
    }
}
```

## Docker Deployment

### Dockerfile Overview

The Dockerfile:
1. Uses Maven 3.9.6 + Java 17 as base
2. Installs Chrome and system dependencies
3. Pre-downloads Maven dependencies (layer caching)
4. Copies source code and runs tests
5. Sets `SELENIUM_TIMEOUT=30` for Docker environments

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app
RUN apt-get update && apt-get install -y google-chrome-stable
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
ENV SELENIUM_TIMEOUT=30
CMD ["mvn", "test", "-B", "-Dmaven.surefire.timeout=600"]
```

### Building and Running

```bash
# Build image
docker build -t swag-labs-tests .

# Run tests (with automatic exit)
docker run swag-labs-tests

# Run and keep container for inspection
docker run --name test-run swag-labs-tests
docker logs test-run
docker rm test-run
```

## CI/CD Pipeline

### GitHub Actions Workflow (`.github/workflows/ci.yml`)

The CI/CD pipeline automatically runs on:
- Push to `main` branch
- Pull requests to `main` branch

#### Pipeline Steps

1. **Checkout Code** - Retrieves repository
2. **Build Docker Image** - Creates test environment
3. **Run Tests** - Executes test suite in container
4. **Parse Results** - Extracts test statistics from XML reports
5. **Slack Notification** - Posts results to Slack channel
6. **Email Notification** - Sends detailed HTML email report
7. **Upload Artifacts** - Stores test reports for download

#### Test Results Parsing

The workflow parses Surefire reports and extracts:
- Total tests run
- Passed/Failed/Errored tests
- Skipped tests
- Failed test names

#### Notifications

**Slack Message** includes:
- Status badge (passed/failed)
- Test statistics (Passed, Failed, Errors, Skipped)
- Repository, branch, and commit info
- Direct link to GitHub Actions run

**Email Report** includes:
- Formatted HTML with color-coded results
- Detailed test statistics table
- Failed test names and details
- Download link for test artifacts

### Setting Up Notifications

#### Slack Integration
1. Create Slack webhook: [api.slack.com/apps](https://api.slack.com/apps)
2. Add secret to repository: `Settings → Secrets → SLACK_WEBHOOK_URL`

#### Email Integration
1. Use Gmail or SMTP provider
2. Add secrets:
   - `MAIL_USERNAME` - Email address
   - `MAIL_PASSWORD` - App password (not regular password)
   - `MAIL_RECIPIENT` - Email to receive reports

## Configuration

### TestConfig.java

Configure test behavior:

```java
public class TestConfig {
    // Timeout for element waits (seconds)
    public static final int TIMEOUT_SECONDS = 
        Integer.parseInt(System.getenv().getOrDefault("SELENIUM_TIMEOUT", "20"));
    
    // Base URL for the application
    public static final String BASE_URL = "https://www.saucedemo.com";
}
```

### Environment Variables

| Variable | Default | Purpose |
|----------|---------|---------|
| `SELENIUM_TIMEOUT` | 20s | Element wait timeout (increase for slow CI/Docker) |
| `BROWSER` | chrome | Browser type (future expansion) |

## Troubleshooting

### Issue: Tests timeout in Docker
**Solution**: Increase `SELENIUM_TIMEOUT` in Dockerfile
```dockerfile
ENV SELENIUM_TIMEOUT=60  # Increased from 30
```

### Issue: Chrome fails to start
**Solution**: Ensure Chrome dependencies are installed
```bash
docker build -t swag-labs-tests .  # Rebuilds with all dependencies
```

### Issue: JavaScript errors in headless Chrome
**Solution**: Use `jsClick()` instead of `click()` for form submissions
```java
private void submitFormViaJs() {
    jsClick(CONTINUE_BUTTON);  // More reliable than .submit()
}
```

### Issue: Elements not found
**Solution**: Check if element exists and is visible
```java
protected boolean isVisible(By locator) {
    try {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator))
                   .isDisplayed();
    } catch (Exception e) {
        return false;
    }
}
```

### Issue: Test reports not uploaded in CI
**Solution**: Check if surefire reports directory exists
```bash
docker run --name test-run swag-labs-tests
docker cp test-run:/app/target/surefire-reports ./reports
```

## Key Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| Selenium | 4.18.1 | Web browser automation |
| JUnit 5 | 5.9.3 | Test framework |
| Maven Surefire | 3.2.5 | Test execution plugin |
| ChromeDriver | Latest | Chrome WebDriver |

See `pom.xml` for complete dependency tree.

## Contributing

1. **Create a feature branch**
   ```bash
   git checkout -b feature/my-feature
   ```

2. **Make changes and run tests**
   ```bash
   mvn test
   ```

3. **Commit and push**
   ```bash
   git commit -m "Add my feature"
   git push origin feature/my-feature
   ```

4. **Open a pull request**
   - CI/CD will automatically run tests
   - Review required before merge

## License

This project is open source and available under the MIT License.

## Support

For issues or questions:
1. Check [Troubleshooting](#troubleshooting) section
2. Review test logs in GitHub Actions
3. Check Surefire reports in `target/surefire-reports/`
4. Open an issue on GitHub

---

**Last Updated**: March 2026  
**Maintainer**: hasbyQa  
**Repository**: [swag-labs-docker-tests](https://github.com/hasbyQa/swag-labs-docker-tests)
