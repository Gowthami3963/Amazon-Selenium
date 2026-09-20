package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.utilities.WaitUtils;

import java.util.List;

/**
 * CartPage.java
 *
 * Purpose:
 * Page Object Model for Amazon Shopping Cart Page.
 *
 * Contains:
 * - Cart item operations
 * - Delete item operations
 * - Quantity update operations
 * - Subtotal verification
 * - Checkout operations
 * - Cart validation
 */
public class CartPage {

    private WebDriver driver;

    private static final Logger logger =
            LogManager.getLogger(CartPage.class);

    // ============================================================
    // LOCATORS
    // ============================================================

    @FindBy(xpath = "//div[@data-component-type='s-shopping-cart-item']")
    private List<WebElement> cartItems;

    @FindBy(xpath = "//input[@value='Delete']")
    private List<WebElement> deleteButtons;

    @FindBy(xpath = "//span[@class='a-price-whole']")
    private List<WebElement> itemPrices;

    @FindBy(xpath = "//span[@id='sc-subtotal-amount-buybox']")
    private WebElement subtotalPrice;

    @FindBy(id = "sc-buy-box-ptc-button")
    private WebElement proceedToCheckoutButton;

    @FindBy(xpath = "//h1[contains(text(), 'Shopping Cart')]")
    private WebElement cartPageTitle;

    @FindBy(xpath = "//span[contains(text(), 'Your Amazon Cart is empty')]")
    private WebElement emptyCartMessage;

    @FindBy(xpath = "//select[@class='a-native-dropdown']")
    private List<WebElement> quantityDropdowns;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public CartPage(WebDriver driver) {

        this.driver = driver;

        PageFactory.initElements(driver, this);

        logger.info("CartPage initialized");
    }


    // ============================================================
    // CART ITEM METHODS
    // ============================================================

    /**
     * Get number of items in cart.
     *
     * @return number of cart items
     */
    public int getCartItemsCount() {

        try {

            logger.info("Getting cart items count");

            int count = cartItems.size();

            logger.info("Cart items count: " + count);

            return count;

        } catch (Exception e) {

            logger.error(
                    "Error getting cart items count: "
                            + e.getMessage()
            );

            return 0;
        }
    }


    /**
     * Check whether cart is empty.
     *
     * @return true if cart is empty
     */
    public boolean isCartEmpty() {

        try {

            logger.info("Checking if cart is empty");

            try {

                WaitUtils.waitForVisibility(
                        driver,
                        emptyCartMessage,
                        5
                );

                logger.info("Cart is empty");

                return true;

            } catch (Exception e) {

                logger.info("Cart is not empty");

                return false;
            }

        } catch (Exception e) {

            logger.error(
                    "Error checking if cart is empty: "
                            + e.getMessage()
            );

            return false;
        }
    }


