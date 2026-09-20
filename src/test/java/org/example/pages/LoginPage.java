package org.example.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * LoginPage.java
 *
 * Amazon Login Page Object.
 *
 * Flow:
 * 1. Enter email/mobile
 * 2. Click Continue
 * 3. Enter password
 * 4. Click Sign In
 */
public class LoginPage {

    private WebDriver driver;

    private static final Logger logger =
            LogManager.getLogger(LoginPage.class);

    // ============================================================
    // LOCATORS
    // ============================================================

    /**
     * Amazon email/mobile field.
     *
     * Confirmed from current Amazon DOM:
     * id = ap_email_login
     */
    @FindBy(id = "ap_email_login")
    private WebElement emailField;

    /**
     * Continue button.
     *
     * Confirmed from current Amazon DOM:
     * id = continue
     */
    private By continueButton = By.id("continue");

    /**
     * Amazon password field.
     *
     * Confirmed from current Amazon DOM:
     * id = ap_password
     */
    @FindBy(id = "ap_password")
    private WebElement passwordField;

    /**
     * Sign In button.
     *
     * Amazon login button.
     */
    @FindBy(id = "signInSubmit")
    private WebElement loginButton;

    /**
     * Login page heading.
     */
    @FindBy(xpath =
            "//h1[contains(translate(normalize-space(.), " +
                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                    "'abcdefghijklmnopqrstuvwxyz'), 'sign in')]")
    private WebElement loginHeader;

    /**
     * Amazon error message.
     */
    private By errorMessage =
            By.xpath("//div[contains(@class,'a-alert-error')]");

    /**
     * Create Amazon account link.
     */
    private By createAccountLink =
            By.xpath("//a[contains(normalize-space(.), " +
                    "'Create your Amazon account')]");


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public LoginPage(WebDriver driver) {

        this.driver = driver;

        PageFactory.initElements(driver, this);

        logger.info("LoginPage initialized");
    }


    // ============================================================
    // LOGIN
    // ============================================================

    /**
     * Perform complete Amazon login.
     *
     * Flow:
     *
     * Email
     *   ↓
     * Continue
     *   ↓
     * Password
     *   ↓
     * Sign In
     *
     * @param email Amazon email/mobile
     * @param password Amazon password
     * @return HomePage
     */
    public HomePage login(String email, String password) {

        try {

            logger.info("========== LOGIN STARTED ==========");

            logger.info("Current URL: " + driver.getCurrentUrl());
            logger.info("Page Title: " + driver.getTitle());


            // ====================================================
            // STEP 1 - ENTER EMAIL
            // ====================================================

            logger.info("Waiting for email field");

            WaitUtils.waitForVisibility(
                    driver,
                    emailField,
                    10
            );

            emailField.clear();

            emailField.sendKeys(email);

            logger.info("Email entered successfully");


            // ====================================================
            // STEP 2 - CLICK CONTINUE
            // ====================================================

            logger.info("Waiting for Continue button");

            WebElement continueElement =
                    WaitUtils.waitForClickability(
                            driver,
                            continueButton,
                            10
                    );

            continueElement.click();

            logger.info("Continue button clicked");


            // ====================================================
            // STEP 3 - WAIT FOR PASSWORD PAGE
            // ====================================================

            logger.info("Waiting for password field");

            /*
             * Important:
             *
             * Amazon changes the page after Continue.
             * Therefore, locate the password field again
             * using its locator.
             */
            By passwordLocator =
                    By.id("ap_password");

            WebElement passwordFieldElement =
                    WaitUtils.waitForVisibility(
                            driver,
                            passwordLocator,
                            15
                    );

            logger.info("Password field is visible");


            // ====================================================
            // STEP 4 - ENTER PASSWORD
            // ====================================================

            passwordFieldElement.clear();

            passwordFieldElement.sendKeys(password);

            logger.info("Password entered successfully");


            // ====================================================
            // STEP 5 - CLICK SIGN IN
            // ====================================================

            logger.info("Waiting for Sign In button");

            WaitUtils.waitForClickability(
                    driver,
                    loginButton,
                    10
            ).click();

            logger.info("Sign In button clicked");


            // ====================================================
            // STEP 6 - WAIT FOR LOGIN RESULT
            // ====================================================

            Thread.sleep(2000);

            logger.info(
                    "Current URL after login: "
                            + driver.getCurrentUrl()
            );

            logger.info("Login operation completed");

            logger.info("========== LOGIN FINISHED ==========");

            return new HomePage(driver);

        } catch (Exception e) {

            logger.error(
                    "Error during login: "
                            + e.getMessage(),
                    e
            );

            throw new RuntimeException(
                    "Failed to login: "
                            + e.getMessage(),
                    e
            );
        }
    }


