package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utilities.WaitUtils;
import java.util.List;

/**
 * SearchPage.java
 * Purpose: Page Object Model for Amazon Search Results Page
 * Contains locators and methods for search result interactions
 */
public class SearchPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(SearchPage.class);

    // Locators using @FindBy annotation
    @FindBy(xpath = "//h2[contains(@class, 's-size-mini')]")
    private List<WebElement> searchResults;

    @FindBy(xpath = "//h1[contains(text(), 'Results')]")
    private WebElement resultsHeader;

    @FindBy(xpath = "//span[@class='a-price-whole']")
    private List<WebElement> productPrices;

    @FindBy(xpath = "//div[@class='sg-col-inner']//h2//a")
    private List<WebElement> productLinks;

    @FindBy(xpath = "//div[@data-component-type='s-search-result']")
    private List<WebElement> productCards;

    @FindBy(xpath = "//span[contains(text(), 'No results')]")
    private WebElement noResultsMessage;

    /**
     * Constructor to initialize PageFactory elements
     *
     * @param driver WebDriver instance
     */
    public SearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("SearchPage initialized");
    }

    /**
     * Get number of search results
     *
     * @return count of search results
     */
    public int getSearchResultsCount() {
        try {
            logger.info("Getting search results count");
            int count = searchResults.size();
            logger.info("Search results count: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting search results count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Check if search results are displayed
     *
     * @return true if results are displayed, false otherwise
     */
    public boolean isSearchResultsDisplayed() {
        try {
            logger.info("Checking if search results are displayed");
            return getSearchResultsCount() > 0;
        } catch (Exception e) {
            logger.error("Error checking search results: " + e.getMessage());
            return false;
        }
    }

    /**
     * Open first product from search results
     *
     * @return ProductPage object after click
     */
    public ProductPage openFirstProduct() {
        try {
            logger.info("Opening first product from search results");

            if (!isSearchResultsDisplayed()) {
                throw new RuntimeException("No search results found");
            }

            // Wait for product links to be visible
            WaitUtils.waitForVisibility(driver, productLinks.get(0), 10);
            productLinks.get(0).click();
            logger.info("First product clicked");

            Thread.sleep(2000); // Wait for product page to load
            return new ProductPage(driver);

        } catch (Exception e) {
            logger.error("Error opening first product: " + e.getMessage());
            throw new RuntimeException("Failed to open first product: " + e.getMessage());
        }
    }

    /**
     * Open product by index
     *
     * @param index index of product to open
     * @return ProductPage object after click
     */
    public ProductPage openProductByIndex(int index) {
        try {
            logger.info("Opening product at index: " + index);

            if (productLinks.size() <= index) {
                throw new RuntimeException("Product at index " + index + " not found");
            }

            WaitUtils.waitForVisibility(driver, productLinks.get(index), 10);
            productLinks.get(index).click();
            logger.info("Product at index " + index + " clicked");

            Thread.sleep(2000);
            return new ProductPage(driver);

        } catch (Exception e) {
            logger.error("Error opening product by index: " + e.getMessage());
            throw new RuntimeException("Failed to open product: " + e.getMessage());
        }
    }

    /**
     * Search for product by name in results
     *
     * @param productName name of product to search
     * @return ProductPage object if found, null otherwise
     */
    public ProductPage searchAndOpenProduct(String productName) {
        try {
            logger.info("Searching for product: " + productName + " in results");

            for (WebElement product : productLinks) {
                if (product.getText().toLowerCase().contains(productName.toLowerCase())) {
                    product.click();
                    logger.info("Product '" + productName + "' found and clicked");
                    Thread.sleep(2000);
                    return new ProductPage(driver);
                }
            }

            logger.warn("Product '" + productName + "' not found in search results");
            return null;

        } catch (Exception e) {
            logger.error("Error searching for product: " + e.getMessage());
            return null;
        }
    }

    /**
     * Check if no results message is displayed
     *
     * @return true if no results message is shown, false otherwise
     */
    public boolean isNoResultsMessageDisplayed() {
        try {
            logger.info("Checking if no results message is displayed");
            WaitUtils.waitForVisibility(driver, noResultsMessage, 5);
            logger.info("No results message displayed");
            return true;
        } catch (Exception e) {
            logger.info("No results message not found");
            return false;
        }
    }

    /**
     * Get first product price
     *
     * @return price of first product
     */
    public String getFirstProductPrice() {
        try {
            logger.info("Getting first product price");
            if (productPrices.isEmpty()) {
                logger.warn("No prices found");
                return "";
            }
            String price = productPrices.get(0).getText();
            logger.info("First product price: " + price);
            return price;
        } catch (Exception e) {
            logger.error("Error getting first product price: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get first product name
     *
     * @return name of first product
     */
    public String getFirstProductName() {
        try {
            logger.info("Getting first product name");
            if (productLinks.isEmpty()) {
                logger.warn("No products found");
                return "";
            }
            String name = productLinks.get(0).getText();
            logger.info("First product name: " + name);
            return name;
        } catch (Exception e) {
            logger.error("Error getting first product name: " + e.getMessage());
            return "";
        }
    }
}

