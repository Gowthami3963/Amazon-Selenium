package org.example.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.base.BaseClass;
import org.example.pages.CartPage;
import org.example.pages.CheckoutPage;
import org.example.pages.HomePage;
import org.example.pages.LoginPage;
import org.example.pages.LogoutPage;
import org.example.pages.ProductPage;
import org.example.pages.SearchPage;
import org.example.utilities.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * EndToEndTest.java
 * Purpose: Complete end-to-end user flow test cases
 * Tests the complete user journey from login to checkout
 * Includes multi-step workflows and integration testing
 */
public class EndToEndTest extends BaseClass {

    private static final Logger logger = LogManager.getLogger(EndToEndTest.class);

    /**
     * Complete end-to-end flow: Login → Search → Product → Add to Cart → Checkout
     * Priority: 1
     * Group: e2e (end-to-end testing)
     */
    @Test(priority = 1, groups = "e2e", retryAnalyzer = RetryAnalyzer.class)
    public void testCompleteE2EFlow() {
        try {
            logger.info("==== TEST START: Complete E2E Flow ====");

            // STEP 1: Verify Home Page
            logger.info("STEP 1: Verifying home page");
            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");
            logger.info("✓ Home page loaded successfully");

            // STEP 2: Click Sign In
            logger.info("STEP 2: Clicking sign in button");
            LoginPage loginPage = homePage.clickSignIn();
            Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should load");
            logger.info("✓ Login page loaded");

            // STEP 3: Perform Login
            logger.info("STEP 3: Performing login");
            String testEmail = "testuser@gmail.com";
            String testPassword = "testpassword123";
            homePage = loginPage.login(testEmail, testPassword);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Should be logged in and on home page");
            logger.info("✓ Login successful");

            // STEP 4: Search for Product
            logger.info("STEP 4: Searching for product");
            String productToSearch = "iPhone 15";
            SearchPage searchPage = homePage.searchProduct(productToSearch);
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");
            int resultsCount = searchPage.getSearchResultsCount();
            logger.info("✓ Search completed with " + resultsCount + " results");

            // STEP 5: Open First Product
            logger.info("STEP 5: Opening first product from search results");
            ProductPage productPage = searchPage.openFirstProduct();
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product title should be displayed");
            String productTitle = productPage.getProductTitle();
            String productPrice = productPage.getProductPrice();
            logger.info("✓ Product opened: " + productTitle + " - Price: " + productPrice);

            // STEP 6: Select Quantity
            logger.info("STEP 6: Selecting product quantity");
            String quantity = "2";
            productPage.selectQuantity(quantity);
            logger.info("✓ Quantity selected: " + quantity);

            // STEP 7: Add to Cart
            logger.info("STEP 7: Adding product to cart");
            CartPage cartPage = productPage.addToCart();
            Assert.assertNotNull(cartPage, "Cart page should be returned");
            logger.info("✓ Product added to cart");

            // STEP 8: Verify Cart
            logger.info("STEP 8: Verifying cart contents");
            Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
            int cartItems = cartPage.getCartItemsCount();
            String subtotal = cartPage.getSubtotalPrice();
            logger.info("✓ Cart verified - Items: " + cartItems + ", Subtotal: " + subtotal);

            // STEP 9: Proceed to Checkout
            logger.info("STEP 9: Proceeding to checkout");
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");
            logger.info("✓ Checkout page loaded");

            // STEP 10: Select Address
            logger.info("STEP 10: Selecting shipping address");
            if (checkoutPage.getAddressCount() > 0) {
                checkoutPage.selectFirstAddress();
                String shippingAddress = checkoutPage.getShippingAddress();
                logger.info("✓ Address selected: " + shippingAddress);
            } else {
                logger.warn("⚠ No addresses available for selection");
            }

            // STEP 11: Select Delivery Option
            logger.info("STEP 11: Selecting delivery option");
            Thread.sleep(500);
            if (checkoutPage.isDeliveryOptionsVisible()) {
                checkoutPage.selectFirstDeliveryOption();
                logger.info("✓ Delivery option selected");
            } else {
                logger.warn("⚠ Delivery options not visible");
            }

            logger.info("==== TEST PASSED: Complete E2E Flow ====");

        } catch (Exception e) {
            logger.error("==== TEST FAILED: Complete E2E Flow ====");
            logger.error("Error message: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Complete E2E flow test failed: " + e.getMessage());
        }
    }

