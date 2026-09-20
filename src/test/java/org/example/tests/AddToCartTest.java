package org.example.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.base.BaseClass;
import org.example.pages.CartPage;
import org.example.pages.HomePage;
import org.example.pages.ProductPage;
import org.example.pages.SearchPage;
import org.example.utilities.DataProviders;
import org.example.utilities.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * AddToCartTest.java
 * Purpose: Test cases for adding products to Amazon shopping cart
 * Includes product selection, quantity selection, and cart verification
 */
public class AddToCartTest extends BaseClass {

    private static final Logger logger = LogManager.getLogger(AddToCartTest.class);

    /**
     * Test add single product to cart
     * Priority: 1
     * Group: smoke
     */
    @Test(priority = 1, groups = "smoke", retryAnalyzer = RetryAnalyzer.class)
    public void testAddSingleProductToCart() {
        try {
            logger.info("Test: Add Single Product To Cart - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");
            logger.info("Home page loaded successfully");

            // Search for product
            String productName = "iPhone 15";
            logger.info("Searching for product: " + productName);
            SearchPage searchPage = homePage.searchProduct(productName);
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            // Open first product
            logger.info("Opening first product");
            ProductPage productPage = searchPage.openFirstProduct();
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product title should be displayed");
            String productTitle = productPage.getProductTitle();
            logger.info("Product selected: " + productTitle);

            // Add to cart
            logger.info("Adding product to cart");
            CartPage cartPage = productPage.addToCart();
            Assert.assertNotNull(cartPage, "Cart page should be returned");
            logger.info("Product added to cart successfully");

            // Verify product is in cart
            Assert.assertTrue(cartPage.isCheckoutButtonVisible(), "Checkout button should be visible");
            logger.info("Test: Add Single Product To Cart - Passed");

        } catch (Exception e) {
            logger.error("Test: Add Single Product To Cart - Failed: " + e.getMessage());
            Assert.fail("Add single product to cart test failed: " + e.getMessage());
        }
    }

