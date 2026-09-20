package org.example.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utilities.ConfigReader;

/**
 * BaseClass.java
 * Purpose: Initialize and manage WebDriver for all test classes
 * Extends this class in all test classes for browser setup and teardown
 */
public class BaseClass {

    // Static WebDriver for thread-safe operations
    protected static WebDriver driver;
    private static final Logger logger = LogManager.getLogger(BaseClass.class);

    /**
     * Initialize WebDriver before each test method
     * Setup is done based on browser type in config.properties
     */
    @BeforeMethod
    public void setUp() {
        try {
            String browser = ConfigReader.getBrowser().toLowerCase();
            String url = ConfigReader.getURL();
            long implicitWait = ConfigReader.getImplicitWait();

            logger.info("Starting browser setup...");

            // Initialize WebDriver based on browser type
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    logger.info("Chrome browser launched");
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    logger.info("Firefox browser launched");
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    logger.info("Edge browser launched");
                    break;

                default:
                    logger.error("Browser '" + browser + "' not supported");
                    throw new IllegalArgumentException("Browser '" + browser + "' is not supported");
            }

            // Maximize browser window
            driver.manage().window().maximize();
            logger.info("Browser window maximized");

            // Set implicit wait
            java.util.concurrent.TimeUnit.SECONDS.toString();
            driver.manage().timeouts().implicitlyWait(
                    java.time.Duration.ofSeconds(implicitWait)
            );
            logger.info("Implicit wait set to: " + implicitWait + " seconds");

            // Navigate to URL
            driver.get(url);
            logger.info("Navigated to URL: " + url);

        } catch (Exception e) {
            logger.error("Error during browser setup: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize WebDriver: " + e.getMessage());
        }
    }

    /**
     * Close WebDriver after each test method
     */
    @AfterMethod
    public void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
                logger.info("Browser closed successfully");
            }
        } catch (Exception e) {
            logger.error("Error during browser teardown: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get WebDriver instance
     *
     * @return WebDriver object
     */
    public static WebDriver getDriver() {
        return driver;
    }
}

