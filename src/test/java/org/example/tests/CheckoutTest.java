package org.example.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.base.BaseClass;
import org.example.pages.CartPage;
import org.example.pages.CheckoutPage;
import org.example.pages.HomePage;
import org.example.pages.ProductPage;
import org.example.pages.SearchPage;
import org.example.utilities.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * CheckoutTest.java
 * Purpose: Test cases for Amazon checkout functionality
 * Tests address selection, delivery options, and checkout flow
 */
public class CheckoutTest extends BaseClass {

    private static final Logger logger = LogManager.getLogger(CheckoutTest.class);

    /**
     * Test checkout page load
     * Priority: 1
     * Group: smoke
     */
    @Test(priority = 1, groups = "smoke", retryAnalyzer = RetryAnalyzer.class)
    public void testCheckoutPageLoad() {
        try {
            logger.info("Test: Checkout Page Load - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Add product to cart and proceed
            SearchPage searchPage = homePage.searchProduct("phone");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();
            Assert.assertNotNull(cartPage, "Cart page should be returned");

            // Proceed to checkout
            logger.info("Proceeding to checkout");
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");

            logger.info("Test: Checkout Page Load - Passed");

        } catch (Exception e) {
            logger.error("Test: Checkout Page Load - Failed: " + e.getMessage());
            Assert.fail("Checkout page load test failed: " + e.getMessage());
        }
    }

    /**
     * Test address selection on checkout
     * Priority: 2
     * Group: regression
     */
    @Test(priority = 2, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testAddressSelection() {
        try {
            logger.info("Test: Address Selection - Started");

            // Setup cart and navigate to checkout
            HomePage homePage = new HomePage(driver);
            SearchPage searchPage = homePage.searchProduct("product");
            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");

            // Check available addresses
            int addressCount = checkoutPage.getAddressCount();
            logger.info("Available addresses: " + addressCount);

            if (addressCount > 0) {
                // Select first address
                logger.info("Selecting first address");
                checkoutPage.selectFirstAddress();
                logger.info("First address selected");

                // Verify shipping address is set
                String shippingAddress = checkoutPage.getShippingAddress();
                Assert.assertFalse(shippingAddress.isEmpty(), "Shipping address should be set");
                logger.info("Shipping address: " + shippingAddress);
            } else {
                logger.warn("No addresses available in checkout");
            }

            logger.info("Test: Address Selection - Passed");

        } catch (Exception e) {
            logger.error("Test: Address Selection - Failed: " + e.getMessage());
            Assert.fail("Address selection test failed: " + e.getMessage());
        }
    }

    /**
     * Test delivery options selection
     * Priority: 3
     * Group: regression
     */
    @Test(priority = 3, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testDeliveryOptionsSelection() {
        try {
            logger.info("Test: Delivery Options Selection - Started");

            // Navigate to checkout
            HomePage homePage = new HomePage(driver);
            SearchPage searchPage = homePage.searchProduct("product");
            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");

            // Select address if available
            if (checkoutPage.getAddressCount() > 0) {
                checkoutPage.selectFirstAddress();
                logger.info("Address selected");
            }

            // Verify delivery options are visible
            Thread.sleep(1000);
            boolean deliveryOptionsVisible = checkoutPage.isDeliveryOptionsVisible();
            logger.info("Delivery options visible: " + deliveryOptionsVisible);

            if (deliveryOptionsVisible) {
                // Select first delivery option
                logger.info("Selecting first delivery option");
                checkoutPage.selectFirstDeliveryOption();
                logger.info("Delivery option selected");
            }

            logger.info("Test: Delivery Options Selection - Passed");

        } catch (Exception e) {
            logger.error("Test: Delivery Options Selection - Failed: " + e.getMessage());
            Assert.fail("Delivery options selection test failed: " + e.getMessage());
        }
    }

    /**
     * Test proceed to payment from checkout
     * Priority: 4
     * Group: regression
     */
    @Test(priority = 4, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testProceedToPayment() {
        try {
            logger.info("Test: Proceed To Payment - Started");

            // Navigate to checkout
            HomePage homePage = new HomePage(driver);
            SearchPage searchPage = homePage.searchProduct("product");
            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");

            // Select address if available
            if (checkoutPage.getAddressCount() > 0) {
                checkoutPage.selectFirstAddress();
                checkoutPage.selectFirstDeliveryOption();
                Thread.sleep(1000);
            }

            // Verify continue to payment button is enabled
            Assert.assertTrue(checkoutPage.isContinueToPaymentButtonEnabled(),
                    "Continue to payment button should be enabled");
            logger.info("Continue to payment button is enabled");

            // Proceed to payment
            logger.info("Proceeding to payment");
            var paymentPage = checkoutPage.continueToPayment();
            Assert.assertNotNull(paymentPage, "Payment page should be returned");

            logger.info("Test: Proceed To Payment - Passed");

        } catch (Exception e) {
            logger.error("Test: Proceed To Payment - Failed: " + e.getMessage());
            Assert.fail("Proceed to payment test failed: " + e.getMessage());
        }
    }

    /**
     * Test checkout flow with address and delivery
     * Priority: 5
     * Group: regression
     */
    @Test(priority = 5, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testCompleteCheckoutFlow() {
        try {
            logger.info("Test: Complete Checkout Flow - Started");

            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            // Search and add product
            logger.info("Searching for product");
            SearchPage searchPage = homePage.searchProduct("smartphone");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            // Open product
            logger.info("Opening product");
            ProductPage productPage = searchPage.openFirstProduct();
            Assert.assertTrue(productPage.isProductTitleDisplayed(), "Product title should be displayed");

            // Add to cart
            logger.info("Adding to cart");
            CartPage cartPage = productPage.addToCart();
            Assert.assertFalse(cartPage.isCartEmpty(), "Cart should have items");

            // Proceed to checkout
            logger.info("Proceeding to checkout");
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");

            // Select address
            if (checkoutPage.getAddressCount() > 0) {
                logger.info("Selecting address");
                checkoutPage.selectFirstAddress();

                // Select delivery option
                Thread.sleep(500);
                if (checkoutPage.isDeliveryOptionsVisible()) {
                    logger.info("Selecting delivery option");
                    checkoutPage.selectFirstDeliveryOption();
                }
            }

            logger.info("Checkout flow completed successfully");
            logger.info("Test: Complete Checkout Flow - Passed");

        } catch (Exception e) {
            logger.error("Test: Complete Checkout Flow - Failed: " + e.getMessage());
            Assert.fail("Complete checkout flow test failed: " + e.getMessage());
        }
    }

    /**
     * Test verify shipping address info
     * Priority: 6
     * Group: sanity
     */
    @Test(priority = 6, groups = "sanity")
    public void testShippingAddressInfo() {
        try {
            logger.info("Test: Shipping Address Info - Started");

            HomePage homePage = new HomePage(driver);
            SearchPage searchPage = homePage.searchProduct("product");
            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();
            CheckoutPage checkoutPage = cartPage.proceedToCheckout();

            if (checkoutPage.getAddressCount() > 0) {
                checkoutPage.selectFirstAddress();
                String address = checkoutPage.getShippingAddress();
                Assert.assertFalse(address.isEmpty(), "Shipping address should not be empty");
                logger.info("Shipping address: " + address);
            }

            logger.info("Test: Shipping Address Info - Passed");

        } catch (Exception e) {
            logger.error("Test: Shipping Address Info - Failed: " + e.getMessage());
            Assert.fail("Shipping address info test failed: " + e.getMessage());
        }
    }
}

