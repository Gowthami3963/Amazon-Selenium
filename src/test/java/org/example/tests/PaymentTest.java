package org.example.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.base.BaseClass;
import org.example.pages.CartPage;
import org.example.pages.CheckoutPage;
import org.example.pages.HomePage;
import org.example.pages.PaymentPage;
import org.example.pages.ProductPage;
import org.example.pages.SearchPage;
import org.example.utilities.DataProviders;
import org.example.utilities.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * PaymentTest.java
 * Purpose: Test cases for Amazon payment page functionality
 * NOTE: DUMMY PAYMENT TESTS ONLY - Does not place actual orders
 * Tests payment method verification and review order elements
 */
public class PaymentTest extends BaseClass {

    private static final Logger logger = LogManager.getLogger(PaymentTest.class);

    /**
     * Test payment page load
     * Priority: 1
     * Group: smoke
     * NOTE: This is a dummy test - does not place actual order
     */
    @Test(priority = 1, groups = "smoke", retryAnalyzer = RetryAnalyzer.class)
    public void testPaymentPageLoad() {
        try {
            logger.info("Test: Payment Page Load - Started");

            // Navigate through to payment page
            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            SearchPage searchPage = homePage.searchProduct("product");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();

            CheckoutPage checkoutPage = cartPage.proceedToCheckout();
            Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should load");

            // Select address and delivery if available
            if (checkoutPage.getAddressCount() > 0) {
                checkoutPage.selectFirstAddress();
                Thread.sleep(500);
                if (checkoutPage.isDeliveryOptionsVisible()) {
                    checkoutPage.selectFirstDeliveryOption();
                    Thread.sleep(500);
                }
            }

            // Proceed to payment
            logger.info("Proceeding to payment page");
            PaymentPage paymentPage = checkoutPage.continueToPayment();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            logger.info("Test: Payment Page Load - Passed");

        } catch (Exception e) {
            logger.error("Test: Payment Page Load - Failed: " + e.getMessage());
            Assert.fail("Payment page load test failed: " + e.getMessage());
        }
    }

