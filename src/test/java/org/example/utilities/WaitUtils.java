package org.example.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * WaitUtils.java
 * Purpose: Handle explicit waits in Selenium
 * Provides reusable methods for different wait conditions
 */
public class WaitUtils {

    /**
     * Wait for element to be visible
     *
     * @param driver WebDriver instance
     * @param element WebElement to wait for
     * @param timeOut wait timeout in seconds
     * @return WebElement once visible
     */
    public static WebElement waitForVisibility(WebDriver driver, WebElement element, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be visible using By locator
     *
     * @param driver WebDriver instance
     * @param locator By locator
     * @param timeOut wait timeout in seconds
     * @return WebElement once visible
     */
    public static WebElement waitForVisibility(WebDriver driver, By locator, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable
     *
     * @param driver WebDriver instance
     * @param element WebElement to wait for
     * @param timeOut wait timeout in seconds
     * @return WebElement once clickable
     */
    public static WebElement waitForClickability(WebDriver driver, WebElement element, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for element to be clickable using By locator
     *
     * @param driver WebDriver instance
     * @param locator By locator
     * @param timeOut wait timeout in seconds
     * @return WebElement once clickable
     */
    public static WebElement waitForClickability(WebDriver driver, By locator, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be present in DOM
     *
     * @param driver WebDriver instance
     * @param locator By locator
     * @param timeOut wait timeout in seconds
     * @return WebElement once present
     */
    public static WebElement waitForPresence(WebDriver driver, By locator, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element to be invisible
     *
     * @param driver WebDriver instance
     * @param locator By locator
     * @param timeOut wait timeout in seconds
     */
    public static void waitForInvisibility(WebDriver driver, By locator, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for text to be present in element
     *
     * @param driver WebDriver instance
     * @param locator By locator
     * @param text text to wait for
     * @param timeOut wait timeout in seconds
     */
    public static void waitForText(WebDriver driver, By locator, String text, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Wait for alert to be present
     *
     * @param driver WebDriver instance
     * @param timeOut wait timeout in seconds
     */
    public static void waitForAlert(WebDriver driver, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Wait for URL to contain specific string
     *
     * @param driver WebDriver instance
     * @param urlString string that should be in URL
     * @param timeOut wait timeout in seconds
     */
    public static void waitForURLContains(WebDriver driver, String urlString, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.urlContains(urlString));
    }
}

