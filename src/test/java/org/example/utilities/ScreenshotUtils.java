package org.example.utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils.java
 * Purpose: Capture screenshots on test failure or demand
 * Saves screenshots with timestamp in screenshots folder
 */
public class ScreenshotUtils {

    /**
     * Capture screenshot and save to screenshots folder
     *
     * @param driver WebDriver instance
     * @param screenshotName name of the screenshot file
     * @return path of the saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Create screenshots folder if it doesn't exist
            String screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/";
            File screenshotsFolder = new File(screenshotPath);

            if (!screenshotsFolder.exists()) {
                screenshotsFolder.mkdirs();
            }

            // Generate timestamp for unique filename
            String timestamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";
            String filePath = screenshotPath + fileName;

            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File srcFile = ts.getScreenshotAs(OutputType.FILE);

            // Copy screenshot to destination folder
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("Screenshot saved at: " + filePath);
            return filePath;

        } catch (IOException e) {
            System.out.println("Error capturing screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Capture screenshot and return as File
     * Useful for attaching to Extent Reports
     *
     * @param driver WebDriver instance
     * @return File object of screenshot
     */
    public static File captureScreenshotAsFile(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.FILE);
        } catch (Exception e) {
            System.out.println("Error capturing screenshot as file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot and return as Base64 string
     * Useful for embedding in reports
     *
     * @param driver WebDriver instance
     * @return Base64 string of screenshot
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.out.println("Error capturing screenshot as Base64: " + e.getMessage());
            return null;
        }
    }
}