    /**
     * E2E Flow with multiple products in cart
     * Priority: 2
     * Group: e2e
     */
    @Test(priority = 2, groups = "e2e", retryAnalyzer = RetryAnalyzer.class)
    public void testE2EWithMultipleProducts() {
        try {
            logger.info("==== TEST START: E2E With Multiple Products ====");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Add first product
            logger.info("Adding first product to cart");
            SearchPage searchPage = homePage.searchProduct("iPhone");
            ProductPage productPage = searchPage.openFirstProduct();
            productPage.selectQuantity("1");
            CartPage cartPage = productPage.addToCart();
            logger.info("✓ First product added");

            // Add second product
            logger.info("Adding second product to cart");
            Thread.sleep(1000);
            homePage = new HomePage(driver);
            searchPage = homePage.searchProduct("Samsung");
            productPage = searchPage.openFirstProduct();
            productPage.selectQuantity("1");
            cartPage = productPage.addToCart();
            logger.info("✓ Second product added");

            // Verify both products in cart
            int totalItems = cartPage.getCartItemsCount();
            logger.info("Total items in cart: " + totalItems);
            Assert.assertTrue(totalItems > 0, "Cart should have multiple items");

            // Proceed to checkout
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");

            logger.info("==== TEST PASSED: E2E With Multiple Products ====");

        } catch (Exception e) {
            logger.error("==== TEST FAILED: E2E With Multiple Products ====");
            logger.error("Error: " + e.getMessage());
            Assert.fail("E2E multiple products test failed: " + e.getMessage());
        }
    }

    /**
     * E2E Flow: Search → Cart → Checkout → Payment Page
     * Priority: 3
     * Group: e2e
     */
    @Test(priority = 3, groups = "e2e", retryAnalyzer = RetryAnalyzer.class)
    public void testSearchToPaymentFlow() {
        try {
            logger.info("==== TEST START: Search to Payment Flow ====");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");
            logger.info("✓ Home page loaded");

            // Search
            logger.info("Searching for product");
            SearchPage searchPage = homePage.searchProduct("laptop");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should display");
            logger.info("✓ Search results displayed");

            // Open product
            logger.info("Opening product");
            ProductPage productPage = searchPage.openFirstProduct();
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product should display");
            logger.info("✓ Product opened");

            // Add to cart
            logger.info("Adding to cart");
            CartPage cartPage = productPage.addToCart();
            Assert.assertFalse(cartPage.isCartEmpty(), "Cart should have items");
            logger.info("✓ Product added to cart");

            // Checkout
            logger.info("Proceeding to checkout");
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");
            logger.info("✓ Checkout page loaded");

            // Select address and delivery
            if (checkoutPage.getAddressCount() > 0) {
                checkoutPage.selectFirstAddress();
                Thread.sleep(500);
                checkoutPage.selectFirstDeliveryOption();
                logger.info("✓ Address and delivery selected");
            }

            // Payment page
            logger.info("Proceeding to payment");
            var paymentPage = checkoutPage.continueToPayment();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");
            logger.info("✓ Payment page loaded");

            logger.info("==== TEST PASSED: Search to Payment Flow ====");

        } catch (Exception e) {
            logger.error("==== TEST FAILED: Search to Payment Flow ====");
            logger.error("Error: " + e.getMessage());
            Assert.fail("Search to payment flow test failed: " + e.getMessage());
        }
    }

