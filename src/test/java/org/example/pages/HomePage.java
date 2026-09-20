package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utilities.WaitUtils;

/**
 * HomePage.java
 * Purpose: Page Object Model for Amazon Home Page
 * Contains locators and methods for home page interactions
 */
public class HomePage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(HomePage.class);

    // Locators using @FindBy annotation (PageFactory)
    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;

    @FindBy(xpath = "//input[@value='Go']")
    private WebElement searchButton;

    @FindBy(id = "nav-cart-count")
    private WebElement cartIcon;

    @FindBy(id = "nav-link-accountList")
    private WebElement signInButton;

    @FindBy(xpath = "//h2[contains(text(), 'Sign in')]")
    private WebElement signInHeader;

    @FindBy(id = "nav-logo-sprites")
    private WebElement amazonLogo;

    /**
     * Constructor to initialize PageFactory elements
     *
     * @param driver WebDriver instance
     */
    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("HomePage initialized");
    }

    /**
     * Search for a product on home page
     *
     * @param productName name of the product to search
     * @return SearchPage object after search
     */
    public SearchPage searchProduct(String productName) {
        try {
            logger.info("Searching for product: " + productName);
            WaitUtils.waitForVisibility(driver, searchBox, 10);
            searchBox.clear();
            searchBox.sendKeys(productName);
            logger.info("Product name entered in search box");

            searchButton.click();
            logger.info("Search button clicked");

            return new SearchPage(driver);
        } catch (Exception e) {
            logger.error("Error searching for product: " + e.getMessage());
            throw new RuntimeException("Failed to search product: " + e.getMessage());
        }
    }

    /**
     * Click on sign in button
     *
     * @return LoginPage object after click
     */
    public LoginPage clickSignIn() {
        try {
            logger.info("Clicking on sign in button");
            WaitUtils.waitForClickability(driver, signInButton, 10);
            signInButton.click();
            logger.info("Sign in button clicked");

            return new LoginPage(driver);
        } catch (Exception e) {
            logger.error("Error clicking sign in button: " + e.getMessage());
            throw new RuntimeException("Failed to click sign in: " + e.getMessage());
        }
    }

    /**
     * Click on cart icon
     *
     * @return CartPage object after click
     */
    public CartPage openCart() {
        try {
            logger.info("Clicking on cart icon");
            WaitUtils.waitForClickability(driver, cartIcon, 10);
            cartIcon.click();
            logger.info("Cart icon clicked");

            return new CartPage(driver);
        } catch (Exception e) {
            logger.error("Error clicking cart icon: " + e.getMessage());
            throw new RuntimeException("Failed to open cart: " + e.getMessage());
        }
    }

    /**
     * Verify if homepage is loaded
     *
     * @return true if home page is visible, false otherwise
     */
    public boolean isHomePageLoaded() {
        try {
            logger.info("Verifying if home page is loaded");
            WaitUtils.waitForVisibility(driver, amazonLogo, 10);
            logger.info("Home page is loaded");
            return true;
        } catch (Exception e) {
            logger.error("Home page not loaded: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get search box visibility status
     *
     * @return true if search box is visible, false otherwise
     */
    public boolean isSearchBoxVisible() {
        try {
            return searchBox.isDisplayed();
        } catch (Exception e) {
            logger.error("Error checking search box visibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get cart count
     *
     * @return cart item count as string
     */
    public String getCartCount() {
        try {
            logger.info("Getting cart count");
            String count = cartIcon.getText();
            logger.info("Cart count: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting cart count: " + e.getMessage());
            return "0";
        }
    }
}