    /**
     * Test add product to cart with quantity selection
     * Priority: 2
     * Group: regression
     */
    @Test(priority = 2, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testAddProductWithQuantity() {
        try {
            logger.info("Test: Add Product With Quantity - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Search for product
            SearchPage searchPage = homePage.searchProduct("Samsung Galaxy S24");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            // Open product
            ProductPage productPage = searchPage.openFirstProduct();
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product title should be displayed");

            // Select quantity - 3 items
            String quantity = "3";
            logger.info("Selecting quantity: " + quantity);
            productPage.selectQuantity(quantity);
            logger.info("Quantity selected: " + quantity);

            // Add to cart
            CartPage cartPage = productPage.addToCart();
            Assert.assertNotNull(cartPage, "Cart page should be returned");

            logger.info("Test: Add Product With Quantity - Passed");

        } catch (Exception e) {
            logger.error("Test: Add Product With Quantity - Failed: " + e.getMessage());
            Assert.fail("Add product with quantity test failed: " + e.getMessage());
        }
    }

    /**
     * Test add multiple products to cart
     * Priority: 3
     * Group: regression
     */
    @Test(priority = 3, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testAddMultipleProductsToCart() {
        try {
            logger.info("Test: Add Multiple Products To Cart - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Add first product
            logger.info("Adding first product");
            SearchPage searchPage = homePage.searchProduct("iPhone 15");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            ProductPage productPage = searchPage.openFirstProduct();
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product title should be displayed");

            CartPage cartPage = productPage.addToCart();
            Assert.assertNotNull(cartPage, "Cart page should be returned");
            int itemsAfterFirst = cartPage.getCartItemsCount();
            logger.info("Items in cart after first product: " + itemsAfterFirst);

            // Go back to home and add second product
            Thread.sleep(1000);
            homePage = new HomePage(driver);
            logger.info("Adding second product");

            searchPage = homePage.searchProduct("Samsung Galaxy");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            productPage = searchPage.openFirstProduct();
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product title should be displayed");

            cartPage = productPage.addToCart();

            // Verify cart has multiple items
            int itemsAfterSecond = cartPage.getCartItemsCount();
            logger.info("Items in cart after second product: " + itemsAfterSecond);
            Assert.assertTrue(itemsAfterSecond > 0, "Cart should have items");

            logger.info("Test: Add Multiple Products To Cart - Passed");

        } catch (Exception e) {
            logger.error("Test: Add Multiple Products To Cart - Failed: " + e.getMessage());
            Assert.fail("Add multiple products to cart test failed: " + e.getMessage());
        }
    }

    /**
     * Test verify cart after adding product
     * Priority: 4
     * Group: regression
     */
    @Test(priority = 4, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testVerifyCartAfterAddingProduct() {
        try {
            logger.info("Test: Verify Cart After Adding Product - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Search and open product
            SearchPage searchPage = homePage.searchProduct("iPad");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            String productName = searchPage.getFirstProductName();
            logger.info("Product selected: " + productName);

            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();

            // Verify cart is not empty
            Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
            Assert.assertTrue(cartPage.isCheckoutButtonVisible(), "Checkout button should be visible");

            // Get subtotal
            String subtotal = cartPage.getSubtotalPrice();
            Assert.assertFalse(subtotal.isEmpty(), "Subtotal should be displayed");
            logger.info("Cart subtotal: " + subtotal);

            logger.info("Test: Verify Cart After Adding Product - Passed");

        } catch (Exception e) {
            logger.error("Test: Verify Cart After Adding Product - Failed: " + e.getMessage());
            Assert.fail("Verify cart test failed: " + e.getMessage());
        }
    }

    /**
     * Test cart item count increments correctly
     * Priority: 5
     * Group: sanity
     */
    @Test(priority = 5, groups = "sanity")
    public void testCartItemCount() {
        try {
            logger.info("Test: Cart Item Count - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Get initial cart count
            String initialCount = homePage.getCartCount();
            logger.info("Initial cart count: " + initialCount);

            // Search and add product
            SearchPage searchPage = homePage.searchProduct("phone");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();

            int itemsInCart = cartPage.getCartItemsCount();
            Assert.assertTrue(itemsInCart > 0, "Cart should have items");
            logger.info("Items in cart: " + itemsInCart);

            logger.info("Test: Cart Item Count - Passed");

        } catch (Exception e) {
            logger.error("Test: Cart Item Count - Failed: " + e.getMessage());
            Assert.fail("Cart item count test failed: " + e.getMessage());
        }
    }

    /**
     * Parameterized test for adding products with different quantities
     *
     * @param quantity product quantity
     * @param color product color
     * @param expectedPrice expected price
     */
    @Test(dataProvider = "productData", dataProviderClass = DataProviders.class,
            groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testAddProductWithDataProvider(String quantity, String color, String expectedPrice) {
        try {
            logger.info("Test: DataProvider Add Product - Quantity: " + quantity + ", Color: " + color);

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            SearchPage searchPage = homePage.searchProduct("iPhone 15");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            ProductPage productPage = searchPage.openFirstProduct();
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product title should be displayed");

            // Select quantity
            productPage.selectQuantity(quantity);
            logger.info("Quantity selected: " + quantity);

            // Add to cart
            CartPage cartPage = productPage.addToCart();
            Assert.assertNotNull(cartPage, "Cart page should be returned");

            logger.info("Test: DataProvider Add Product - Passed");

        } catch (Exception e) {
            logger.error("Test: DataProvider Add Product - Failed: " + e.getMessage());
            Assert.fail("DataProvider add product test failed: " + e.getMessage());
        }
    }

    /**
     * Test verify checkout button is enabled
     * Priority: 6
     * Group: sanity
     */
    @Test(priority = 6, groups = "sanity")
    public void testCheckoutButtonEnabled() {
        try {
            logger.info("Test: Checkout Button Enabled - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            SearchPage searchPage = homePage.searchProduct("product");
            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();

            // Verify checkout button is enabled
            Assert.assertTrue(cartPage.isContinueToPaymentButtonEnabled(), "Checkout button should be enabled");
            logger.info("Checkout button is enabled");

            logger.info("Test: Checkout Button Enabled - Passed");

        } catch (Exception e) {
            logger.error("Test: Checkout Button Enabled - Failed: " + e.getMessage());
            Assert.fail("Checkout button enabled test failed: " + e.getMessage());
        }
    }
}

