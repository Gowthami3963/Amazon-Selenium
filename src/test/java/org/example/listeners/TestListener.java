package org.example.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.base.BaseClass;
import org.example.utilities.ExtentManager;
import org.example.utilities.ScreenshotUtils;

import org.openqa.selenium.WebDriver;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener.java
 *
 * Purpose:
 * - Track TestNG test execution
 * - Create Extent Reports
 * - Log test status
 * - Capture screenshots when tests fail
 * - Attach screenshots to Extent Reports
 */
public class TestListener implements ITestListener {

    private static final Logger logger =
            LogManager.getLogger(TestListener.class);

    private ExtentTest test;

    /**
     * Called when test suite execution starts.
     */
    @Override
    public void onStart(ITestContext context) {

        logger.info(
                "========== Test Suite: "
                        + context.getName()
                        + " started =========="
        );

        ExtentManager.initExtentReports();
    }

    /**
     * Called when a test method starts.
     */
    @Override
    public void onTestStart(ITestResult result) {

        logger.info(
                "Test Started: " + result.getName()
        );

        test = ExtentManager.createTest(
                result.getName()
        );

        test.log(
                Status.INFO,
                "Test: "
                        + result.getName()
                        + " started"
        );
    }

    /**
     * Called when a test passes.
     */
    @Override
    public void onTestSuccess(ITestResult result) {

        logger.info(
                "Test Passed: " + result.getName()
        );

        test.log(
                Status.PASS,
                "Test: "
                        + result.getName()
                        + " passed successfully"
        );
    }

    /**
     * Called when a test fails.
     * Captures screenshot and attaches it to Extent Report.
     */
    @Override
    public void onTestFailure(ITestResult result) {

        logger.error(
                "Test Failed: " + result.getName()
        );

        logger.error(
                "Failure reason: "
                        + result.getThrowable()
        );

        test.log(
                Status.FAIL,
                "Test: "
                        + result.getName()
                        + " failed"
        );

        if (result.getThrowable() != null) {
            test.log(
                    Status.FAIL,
                    result.getThrowable()
            );
        }

        // Capture screenshot on failure
        try {

            WebDriver driver =
                    BaseClass.getDriver();

            if (driver != null) {

                String screenshotPath =
                        ScreenshotUtils.captureScreenshot(
                                driver,
                                result.getName()
                        );

                if (screenshotPath != null) {

                    test.addScreenCaptureFromPath(
                            screenshotPath
                    );

                    logger.info(
                            "Screenshot captured and attached to report"
                    );
                }
            }

        } catch (Exception e) {

            logger.error(
                    "Error capturing screenshot: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Called when a test is skipped.
     */
    @Override
    public void onTestSkipped(ITestResult result) {

        logger.warn(
                "Test Skipped: " + result.getName()
        );

        test.log(
                Status.SKIP,
                "Test: "
                        + result.getName()
                        + " skipped"
        );
    }

    /**
     * Called when a test fails but is within
     * the configured success percentage.
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(
            ITestResult result) {

        logger.warn(
                "Test Failed But Within Success Percentage: "
                        + result.getName()
        );
    }

    /**
     * Called when test suite execution finishes.
     */
    @Override
    public void onFinish(ITestContext context) {

        logger.info(
                "========== Test Suite: "
                        + context.getName()
                        + " finished =========="
        );

        ExtentManager.flushExtentReports();
    }
}