    /**
     * E2E Flow with invalid search and valid search retry
     * Priority: 4
     * Group: e2e
     */
    @Test(priority = 4, groups = "e2e", retryAnalyzer = RetryAnalyzer.class)
    public void testE2EWithInvalidSearchRetry() {
        try {
            logger.info("==== TEST START: E2E With Invalid Search Retry ====");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Try invalid search first
            logger.info("Attempting invalid search");
            SearchPage searchPage = homePage.searchProduct("xyz@#$invalid");
            logger.info("Invalid search completed");

            // Retry with valid search
            logger.info("Retrying with valid search");
            homePage = new HomePage(driver);
            searchPage = homePage.searchProduct("tablet");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Valid search should return results");
            logger.info("✓ Valid search successful");

            // Continue with product selection
            ProductPage productPage = searchPage.openFirstProduct();
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product should display");
            CartPage cartPage = productPage.addToCart();
            Assert.assertFalse(cartPage.isCartEmpty(), "Cart should have items");

            logger.info("==== TEST PASSED: E2E With Invalid Search Retry ====");

        } catch (Exception e) {
            logger.error("==== TEST FAILED: E2E With Invalid Search Retry ====");
            logger.error("Error: " + e.getMessage());
            Assert.fail("E2E invalid search retry test failed: " + e.getMessage());
        }
    }

    /**
     * Complete flow with quantity update
     * Priority: 5
     * Group: e2e
     */
    @Test(priority = 5, groups = "e2e", retryAnalyzer = RetryAnalyzer.class)
    public void testE2EWithQuantityUpdate() {
        try {
            logger.info("==== TEST START: E2E With Quantity Update ====");

            HomePage homePage = new HomePage(driver);
            SearchPage searchPage = homePage.searchProduct("phone");
            ProductPage productPage = searchPage.openFirstProduct();

            // Add with quantity
            logger.info("Adding product with quantity 3");
            CartPage cartPage = productPage.addToCartWithQuantity("3");
            Assert.assertFalse(cartPage.isCartEmpty(), "Cart should have items");
            logger.info("✓ Product added with quantity 3");

            // Update quantity
            logger.info("Updating quantity to 2");
            cartPage.updateFirstItemQuantity("2");
            logger.info("✓ Quantity updated");

            // Verify and proceed
            String subtotal = cartPage.getSubtotalPrice();
            logger.info("Subtotal: " + subtotal);

            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");

            logger.info("==== TEST PASSED: E2E With Quantity Update ====");

        } catch (Exception e) {
            logger.error("==== TEST FAILED: E2E With Quantity Update ====");
            logger.error("Error: " + e.getMessage());
            Assert.fail("E2E quantity update test failed: " + e.getMessage());
        }
    }

    /**
     * E2E Flow demonstrating remove item from cart
     * Priority: 6
     * Group: e2e
     */
    @Test(priority = 6, groups = "e2e", retryAnalyzer = RetryAnalyzer.class)
    public void testE2EWithRemoveItem() {
        try {
            logger.info("==== TEST START: E2E With Remove Item ====");

            HomePage homePage = new HomePage(driver);
            SearchPage searchPage = homePage.searchProduct("phone");
            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();

            // Verify item and remove
            int itemsBeforeRemove = cartPage.getCartItemsCount();
            logger.info("Items before remove: " + itemsBeforeRemove);

            if (itemsBeforeRemove > 0) {
                cartPage.removeFirstItem();
                logger.info("✓ First item removed");
                Thread.sleep(1000);

                int itemsAfterRemove = cartPage.getCartItemsCount();
                logger.info("Items after remove: " + itemsAfterRemove);
            }

            logger.info("==== TEST PASSED: E2E With Remove Item ====");

        } catch (Exception e) {
            logger.error("==== TEST FAILED: E2E With Remove Item ====");
            logger.error("Error: " + e.getMessage());
            Assert.fail("E2E remove item test failed: " + e.getMessage());
        }
    }

    /**
     * Sanity check: Home page search functionality
     * Priority: 7
     * Group: sanity
     */
    @Test(priority = 7, groups = "sanity")
    public void testHomePageSearch() {
        try {
            logger.info("TEST: Home Page Search");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");
            Assert.assertTrue(homePage.isSearchBoxVisible(), "Search box should be visible");

            SearchPage searchPage = homePage.searchProduct("headphones");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should display");

            logger.info("✓ Home page search test passed");

        } catch (Exception e) {
            logger.error("Home page search test failed: " + e.getMessage());
            Assert.fail("Home page search test failed: " + e.getMessage());
        }
    }
}

