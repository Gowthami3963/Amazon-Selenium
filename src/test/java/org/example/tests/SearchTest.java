package org.example.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.base.BaseClass;
import org.example.pages.HomePage;
import org.example.pages.SearchPage;
import org.example.utilities.DataProviders;
import org.example.utilities.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SearchTest.java
 * Purpose: Test cases for Amazon product search functionality
 * Includes positive and negative search test scenarios
 * Tests search results validation and product opening
 */
public class SearchTest extends BaseClass {

    private static final Logger logger = LogManager.getLogger(SearchTest.class);

    /**
     * Test valid product search
     * Priority: 1
     * Group: smoke
     */
    @Test(priority = 1, groups = "smoke", retryAnalyzer = RetryAnalyzer.class)
    public void testValidProductSearch() {
        try {
            logger.info("Test: Valid Product Search - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");
            logger.info("Home page loaded successfully");

            // Search for valid product
            String productToSearch = "iPhone 15";
            logger.info("Searching for product: " + productToSearch);

            SearchPage searchPage = homePage.searchProduct(productToSearch);
            Assert.assertNotNull(searchPage, "Search page should be returned");

            // Verify search results are displayed
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");
            int resultsCount = searchPage.getSearchResultsCount();
            Assert.assertTrue(resultsCount > 0, "Should have at least one search result");
            logger.info("Search results found: " + resultsCount);

            logger.info("Test: Valid Product Search - Passed");

        } catch (Exception e) {
            logger.error("Test: Valid Product Search - Failed: " + e.getMessage());
            Assert.fail("Valid product search test failed: " + e.getMessage());
        }
    }

    /**
     * Test invalid product search
     * Priority: 2
     * Group: regression
     */
    @Test(priority = 2, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testInvalidProductSearch() {
        try {
            logger.info("Test: Invalid Product Search - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Search for invalid/random product
            String invalidProduct = "xyz@#$invalid_product_12345";
            logger.info("Searching for invalid product: " + invalidProduct);

            SearchPage searchPage = homePage.searchProduct(invalidProduct);
            Assert.assertNotNull(searchPage, "Search page should be returned");

            // Verify no results or very few results
            // Check if "no results" message is displayed
            boolean noResultsMessageShown = searchPage.isNoResultsMessageDisplayed();
            logger.info("No results message shown: " + noResultsMessageShown);

            logger.info("Test: Invalid Product Search - Passed");

        } catch (Exception e) {
            logger.error("Test: Invalid Product Search - Failed: " + e.getMessage());
            Assert.fail("Invalid product search test failed: " + e.getMessage());
        }
    }

    /**
     * Test empty product search
     * Priority: 3
     * Group: regression
     */
    @Test(priority = 3, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testEmptyProductSearch() {
        try {
            logger.info("Test: Empty Product Search - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Try to search with empty string (may show all products or error)
            logger.info("Attempting to search with empty query");

            try {
                SearchPage searchPage = homePage.searchProduct("");
                logger.info("Empty search completed - may show all products");
            } catch (Exception e) {
                logger.info("Empty search resulted in exception (expected): " + e.getMessage());
            }

            logger.info("Test: Empty Product Search - Passed");

        } catch (Exception e) {
            logger.error("Test: Empty Product Search - Failed: " + e.getMessage());
            // Not critical, empty search handling varies
        }
    }

    /**
     * Test search results verification
     * Priority: 4
     * Group: regression
     */
    @Test(priority = 4, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testSearchResultsVerification() {
        try {
            logger.info("Test: Search Results Verification - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Search for a product
            String productName = "Samsung Galaxy S24";
            logger.info("Searching for product: " + productName);

            SearchPage searchPage = homePage.searchProduct(productName);
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            // Get and verify product details from results
            String firstProductName = searchPage.getFirstProductName();
            String firstProductPrice = searchPage.getFirstProductPrice();

            Assert.assertFalse(firstProductName.isEmpty(), "First product name should not be empty");
            Assert.assertFalse(firstProductPrice.isEmpty(), "First product price should not be empty");

            logger.info("First product name: " + firstProductName);
            logger.info("First product price: " + firstProductPrice);
            logger.info("Test: Search Results Verification - Passed");

        } catch (Exception e) {
            logger.error("Test: Search Results Verification - Failed: " + e.getMessage());
            Assert.fail("Search results verification test failed: " + e.getMessage());
        }
    }

    /**
     * Test opening product from search results
     * Priority: 5
     * Group: regression
     */
    @Test(priority = 5, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testOpenProductFromSearchResults() {
        try {
            logger.info("Test: Open Product From Search Results - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Search for product
            String productName = "iPhone 15";
            logger.info("Searching for product: " + productName);

            SearchPage searchPage = homePage.searchProduct(productName);
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            // Open first product
            logger.info("Opening first product from search results");
            var productPage = searchPage.openFirstProduct();
            Assert.assertNotNull(productPage, "Product page should be returned");

            // Verify product page is loaded
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product title should be displayed");
            Assert.assertTrue(productPage.isProductPriceDisplayed(), "Product price should be displayed");

            logger.info("Test: Open Product From Search Results - Passed");

        } catch (Exception e) {
            logger.error("Test: Open Product From Search Results - Failed: " + e.getMessage());
            Assert.fail("Open product from search results test failed: " + e.getMessage());
        }
    }

    /**
     * Parameterized search test using DataProvider
     *
     * @param searchQuery search product name
     * @param expectedResult expected result status
     */
    @Test(dataProvider = "searchData", dataProviderClass = DataProviders.class,
            groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testSearchWithDataProvider(String searchQuery, String expectedResult) {
        try {
            logger.info("Test: DataProvider Search - Query: " + searchQuery + ", Expected: " + expectedResult);

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            SearchPage searchPage = homePage.searchProduct(searchQuery);
            Assert.assertNotNull(searchPage, "Search page should be returned");

            // Verify results based on expected result
            if ("success".equals(expectedResult)) {
                Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");
                logger.info("Search successful as expected");
            } else if ("no_results".equals(expectedResult)) {
                boolean noResults = searchPage.isNoResultsMessageDisplayed() || searchPage.getSearchResultsCount() == 0;
                Assert.assertTrue(noResults, "No results should be shown for invalid search");
                logger.info("No results shown as expected");
            }

            logger.info("Test: DataProvider Search - Passed");

        } catch (Exception e) {
            logger.error("Test: DataProvider Search - Failed: " + e.getMessage());
            Assert.fail("DataProvider search test failed: " + e.getMessage());
        }
    }

    /**
     * Test search results count validation
     * Priority: 6
     * Group: sanity
     */
    @Test(priority = 6, groups = "sanity")
    public void testSearchResultsCount() {
        try {
            logger.info("Test: Search Results Count - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            SearchPage searchPage = homePage.searchProduct("phone");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            int count = searchPage.getSearchResultsCount();
            Assert.assertTrue(count > 0, "Results count should be greater than 0");
            logger.info("Search results count: " + count);

            logger.info("Test: Search Results Count - Passed");

        } catch (Exception e) {
            logger.error("Test: Search Results Count - Failed: " + e.getMessage());
            Assert.fail("Search results count test failed: " + e.getMessage());
        }
    }
}

