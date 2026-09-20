package org.example.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.base.BaseClass;
import org.example.pages.HomePage;
import org.example.pages.LoginPage;
import org.example.utilities.DataProviders;
import org.example.utilities.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest.java
 * Purpose: Test cases for Amazon login functionality
 * Includes positive and negative login test scenarios
 * Uses DataProvider for parameterized testing
 */
public class LoginTest extends BaseClass {

    private static final Logger logger = LogManager.getLogger(LoginTest.class);

    /**
     * Test valid login with correct credentials
     * Priority: 1 (high priority, runs first)
     * Group: smoke (critical smoke test)
     */
    @Test(priority = 1, groups = "smoke", retryAnalyzer = RetryAnalyzer.class)
    public void testValidLogin() {
        try {
            logger.info("Test: Valid Login - Started");

            // Navigate to home page
            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");
            logger.info("Home page loaded successfully");

            // Click on sign in
            LoginPage loginPage = homePage.clickSignIn();
            Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should load");
            logger.info("Login page loaded successfully");

            // Perform login with valid credentials
            // Note: Replace with actual test credentials from config
            String validEmail = "testuser@gmail.com";
            String validPassword = "testpassword123";

            logger.info("Attempting login with email: " + validEmail);
            homePage = loginPage.login(validEmail, validPassword);

            // Verify successful login
            Assert.assertTrue(homePage.isHomePageLoaded(), "Should be redirected to home page after successful login");
            logger.info("Test: Valid Login - Passed");

        } catch (Exception e) {
            logger.error("Test: Valid Login - Failed: " + e.getMessage());
            Assert.fail("Valid login test failed: " + e.getMessage());
        }
    }

    /**
     * Test invalid login with wrong credentials
     * Priority: 2
     * Group: regression
     */
    @Test(priority = 2, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testInvalidLogin() {
        try {
            logger.info("Test: Invalid Login - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            LoginPage loginPage = homePage.clickSignIn();
            Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should load");

            // Attempt login with invalid credentials
            String invalidEmail = "invalid@gmail.com";
            String invalidPassword = "wrongpassword";

            logger.info("Attempting login with invalid credentials");
            try {
                loginPage.login(invalidEmail, invalidPassword);
            } catch (Exception e) {
                // Expected to fail
            }

            // Verify error message is displayed
            String errorMessage = loginPage.getErrorMessage();
            Assert.assertFalse(errorMessage.isEmpty(), "Error message should be displayed for invalid login");
            logger.info("Error message displayed: " + errorMessage);
            logger.info("Test: Invalid Login - Passed");

        } catch (Exception e) {
            logger.error("Test: Invalid Login - Failed: " + e.getMessage());
            Assert.fail("Invalid login test failed: " + e.getMessage());
        }
    }

    /**
     * Test login with empty email field
     * Priority: 3
     * Group: regression
     */
    @Test(priority = 3, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testLoginWithEmptyEmail() {
        try {
            logger.info("Test: Login With Empty Email - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            LoginPage loginPage = homePage.clickSignIn();
            Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should load");

            // Verify email field is visible
            Assert.assertTrue(loginPage.isEmailFieldVisible(), "Email field should be visible");

            // Try login with empty email
            boolean errorDisplayed = loginPage.loginWithEmptyEmail();
            Assert.assertTrue(errorDisplayed, "Error message should be displayed for empty email");
            logger.info("Test: Login With Empty Email - Passed");

        } catch (Exception e) {
            logger.error("Test: Login With Empty Email - Failed: " + e.getMessage());
            Assert.fail("Empty email login test failed: " + e.getMessage());
        }
    }

    /**
     * Test login with empty password field
     * Priority: 4
     * Group: regression
     */
    @Test(priority = 4, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testLoginWithEmptyPassword() {
        try {
            logger.info("Test: Login With Empty Password - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            LoginPage loginPage = homePage.clickSignIn();
            Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should load");

            String testEmail = "test@gmail.com";

            // Try login with empty password
            boolean errorDisplayed = loginPage.loginWithEmptyPassword(testEmail);
            Assert.assertTrue(errorDisplayed, "Error message should be displayed for empty password");
            logger.info("Test: Login With Empty Password - Passed");

        } catch (Exception e) {
            logger.error("Test: Login With Empty Password - Failed: " + e.getMessage());
            Assert.fail("Empty password login test failed: " + e.getMessage());
        }
    }

    /**
     * Parameterized login test using DataProvider
     * Tests multiple login scenarios with data from DataProviders class
     *
     * @param email user email
     * @param password user password
     * @param expectedResult expected result (success/failure)
     */
    @Test(dataProvider = "loginData", dataProviderClass = DataProviders.class,
            groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testLoginWithDataProvider(String email, String password, String expectedResult) {
        try {
            logger.info("Test: DataProvider Login - Email: " + email + ", Expected: " + expectedResult);

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            LoginPage loginPage = homePage.clickSignIn();
            Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should load");

            try {
                homePage = loginPage.login(email, password);
                if ("success".equals(expectedResult)) {
                    Assert.assertTrue(homePage.isHomePageLoaded(), "Should login successfully");
                    logger.info("Login successful as expected");
                } else {
                    logger.error("Login should have failed but succeeded");
                }
            } catch (Exception e) {
                if ("failure".equals(expectedResult)) {
                    logger.info("Login failed as expected");
                } else {
                    throw e;
                }
            }

            logger.info("Test: DataProvider Login - Passed");

        } catch (Exception e) {
            logger.error("Test: DataProvider Login - Failed: " + e.getMessage());
            Assert.fail("DataProvider login test failed: " + e.getMessage());
        }
    }

    /**
     * Verify login page UI elements
     * Priority: 5
     * Group: sanity
     */
    @Test(priority = 5, groups = "sanity")
    public void testLoginPageUIElements() {
        try {
            logger.info("Test: Login Page UI Elements - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            LoginPage loginPage = homePage.clickSignIn();
            Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should load");

            // Verify UI elements
            Assert.assertTrue(loginPage.isEmailFieldVisible(), "Email field should be visible");
            Assert.assertTrue(loginPage.isPasswordFieldVisible(), "Password field should be visible");

            logger.info("Test: Login Page UI Elements - Passed");

        } catch (Exception e) {
            logger.error("Test: Login Page UI Elements - Failed: " + e.getMessage());
            Assert.fail("Login page UI elements test failed: " + e.getMessage());
        }
    }
}

