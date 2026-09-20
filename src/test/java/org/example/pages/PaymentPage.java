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
 * PaymentPage.java
 * Purpose: Page Object Model for Amazon Payment/Review Order Page
 * Contains locators and methods for payment verification and review
 * NOTE: Does not place actual order - only for testing payment methods
 */
public class PaymentPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(PaymentPage.class);

    // Locators using @FindBy annotation
    @FindBy(xpath = "//h1[contains(text(), 'Review your order')]")
    private WebElement reviewOrderHeader;

    @FindBy(xpath = "//div[contains(@data-payment-method, 'credit')]")
    private List<WebElement> paymentMethods;

    @FindBy(xpath = "//div[@class='a-span3']//span[contains(text(), 'Credit')]")
    private WebElement creditCardOption;

    @FindBy(xpath = "//div[@class='a-span3']//span[contains(text(), 'Debit')]")
    private WebElement debitCardOption;

    @FindBy(xpath = "//div[@class='a-span3']//span[contains(text(), 'UPI')]")
    private WebElement upiOption;

    @FindBy(xpath = "//div[@class='a-span3']//span[contains(text(), 'Net Banking')]")
    private WebElement netBankingOption;

    @FindBy(id = "placeYourOrder")
    private WebElement placeOrderButton;

    @FindBy(xpath = "//span[@id='orderSummaryTotal']")
    private WebElement orderTotal;

    @FindBy(xpath = "//div[contains(@class, 'Order Summary')]")
    private WebElement orderSummary;

    @FindBy(xpath = "//span[contains(text(), 'Order Details')]")
    private WebElement orderDetails;

    @FindBy(xpath = "//div[contains(text(), 'Items')]")
    private List<WebElement> orderItems;

    /**
     * Constructor to initialize PageFactory elements
     *
     * @param driver WebDriver instance
     */
    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("PaymentPage initialized");
    }

    /**
     * Verify if payment page is loaded
     *
     * @return true if payment page is loaded, false otherwise
     */
    public boolean isPaymentPageLoaded() {
        try {
            logger.info("Verifying if payment page is loaded");
            WaitUtils.waitForVisibility(driver, reviewOrderHeader, 10);
            logger.info("Payment page is loaded");
            return true;
        } catch (Exception e) {
            logger.error("Payment page not loaded: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get number of payment methods available
     *
     * @return count of payment methods
     */
    public int getPaymentMethodsCount() {
        try {
            logger.info("Getting payment methods count");
            int count = paymentMethods.size();
            logger.info("Payment methods count: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting payment methods count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Verify if credit card option is visible
     *
     * @return true if credit card option is visible, false otherwise
     */
    public boolean isCreditCardOptionVisible() {
        try {
            logger.info("Checking if credit card option is visible");
            WaitUtils.waitForVisibility(driver, creditCardOption, 10);
            logger.info("Credit card option is visible");
            return true;
        } catch (Exception e) {
            logger.error("Credit card option not visible: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify if debit card option is visible
     *
     * @return true if debit card option is visible, false otherwise
     */
    public boolean isDebitCardOptionVisible() {
        try {
            logger.info("Checking if debit card option is visible");
            WaitUtils.waitForVisibility(driver, debitCardOption, 10);
            logger.info("Debit card option is visible");
            return true;
        } catch (Exception e) {
            logger.error("Debit card option not visible: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify if UPI option is visible
     *
     * @return true if UPI option is visible, false otherwise
     */
    public boolean isUPIOptionVisible() {
        try {
            logger.info("Checking if UPI option is visible");
            WaitUtils.waitForVisibility(driver, upiOption, 10);
            logger.info("UPI option is visible");
            return true;
        } catch (Exception e) {
            logger.error("UPI option not visible: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify if net banking option is visible
     *
     * @return true if net banking option is visible, false otherwise
     */
    public boolean isNetBankingOptionVisible() {
        try {
            logger.info("Checking if net banking option is visible");
            WaitUtils.waitForVisibility(driver, netBankingOption, 10);
            logger.info("Net banking option is visible");
            return true;
        } catch (Exception e) {
            logger.error("Net banking option not visible: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify if place order button is visible
     *
     * @return true if place order button is visible, false otherwise
     */
    public boolean isPlaceOrderButtonVisible() {
        try {
            logger.info("Checking if place order button is visible");
            return placeOrderButton.isDisplayed();
        } catch (Exception e) {
            logger.error("Error checking place order button: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get order total amount
     * NOTE: This is for verification only, not placing actual order
     *
     * @return order total as string
     */
    public String getOrderTotal() {
        try {
            logger.info("Getting order total");
            WaitUtils.waitForVisibility(driver, orderTotal, 10);
            String total = orderTotal.getText();
            logger.info("Order total: " + total);
            return total;
        } catch (Exception e) {
            logger.error("Error getting order total: " + e.getMessage());
            return "";
        }
    }

    /**
     * Verify if order summary is visible
     *
     * @return true if order summary is visible, false otherwise
     */
    public boolean isOrderSummaryVisible() {
        try {
            logger.info("Checking if order summary is visible");
            return orderSummary.isDisplayed();
        } catch (Exception e) {
            logger.error("Error checking order summary visibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get order items count
     *
     * @return count of items in order
     */
    public int getOrderItemsCount() {
        try {
            logger.info("Getting order items count");
            int count = orderItems.size();
            logger.info("Order items count: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting order items count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Select payment method by type
     * Valid types: creditCard, debitCard, upi, netBanking
     *
     * @param paymentMethod type of payment method
     */
    public void selectPaymentMethod(String paymentMethod) {
        try {
            logger.info("Selecting payment method: " + paymentMethod);

            switch (paymentMethod.toLowerCase()) {
                case "creditcard":
                    WaitUtils.waitForClickability(driver, creditCardOption, 10);
                    creditCardOption.click();
                    logger.info("Credit card selected");
                    break;

                case "debitcard":
                    WaitUtils.waitForClickability(driver, debitCardOption, 10);
                    debitCardOption.click();
                    logger.info("Debit card selected");
                    break;

                case "upi":
                    WaitUtils.waitForClickability(driver, upiOption, 10);
                    upiOption.click();
                    logger.info("UPI selected");
                    break;

                case "netbanking":
                    WaitUtils.waitForClickability(driver, netBankingOption, 10);
                    netBankingOption.click();
                    logger.info("Net banking selected");
                    break;

                default:
                    throw new RuntimeException("Payment method '" + paymentMethod + "' not supported");
            }

            Thread.sleep(500);

        } catch (Exception e) {
            logger.error("Error selecting payment method: " + e.getMessage());
            throw new RuntimeException("Failed to select payment method: " + e.getMessage());
        }
    }

    /**
     * Verify payment page elements are displayed
     * This is for validation testing, NOT for placing actual order
     *
     * @return true if all paymen elements are visible, false otherwise
     */
    public boolean verifyPaymentPageElements() {
        try {
            logger.info("Verifying payment page elements");

            boolean orderHeaderVisible = isPaymentPageLoaded();
            boolean paymentMethodsAvailable = getPaymentMethodsCount() > 0;
            boolean summaryVisible = isOrderSummaryVisible();
            boolean totalVisible = !getOrderTotal().isEmpty();

            logger.info("Payment page elements verification complete");
            return orderHeaderVisible && paymentMethodsAvailable && summaryVisible && totalVisible;

        } catch (Exception e) {
            logger.error("Error verifying payment page elements: " + e.getMessage());
            return false;
        }
    }

    /**
     * NOTE: THIS METHOD DOES NOT PLACE ACTUAL ORDER
     * It only verifies that the place order button is visible
     * For testing purposes only - handles dummy payment validation
     *
     * @return true if place order button exists, false otherwise
     */
    public boolean verifyPlaceOrderButtonExists() {
        try {
            logger.info("Verifying place order button exists (DUMMY TEST)");
            return isPlaceOrderButtonVisible();
        } catch (Exception e) {
            logger.error("Error verifying place order button: " + e.getMessage());
            return false;
        }
    }
}