    // ============================================================
    // GET ERROR MESSAGE
    // ============================================================

    public String getErrorMessage() {

        try {

            logger.info("Checking for login error message");

            WebElement error =
                    WaitUtils.waitForVisibility(
                            driver,
                            errorMessage,
                            5
                    );

            String message = error.getText();

            logger.info(
                    "Login error message: "
                            + message
            );

            return message;

        } catch (Exception e) {

            logger.info("No login error message found");

            return "";
        }
    }


    // ============================================================
    // LOGIN PAGE VALIDATION
    // ============================================================

    public boolean isLoginPageLoaded() {

        try {

            logger.info(
                    "Checking whether Login Page is loaded"
            );

            WaitUtils.waitForVisibility(
                    driver,
                    emailField,
                    10
            );

            logger.info("Login Page is loaded");

            return true;

        } catch (Exception e) {

            logger.error(
                    "Login Page not loaded: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ============================================================
    // EMAIL FIELD VALIDATION
    // ============================================================

    public boolean isEmailFieldVisible() {

        try {

            WaitUtils.waitForVisibility(
                    driver,
                    emailField,
                    10
            );

            return emailField.isDisplayed();

        } catch (Exception e) {

            logger.error(
                    "Error checking email field visibility: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ============================================================
    // PASSWORD FIELD VALIDATION
    // ============================================================

    public boolean isPasswordFieldVisible() {

        try {

            /*
             * Use locator directly because password field
             * appears only after Continue.
             */
            WebElement password =
                    WaitUtils.waitForVisibility(
                            driver,
                            By.id("ap_password"),
                            10
                    );

            return password.isDisplayed();

        } catch (Exception e) {

            logger.error(
                    "Error checking password field visibility: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ============================================================
    // EMPTY EMAIL VALIDATION
    // ============================================================

    public boolean loginWithEmptyEmail() {

        try {

            logger.info(
                    "Testing login with empty email"
            );

            // Wait for email field
            WaitUtils.waitForVisibility(
                    driver,
                    emailField,
                    10
            );

            // Clear email
            emailField.clear();

            // Click Continue
            WaitUtils.waitForClickability(
                    driver,
                    continueButton,
                    10
            ).click();

            logger.info(
                    "Continue clicked with empty email"
            );

            Thread.sleep(1000);

            return !getErrorMessage().isEmpty();

        } catch (Exception e) {

            logger.error(
                    "Error in empty email test: "
                            + e.getMessage(),
                    e
            );

            return false;
        }
    }


    // ============================================================
    // EMPTY PASSWORD VALIDATION
    // ============================================================

    public boolean loginWithEmptyPassword(String email) {

        try {

            logger.info(
                    "Testing login with empty password"
            );


            // ----------------------------------------------------
            // Enter email
            // ----------------------------------------------------

            WaitUtils.waitForVisibility(
                    driver,
                    emailField,
                    10
            );

            emailField.clear();

            emailField.sendKeys(email);


            // ----------------------------------------------------
            // Click Continue
            // ----------------------------------------------------

            WaitUtils.waitForClickability(
                    driver,
                    continueButton,
                    10
            ).click();

            logger.info(
                    "Continue clicked"
            );


            // ----------------------------------------------------
            // Wait for password field
            // ----------------------------------------------------

            WebElement password =
                    WaitUtils.waitForVisibility(
                            driver,
                            By.id("ap_password"),
                            15
                    );

            logger.info(
                    "Password field is visible"
            );


            // ----------------------------------------------------
            // Do NOT enter password
            // ----------------------------------------------------

            password.clear();


            // ----------------------------------------------------
            // Click Sign In
            // ----------------------------------------------------

            WaitUtils.waitForClickability(
                    driver,
                    loginButton,
                    10
            ).click();

            logger.info(
                    "Sign In clicked with empty password"
            );


            Thread.sleep(1000);

            return !getErrorMessage().isEmpty();

        } catch (Exception e) {

            logger.error(
                    "Error in empty password test: "
                            + e.getMessage(),
                    e
            );

            return false;
        }
    }


    // ============================================================
    // CREATE ACCOUNT
    // ============================================================

    public boolean isCreateAccountLinkVisible() {

        try {

            WebElement createAccount =
                    WaitUtils.waitForVisibility(
                            driver,
                            createAccountLink,
                            10
                    );

            return createAccount.isDisplayed();

        } catch (Exception e) {

            logger.error(
                    "Error checking Create Account link: "
                            + e.getMessage()
            );

            return false;
        }
    }
}