package com.api.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Singleton manager for ExtentReports.
 * Configures the Spark reporter with project-specific metadata and styling.
 */
public final class ExtentReportManager {

    private static ExtentReports extent;
    private static final String REPORT_DIR = "target/extent-reports/";

    private ExtentReportManager() {
        // Utility class - prevent instantiation
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            extent = createInstance();
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = REPORT_DIR + "API-Test-Report_" + timestamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        // Configure report appearance
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("API Automation Test Report");
        sparkReporter.config().setReportName("AutomationExercise.com - API Test Results");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        sparkReporter.config().setEncoding("UTF-8");

        ExtentReports extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        // System/environment info displayed in the report dashboard
        extentReports.setSystemInfo("Project", "API Automation Framework");
        extentReports.setSystemInfo("Base URL", "https://automationexercise.com");
        extentReports.setSystemInfo("Environment", System.getProperty("environment", "dev"));
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Tester", System.getProperty("user.name"));

        return extentReports;
    }

    /**
     * Flush the report - must be called at the end of the suite.
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
