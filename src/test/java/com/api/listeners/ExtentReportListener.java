package com.api.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.api.reporting.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

/**
 * TestNG listener that integrates ExtentReports for rich HTML test reporting.
 * Automatically captures test results, categories, and failure details.
 */
public class ExtentReportListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(ExtentReportListener.class);
    private static final ExtentReports extent = ExtentReportManager.getInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        logger.info("ExtentReport: Suite '{}' started", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("ExtentReport: Suite '{}' finished. Flushing report...", context.getName());
        ExtentReportManager.flushReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        // Create test node with description
        ExtentTest test = extent.createTest(
                testName,
                description != null && !description.isEmpty() ? description : testName
        );

        // Assign category based on test class name
        String className = result.getTestClass().getRealClass().getSimpleName();
        test.assignCategory(className);

        // Assign category based on the test suite/context name
        test.assignCategory(result.getTestContext().getName());

        extentTest.set(test);
        logger.debug("ExtentReport: Test '{}' started", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            long duration = result.getEndMillis() - result.getStartMillis();
            test.pass(MarkupHelper.createLabel(
                    "PASSED - " + result.getMethod().getMethodName() + " (" + duration + "ms)",
                    ExtentColor.GREEN
            ));
            test.info("Execution time: " + duration + "ms");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            // Log the failure with exception details
            test.fail(MarkupHelper.createLabel(
                    "FAILED - " + result.getMethod().getMethodName(),
                    ExtentColor.RED
            ));

            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                test.fail(throwable);

                // Log stack trace for debugging
                String stackTrace = Arrays.toString(throwable.getStackTrace());
                test.info("Stack Trace: " + stackTrace);
            }

            // Log test parameters if any
            Object[] params = result.getParameters();
            if (params != null && params.length > 0) {
                test.info("Parameters: " + Arrays.toString(params));
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.skip(MarkupHelper.createLabel(
                    "SKIPPED - " + result.getMethod().getMethodName(),
                    ExtentColor.ORANGE
            ));

            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                test.skip(throwable.getMessage());
            }
        }
    }

    /**
     * Utility method to log additional info to the current test in the report.
     * Can be called from test methods to add request/response details.
     */
    public static void logInfo(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.info(message);
        }
    }

    /**
     * Log API request details to the report.
     */
    public static void logRequest(String method, String endpoint, String body) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.info("<b>Request:</b> " + method + " " + endpoint);
            if (body != null && !body.isEmpty()) {
                test.info("<pre>" + body + "</pre>");
            }
        }
    }

    /**
     * Log API response details to the report.
     */
    public static void logResponse(int statusCode, String responseBody) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.info("<b>Response Status:</b> " + statusCode);
            if (responseBody != null && !responseBody.isEmpty()) {
                // Truncate very long responses
                String truncated = responseBody.length() > 2000
                        ? responseBody.substring(0, 2000) + "... [truncated]"
                        : responseBody;
                test.info("<pre>" + truncated + "</pre>");
            }
        }
    }
}
