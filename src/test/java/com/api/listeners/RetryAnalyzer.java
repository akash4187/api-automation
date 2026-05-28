package com.api.listeners;

import com.api.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer for flaky tests.
 * Automatically retries failed tests up to a configurable number of attempts.
 * Useful for handling transient network issues in API testing.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetryCount = ConfigReader.getInstance().getIntProperty("retry.max.attempts", 2);
        if (retryCount < maxRetryCount) {
            retryCount++;
            logger.warn("Retrying test '{}' - Attempt {}/{}",
                    result.getMethod().getMethodName(), retryCount, maxRetryCount);
            return true;
        }
        return false;
    }
}