    /**
     * Verify whether cart page is loaded.
     *
     * @return true if cart page is loaded
     */
    public boolean isCartPageLoaded() {

        try {

            logger.info("Verifying if cart page is loaded");

            WaitUtils.waitForVisibility(
                    driver,
                    cartPageTitle,
                    10
            );

            logger.info("Cart page is loaded");

            return true;

        } catch (Exception e) {

            logger.error(
                    "Cart page not loaded: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ============================================================
    // DELETE ITEM METHODS
    // ============================================================

    /**
     * Remove item from cart by index.
     *
     * @param index index of item
     */
    public void removeItemByIndex(int index) {

        try {

            logger.info(
                    "Removing item at index: " + index
            );

            if (deleteButtons.size() <= index) {

                throw new RuntimeException(
                        "Item at index "
                                + index
                                + " not found"
                );
            }

            WaitUtils.waitForClickability(
                    driver,
                    deleteButtons.get(index),
                    10
            );

            deleteButtons.get(index).click();

            logger.info(
                    "Item at index "
                            + index
                            + " removed"
            );

            Thread.sleep(1000);

        } catch (Exception e) {

            logger.error(
                    "Error removing item: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "Failed to remove item: "
                            + e.getMessage()
            );
        }
    }


    /**
     * Remove first item from cart.
     */
    public void removeFirstItem() {

        try {

            logger.info("Removing first item from cart");

            removeItemByIndex(0);

        } catch (Exception e) {

            logger.error(
                    "Error removing first item: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "Failed to remove first item: "
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // QUANTITY METHODS
    // ============================================================

    /**
     * Update quantity of item by index.
     *
     * @param index item index
     * @param quantity new quantity
     */
    public void updateQuantityByIndex(
            int index,
            String quantity) {

        try {

            logger.info(
                    "Updating quantity for item at index "
                            + index
                            + " to: "
                            + quantity
            );

            if (quantityDropdowns.size() <= index) {

                throw new RuntimeException(
                        "Item at index "
                                + index
                                + " not found"
                );
            }

            Select select =
                    new Select(
                            quantityDropdowns.get(index)
                    );

            select.selectByValue(quantity);

            logger.info(
                    "Quantity updated to: "
                            + quantity
            );

            Thread.sleep(1000);

        } catch (Exception e) {

            logger.error(
                    "Error updating quantity: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "Failed to update quantity: "
                            + e.getMessage()
            );
        }
    }


    /**
     * Update quantity of first item.
     *
     * @param quantity new quantity
     */
    public void updateFirstItemQuantity(
            String quantity) {

        try {

            logger.info(
                    "Updating first item quantity to: "
                            + quantity
            );

            updateQuantityByIndex(
                    0,
                    quantity
            );

        } catch (Exception e) {

            logger.error(
                    "Error updating first item quantity: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "Failed to update first item quantity: "
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // PRICE METHODS
    // ============================================================

    /**
     * Get subtotal price.
     *
     * @return subtotal as String
     */
    public String getSubtotalPrice() {

        try {

            logger.info("Getting subtotal price");

            WaitUtils.waitForVisibility(
                    driver,
                    subtotalPrice,
                    10
            );

            String subtotal =
                    subtotalPrice.getText();

            logger.info(
                    "Subtotal price: " + subtotal
            );

            return subtotal;

        } catch (Exception e) {

            logger.error(
                    "Error getting subtotal price: "
                            + e.getMessage()
            );

            return "";
        }
    }


    // ============================================================
    // CHECKOUT METHODS
    // ============================================================

    /**
     * Proceed to checkout.
     *
     * @return CheckoutPage object
     */
    public CheckoutPage proceedToCheckout() {

        try {

            logger.info(
                    "Clicking proceed to checkout button"
            );

            WaitUtils.waitForClickability(
                    driver,
                    proceedToCheckoutButton,
                    10
            );

            proceedToCheckoutButton.click();

            logger.info(
                    "Proceed to checkout button clicked"
            );

            Thread.sleep(2000);

            return new CheckoutPage(driver);

        } catch (Exception e) {

            logger.error(
                    "Error proceeding to checkout: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "Failed to proceed to checkout: "
                            + e.getMessage()
            );
        }
    }


    /**
     * Check whether checkout button is visible.
     *
     * @return true if visible
     */
    public boolean isCheckoutButtonVisible() {

        try {

            logger.info(
                    "Checking if checkout button is visible"
            );

            WaitUtils.waitForVisibility(
                    driver,
                    proceedToCheckoutButton,
                    10
            );

            return proceedToCheckoutButton.isDisplayed();

        } catch (Exception e) {

            logger.error(
                    "Error checking checkout button visibility: "
                            + e.getMessage()
            );

            return false;
        }
    }


    /**
     * Check whether Continue to Payment / Proceed to Checkout
     * button is enabled.
     *
     * @return true if button is enabled
     */
    public boolean isContinueToPaymentButtonEnabled() {

        try {

            logger.info(
                    "Checking if continue to payment button is enabled"
            );

            WaitUtils.waitForVisibility(
                    driver,
                    proceedToCheckoutButton,
                    10
            );

            boolean enabled =
                    proceedToCheckoutButton.isEnabled();

            logger.info(
                    "Continue to payment button enabled: "
                            + enabled
            );

            return enabled;

        } catch (Exception e) {

            logger.error(
                    "Error checking continue to payment button: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // ============================================================
    // PRODUCT VALIDATION
    // ============================================================

    /**
     * Verify whether a specific product is present in cart.
     *
     * @param productName product name
     * @return true if product is found
     */
    public boolean isProductInCart(
            String productName) {

        try {

            logger.info(
                    "Verifying if product '"
                            + productName
                            + "' is in cart"
            );

            for (WebElement item : cartItems) {

                if (item.getText()
                        .toLowerCase()
                        .contains(
                                productName.toLowerCase()
                        )) {

                    logger.info(
                            "Product '"
                                    + productName
                                    + "' found in cart"
                    );

                    return true;
                }
            }

            logger.warn(
                    "Product '"
                            + productName
                            + "' not found in cart"
            );

            return false;

        } catch (Exception e) {

            logger.error(
                    "Error verifying product in cart: "
                            + e.getMessage()
            );

            return false;
        }
    }
}