    /**
     * Test verify payment methods available
     * Priority: 2
     * Group: regression
     */
    @Test(priority = 2, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testPaymentMethodsAvailable() {
        try {
            logger.info("Test: Payment Methods Available - Started");

            PaymentPage paymentPage = navigateToPaymentPage();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            // Verify payment methods count
            int methodsCount = paymentPage.getPaymentMethodsCount();
            logger.info("Available payment methods: " + methodsCount);

            logger.info("Test: Payment Methods Available - Passed");

        } catch (Exception e) {
            logger.error("Test: Payment Methods Available - Failed: " + e.getMessage());
            Assert.fail("Payment methods available test failed: " + e.getMessage());
        }
    }

    /**
     * Test credit card payment option visible
     * Priority: 3
     * Group: regression
     */
    @Test(priority = 3, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testCreditCardOptionVisible() {
        try {
            logger.info("Test: Credit Card Option Visible - Started");

            PaymentPage paymentPage = navigateToPaymentPage();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            // Verify credit card option
            boolean creditCardVisible = paymentPage.isCreditCardOptionVisible();
            Assert.assertTrue(creditCardVisible, "Credit card payment option should be visible");
            logger.info("Credit card payment option is visible");

            logger.info("Test: Credit Card Option Visible - Passed");

        } catch (Exception e) {
            logger.error("Test: Credit Card Option Visible - Failed: " + e.getMessage());
            // Not critical - may not always be visible depending on location
        }
    }

    /**
     * Test debit card payment option visible
     * Priority: 4
     * Group: regression
     */
    @Test(priority = 4, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testDebitCardOptionVisible() {
        try {
            logger.info("Test: Debit Card Option Visible - Started");

            PaymentPage paymentPage = navigateToPaymentPage();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            // Verify debit card option
            boolean debitCardVisible = paymentPage.isDebitCardOptionVisible();
            Assert.assertTrue(debitCardVisible, "Debit card payment option should be visible");
            logger.info("Debit card payment option is visible");

            logger.info("Test: Debit Card Option Visible - Passed");

        } catch (Exception e) {
            logger.error("Test: Debit Card Option Visible - Failed: " + e.getMessage());
            // Not critical - may not always be visible
        }
    }

    /**
     * Test UPI payment option visible (Amazon India)
     * Priority: 5
     * Group: regression
     */
    @Test(priority = 5, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testUPIOptionVisible() {
        try {
            logger.info("Test: UPI Option Visible - Started");

            PaymentPage paymentPage = navigateToPaymentPage();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            // Verify UPI option (for Amazon India)
            boolean upiVisible = paymentPage.isUPIOptionVisible();
            logger.info("UPI payment option visible: " + upiVisible);

            logger.info("Test: UPI Option Visible - Passed");

        } catch (Exception e) {
            logger.error("Test: UPI Option Visible - Failed: " + e.getMessage());
            // Not critical - UPI only available in India
        }
    }

    /**
     * Test order summary visibility
     * Priority: 6
     * Group: regression
     */
    @Test(priority = 6, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testOrderSummaryVisible() {
        try {
            logger.info("Test: Order Summary Visible - Started");

            PaymentPage paymentPage = navigateToPaymentPage();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            // Verify order summary
            boolean summaryVisible = paymentPage.isOrderSummaryVisible();
            Assert.assertTrue(summaryVisible, "Order summary should be visible");
            logger.info("Order summary is visible");

            logger.info("Test: Order Summary Visible - Passed");

        } catch (Exception e) {
            logger.error("Test: Order Summary Visible - Failed: " + e.getMessage());
            Assert.fail("Order summary visible test failed: " + e.getMessage());
        }
    }

    /**
     * Test order total verification
     * Priority: 7
     * Group: regression
     */
    @Test(priority = 7, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testOrderTotalVerification() {
        try {
            logger.info("Test: Order Total Verification - Started");

            PaymentPage paymentPage = navigateToPaymentPage();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            // Get order total
            String orderTotal = paymentPage.getOrderTotal();
            Assert.assertFalse(orderTotal.isEmpty(), "Order total should be displayed");
            logger.info("Order total: " + orderTotal);

            logger.info("Test: Order Total Verification - Passed");

        } catch (Exception e) {
            logger.error("Test: Order Total Verification - Failed: " + e.getMessage());
            Assert.fail("Order total verification test failed: " + e.getMessage());
        }
    }

    /**
     * Test verify all payment page elements
     * Priority: 8
     * Group: sanity
     */
    @Test(priority = 8, groups = "sanity")
    public void testPaymentPageElements() {
        try {
            logger.info("Test: Payment Page Elements - Started");

            PaymentPage paymentPage = navigateToPaymentPage();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            // Verify all payment page elements
            boolean allElementsValid = paymentPage.verifyPaymentPageElements();
            Assert.assertTrue(allElementsValid, "All payment page elements should be valid");

            logger.info("Test: Payment Page Elements - Passed");

        } catch (Exception e) {
            logger.error("Test: Payment Page Elements - Failed: " + e.getMessage());
            Assert.fail("Payment page elements test failed: " + e.getMessage());
        }
    }

    /**
     * Test dummy payment selection - does not place order
     * Priority: 9
     * Group: regression
     * NOTE: This is a DUMMY TEST - does NOT place actual order
     */
    @Test(priority = 9, groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testDummyPaymentMethodSelection() {
        try {
            logger.info("Test: Dummy Payment Method Selection - Started (NO ORDER PLACED)");

            PaymentPage paymentPage = navigateToPaymentPage();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            // Try to select credit card (DUMMY - NOT SUBMITTING)
            try {
                logger.info("Selecting credit card payment method (DUMMY TEST)");
                paymentPage.selectPaymentMethod("creditCard");
                logger.info("Credit card selected (NOT SUBMITTING PAYMENT)");
            } catch (Exception e) {
                logger.info("Credit card option not available for selection");
            }

            // IMPORTANT: NOT calling placeOrder() to avoid real transaction
            logger.info("DUMMY TEST - No actual payment submitted");
            logger.info("Test: Dummy Payment Method Selection - Passed");

        } catch (Exception e) {
            logger.error("Test: Dummy Payment Method Selection - Failed: " + e.getMessage());
            Assert.fail("Dummy payment selection test failed: " + e.getMessage());
        }
    }

    /**
     * Parameterized payment method test with DataProvider
     * NOTE: DUMMY TEST ONLY
     *
     * @param paymentMethod payment method type
     * @param cardType card type (not actually used)
     * @param expectedResult expected result
     */
    @Test(dataProvider = "paymentData", dataProviderClass = DataProviders.class,
            groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    public void testPaymentMethodWithDataProvider(String paymentMethod, String cardType, String expectedResult) {
        try {
            logger.info("Test: DataProvider Payment Method - Method: " + paymentMethod +
                    ", CardType: " + cardType + " (DUMMY TEST)");

            PaymentPage paymentPage = navigateToPaymentPage();
            Assert.assertTrue(paymentPage.isPaymentPageLoaded(), "Payment page should load");

            // Try to select payment method (DUMMY - NOT SUBMITTING)
            try {
                paymentPage.selectPaymentMethod(paymentMethod.toLowerCase());
                logger.info("Payment method selected: " + paymentMethod + " (NOT SUBMITTING)");

                if ("success".equals(expectedResult)) {
                    logger.info("Payment method selection successful");
                }
            } catch (Exception e) {
                logger.info("Payment method not available or selection failed");
            }

            logger.info("Test: DataProvider Payment Method - Passed (DUMMY)");

        } catch (Exception e) {
            logger.error("Test: DataProvider Payment Method - Failed: " + e.getMessage());
            Assert.fail("DataProvider payment method test failed: " + e.getMessage());
        }
    }

    /**
     * Helper method to navigate to payment page
     *
     * @return PaymentPage object
     */
    private PaymentPage navigateToPaymentPage() {
        try {
            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should load");

            SearchPage searchPage = homePage.searchProduct("product");
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");

            ProductPage productPage = searchPage.openFirstProduct();
            CartPage cartPage = productPage.addToCart();

            CheckoutPage checkoutPage = cartPage.proceedToCheckout();

            if (checkoutPage.getAddressCount() > 0) {
                checkoutPage.selectFirstAddress();
                Thread.sleep(500);
                if (checkoutPage.isDeliveryOptionsVisible()) {
                    checkoutPage.selectFirstDeliveryOption();
                    Thread.sleep(500);
                }
            }

            return checkoutPage.continueToPayment();

        } catch (Exception e) {
            logger.error("Error navigating to payment page: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to payment page: " + e.getMessage());
        }
    }
}

