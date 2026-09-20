package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.interactions.Actions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utilities.WaitUtils;

/**
 * LogoutPage.java
 * Purpose: Page Object Model for Amazon Logout functionality
 * Contains locators and methods for logout operations
 */
public class LogoutPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(LogoutPage.class);

    // Locators using @FindBy annotation
    @FindBy(id = "nav-link-accountList")
    private WebElement accountMenuButton;

    @FindBy(xpath = "//span[contains(text(), 'Sign Out')]")
    private WebElement signOutButton;

    @FindBy(xpath = "//span[contains(text(), 'Your Account')]")
    private WebElement yourAccountOption;

    @FindBy(xpath = "//a[contains(text(), 'Sign out')]")
    private WebElement signOutLink;

    @FindBy(id = "nav-logo-sprites")
    private WebElement amazonLogo;

    /**
     * Constructor to initialize PageFactory elements
     *
     * @param driver WebDriver instance
     */
    public LogoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("LogoutPage initialized");
    }

    /**
     * Perform logout operation
     * Clicks on account menu and then sign out option
     *
     * @return HomePage object after logout
     */
    public HomePage logout() {
        try {
            logger.info("Performing logout");

            // Wait for account menu button to be clickable
            WaitUtils.waitForClickability(driver, accountMenuButton, 10);
            logger.info("Account menu button is clickable");

            // Use Actions to hover over account menu button
            Actions actions = new Actions(driver);
            actions.moveToElement(accountMenuButton).perform();
            logger.info("Hovered over account menu button");

            // Wait for sign out button to appear
            Thread.sleep(1000);
            WaitUtils.waitForVisibility(driver, signOutButton, 10);
            logger.info("Sign out button is visible");

            // Click sign out button
            signOutButton.click();
            logger.info("Sign out button clicked");

            Thread.sleep(2000);
            logger.info("Logout successful");

            return new HomePage(driver);

        } catch (Exception e) {
            logger.error("Error during logout: " + e.getMessage());
            throw new RuntimeException("Failed to logout: " + e.getMessage());
        }
    }

    /**
     * Alternative logout method using sign out link
     *
     * @return HomePage object after logout
     */
    public HomePage logoutUsingLink() {
        try {
            logger.info("Performing logout using sign out link");

            WaitUtils.waitForClickability(driver, signOutLink, 10);
            signOutLink.click();
            logger.info("Sign out link clicked");

            Thread.sleep(2000);
            logger.info("Logout via link successful");

            return new HomePage(driver);

        } catch (Exception e) {
            logger.error("Error during logout via link: " + e.getMessage());
            throw new RuntimeException("Failed to logout via link: " + e.getMessage());
        }
    }

    /**
     * Check if user is logged out by verifying home page loads
     *
     * @return true if logout was successful, false otherwise
     */
    public boolean isLogoutSuccessful() {
        try {
            logger.info("Verifying if logout was successful");
            HomePage homePage = new HomePage(driver);
            boolean isLoaded = homePage.isHomePageLoaded();
            logger.info("Logout verification result: " + isLoaded);
            return isLoaded;
        } catch (Exception e) {
            logger.error("Error verifying logout: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get account menu button visibility
     *
     * @return true if account menu button is visible, false otherwise
     */
    public boolean isAccountMenuVisible() {
        try {
            logger.info("Checking if account menu is visible");
            return accountMenuButton.isDisplayed();
        } catch (Exception e) {
            logger.error("Error checking account menu visibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Access "Your Account" option from menu
     *
     * @return true if option is accessible, false otherwise
     */
    public boolean accessYourAccount() {
        try {
            logger.info("Accessing Your Account option");

            // Hover over account menu
            Actions actions = new Actions(driver);
            actions.moveToElement(accountMenuButton).perform();
            logger.info("Hovered over account menu");

            Thread.sleep(1000);
            WaitUtils.waitForVisibility(driver, yourAccountOption, 10);
            yourAccountOption.click();
            logger.info("Your Account option clicked");

            Thread.sleep(2000);
            return true;

        } catch (Exception e) {
            logger.error("Error accessing Your Account: " + e.getMessage());
            return false;
        }
    }
}

