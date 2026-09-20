package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utilities.WaitUtils;

/**
 * ProductPage.java
 * Purpose: Page Object Model for Amazon Product Details Page
 * Contains locators and methods for product interactions
 */
public class ProductPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(ProductPage.class);

    // Locators using @FindBy annotation
    @FindBy(id = "productTitle")
    private WebElement productTitle;

    @FindBy(xpath = "//span[@class='a-price-whole']")
    private WebElement productPrice;

    @FindBy(id = "quantity")
    private WebElement quantityDropdown;

    @FindBy(id = "add-to-cart-button")
    private WebElement addToCartButton;

    @FindBy(xpath = "//h1[@class='a-size-base']")
    private WebElement pageTitle;

    @FindBy(xpath = "//span[@id='acrCustomerReviewText']")
    private WebElement reviewsCount;

    @FindBy(xpath = "//span[@class='a-icon-star']")
    private WebElement starRating;

    @FindBy(xpath = "//span[@id='priceblock_dealprice']")
    private WebElement dealPrice;

    /**
     * Constructor to initialize PageFactory elements
     *
     * @param driver WebDriver instance
     */
    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("ProductPage initialized");
    }

    /**
     * Get product title
     *
     * @return product title text
     */
    public String getProductTitle() {
        try {
            logger.info("Getting product title");
            WaitUtils.waitForVisibility(driver, productTitle, 10);
            String title = productTitle.getText();
            logger.info("Product title: " + title);
            return title;
        } catch (Exception e) {
            logger.error("Error getting product title: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get product price
     *
     * @return product price as string
     */
    public String getProductPrice() {
        try {
            logger.info("Getting product price");
            WaitUtils.waitForVisibility(driver, productPrice, 10);
            String price = productPrice.getText();
            logger.info("Product price: " + price);
            return price;
        } catch (Exception e) {
            logger.error("Error getting product price: " + e.getMessage());
            return "";
        }
    }

    /**
     * Verify if product title is displayed
     *
     * @return true if title is visible, false otherwise
     */
    public boolean isProductTitleDisplayed() {
        try {
            logger.info("Verifying if product title is displayed");
            return productTitle.isDisplayed();
        } catch (Exception e) {
            logger.error("Error verifying product title: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify if product price is displayed
     *
     * @return true if price is visible, false otherwise
     */
    public boolean isProductPriceDisplayed() {
        try {
            logger.info("Verifying if product price is displayed");
            return productPrice.isDisplayed();
        } catch (Exception e) {
            logger.error("Error verifying product price: " + e.getMessage());
            return false;
        }
    }

    /**
     * Select quantity
     *
     * @param quantity quantity to select
     */
    public void selectQuantity(String quantity) {
        try {
            logger.info("Selecting quantity: " + quantity);
            WaitUtils.waitForVisibility(driver, quantityDropdown, 10);

            Select select = new Select(quantityDropdown);
            select.selectByValue(quantity);
            logger.info("Quantity selected: " + quantity);

        } catch (Exception e) {
            logger.error("Error selecting quantity: " + e.getMessage());
            throw new RuntimeException("Failed to select quantity: " + e.getMessage());
        }
    }

    /**
     * Add product to cart
     *
     * @return CartPage object after adding to cart
     */
    public CartPage addToCart() {
        try {
            logger.info("Adding product to cart");
            WaitUtils.waitForClickability(driver, addToCartButton, 10);
            addToCartButton.click();
            logger.info("Product added to cart");

            Thread.sleep(2000); // Wait for cart update
            return new CartPage(driver);

        } catch (Exception e) {
            logger.error("Error adding product to cart: " + e.getMessage());
            throw new RuntimeException("Failed to add product to cart: " + e.getMessage());
        }
    }

    /**
     * Add product to cart with quantity
     *
     * @param quantity quantity to add
     * @return CartPage object after adding to cart
     */
    public CartPage addToCartWithQuantity(String quantity) {
        try {
            logger.info("Adding product to cart with quantity: " + quantity);
            selectQuantity(quantity);
            Thread.sleep(500);
            return addToCart();

        } catch (Exception e) {
            logger.error("Error adding product to cart with quantity: " + e.getMessage());
            throw new RuntimeException("Failed to add to cart with quantity: " + e.getMessage());
        }
    }

    /**
     * Check if add to cart button is enabled
     *
     * @return true if button is enabled, false otherwise
     */
    public boolean isAddToCartButtonEnabled() {
        try {
            logger.info("Checking if add to cart button is enabled");
            return addToCartButton.isEnabled();
        } catch (Exception e) {
            logger.error("Error checking add to cart button: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get reviews count
     *
     * @return reviews count as string
     */
    public String getReviewsCount() {
        try {
            logger.info("Getting reviews count");
            String count = reviewsCount.getText();
            logger.info("Reviews count: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting reviews count: " + e.getMessage());
            return "0";
        }
    }

    /**
     * Get star rating
     *
     * @return star rating as string
     */
    public String getStarRating() {
        try {
            logger.info("Getting star rating");
            String rating = starRating.getText();
            logger.info("Star rating: " + rating);
            return rating;
        } catch (Exception e) {
            logger.error("Error getting star rating: " + e.getMessage());
            return "0";
        }
    }
}

