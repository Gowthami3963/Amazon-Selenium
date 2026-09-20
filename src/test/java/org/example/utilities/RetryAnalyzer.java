package org.example.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * RetryAnalyzer.java
 * Purpose: Retry failed test cases automatically
 * Implements IRetryAnalyzer to run failed tests again
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2; // Retry failed tests 2 times

    /**
     * Determine if a test should be retried
     *
     * @param result ITestResult of the failed test
     * @return true if test should be retried, false otherwise
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.info("Retrying test: " + result.getName() + " | Retry count: " + retryCount);
            return true;
        }
        return false;
    }
}

