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
 * CheckoutPage.java
 * Purpose: Page Object Model for Amazon Checkout Page
 * Contains locators and methods for checkout operations
 */
public class CheckoutPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(CheckoutPage.class);

    // Locators using @FindBy annotation
    @FindBy(xpath = "//div[contains(@class, 'a-box')]//span[contains(text(), 'Checkout')]")
    private WebElement checkoutTitle;

    @FindBy(xpath = "//div[@data-a-input-name='shipToAddress']//label")
    private List<WebElement> addressOptions;

    @FindBy(xpath = "//input[@name='proceedToPayment']")
    private WebElement continueToPaymentButton;

    @FindBy(xpath = "//div[contains(@class, 'a-section')]//h3")
    private WebElement shippingAddress;

    @FindBy(xpath = "//div[contains(@data-module-id, 'DeliveryOptions')]")
    private WebElement deliveryOptions;

    @FindBy(xpath = "//input[@name='deliveryOptionId']")
    private List<WebElement> deliveryRadioButtons;

    @FindBy(xpath = "//span[contains(text(), 'Use this address')]")
    private WebElement useThisAddressButton;

    @FindBy(xpath = "//a[@href='/a/ordered']")
    private WebElement orderSummaryLink;

    /**
     * Constructor to initialize PageFactory elements
     *
     * @param driver WebDriver instance
     */
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("CheckoutPage initialized");
    }

    /**
     * Verify if checkout page is loaded
     *
     * @return true if checkout page is loaded, false otherwise
     */
    public boolean isCheckoutPageLoaded() {
        try {
            logger.info("Verifying if checkout page is loaded");
            WaitUtils.waitForVisibility(driver, checkoutTitle, 10);
            logger.info("Checkout page is loaded");
            return true;
        } catch (Exception e) {
            logger.error("Checkout page not loaded: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get number of available addresses
     *
     * @return count of addresses
     */
    public int getAddressCount() {
        try {
            logger.info("Getting address count");
            int count = addressOptions.size();
            logger.info("Address count: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting address count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Select address by index
     *
     * @param index index of address to select
     */
    public void selectAddressByIndex(int index) {
        try {
            logger.info("Selecting address at index: " + index);

            if (addressOptions.size() <= index) {
                throw new RuntimeException("Address at index " + index + " not found");
            }

            WaitUtils.waitForClickability(driver, addressOptions.get(index), 10);
            addressOptions.get(index).click();
            logger.info("Address at index " + index + " selected");

            Thread.sleep(500);

        } catch (Exception e) {
            logger.error("Error selecting address: " + e.getMessage());
            throw new RuntimeException("Failed to select address: " + e.getMessage());
        }
    }

    /**
     * Select first available address
     */
    public void selectFirstAddress() {
        try {
            logger.info("Selecting first address");
            selectAddressByIndex(0);
        } catch (Exception e) {
            logger.error("Error selecting first address: " + e.getMessage());
            throw new RuntimeException("Failed to select first address: " + e.getMessage());
        }
    }

    /**
     * Get shipping address details
     *
     * @return shipping address text
     */
    public String getShippingAddress() {
        try {
            logger.info("Getting shipping address");
            WaitUtils.waitForVisibility(driver, shippingAddress, 10);
            String address = shippingAddress.getText();
            logger.info("Shipping address: " + address);
            return address;
        } catch (Exception e) {
            logger.error("Error getting shipping address: " + e.getMessage());
            return "";
        }
    }

    /**
     * Verify if delivery options are visible
     *
     * @return true if delivery options are visible, false otherwise
     */
    public boolean isDeliveryOptionsVisible() {
        try {
            logger.info("Checking if delivery options are visible");
            return deliveryOptions.isDisplayed();
        } catch (Exception e) {
            logger.error("Error checking delivery options visibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Select delivery option by index
     *
     * @param index index of delivery option to select
     */
    public void selectDeliveryOptionByIndex(int index) {
        try {
            logger.info("Selecting delivery option at index: " + index);

            if (deliveryRadioButtons.size() <= index) {
                throw new RuntimeException("Delivery option at index " + index + " not found");
            }

            WaitUtils.waitForClickability(driver, deliveryRadioButtons.get(index), 10);
            deliveryRadioButtons.get(index).click();
            logger.info("Delivery option at index " + index + " selected");

            Thread.sleep(500);

        } catch (Exception e) {
            logger.error("Error selecting delivery option: " + e.getMessage());
            throw new RuntimeException("Failed to select delivery option: " + e.getMessage());
        }
    }

    /**
     * Select first delivery option
     */
    public void selectFirstDeliveryOption() {
        try {
            logger.info("Selecting first delivery option");
            selectDeliveryOptionByIndex(0);
        } catch (Exception e) {
            logger.error("Error selecting first delivery option: " + e.getMessage());
            throw new RuntimeException("Failed to select first delivery option: " + e.getMessage());
        }
    }

    /**
     * Proceed to payment
     *
     * @return PaymentPage object after click
     */
    public PaymentPage continueToPayment() {
        try {
            logger.info("Clicking continue to payment button");
            WaitUtils.waitForClickability(driver, continueToPaymentButton, 10);
            continueToPaymentButton.click();
            logger.info("Continue to payment button clicked");

            Thread.sleep(2000);
            return new PaymentPage(driver);

        } catch (Exception e) {
            logger.error("Error continuing to payment: " + e.getMessage());
            throw new RuntimeException("Failed to continue to payment: " + e.getMessage());
        }
    }

    /**
     * Check if continue to payment button is enabled
     *
     * @return true if button is enabled, false otherwise
     */
    public boolean isContinueToPaymentButtonEnabled() {
        try {
            logger.info("Checking if continue to payment button is enabled");
            return continueToPaymentButton.isEnabled();
        } catch (Exception e) {
            logger.error("Error checking continue button: " + e.getMessage());
            return false;
        }
    }

    /**
     * Use this address button click
     */
    public void clickUseThisAddress() {
        try {
            logger.info("Clicking use this address button");
            WaitUtils.waitForClickability(driver, useThisAddressButton, 10);
            useThisAddressButton.click();
            logger.info("Use this address button clicked");

            Thread.sleep(1000);

        } catch (Exception e) {
            logger.error("Error clicking use this address: " + e.getMessage());
            // Not critical, may not appear in all scenarios
        }
    }
}

