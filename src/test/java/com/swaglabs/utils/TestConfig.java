package com.swaglabs.utils;

// Central config — every constant lives here
public final class TestConfig {

    // Prevent instantiation — this is a constants-only class
    private TestConfig() {}

    // App
    public static final String BASE_URL        = "https://www.saucedemo.com";
    public static final int    TIMEOUT_SECONDS = 10;

    // Valid credentials
    public static final String VALID_USER     = "standard_user";
    public static final String VALID_PASSWORD = "secret_sauce";

    // Invalid credentials (for negative tests)
    public static final String INVALID_USER     = "bad_user";
    public static final String INVALID_PASSWORD = "bad_password";

    // Locked-out user (Swag Labs built-in test account)
    public static final String LOCKED_USER = "locked_out_user";

    // Checkout form data
    public static final String FIRST_NAME  = "Test";
    public static final String LAST_NAME   = "User";
    public static final String POSTAL_CODE = "12345";

    // Expected UI text
    public static final String LOGIN_ERROR_MESSAGE   = "Epic sadface:";
    public static final String CHECKOUT_COMPLETE_MSG = "Thank you for your order!";
    public static final String PRODUCTS_TITLE        = "Products";
}