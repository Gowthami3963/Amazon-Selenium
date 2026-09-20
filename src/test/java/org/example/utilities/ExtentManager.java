package org.example.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentManager.java
 * Purpose: Manage Extent HTML Reports
 * Generates professional HTML reports with test details and screenshots
 */
public class ExtentManager {

    private static ExtentReports extent;
    private static ExtentTest test;
    private static String reportPath;

    /**
     * Initialize Extent Reports
     *
     * @return ExtentReports instance
     */
    public static ExtentReports initExtentReports() {
        if (extent == null) {
            // Create reports folder if it doesn't exist
            String reportsPath = System.getProperty("user.dir") + "/src/test/resources/reports/";
            File reportsFolder = new File(reportsPath);

            if (!reportsFolder.exists()) {
                reportsFolder.mkdirs();
            }

            // Generate report name with timestamp
            String timestamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
            reportPath = reportsPath + "AmazonTestReport_" + timestamp + ".html";

            // Create ExtentSparkReporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

            // Configure report
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Amazon Automation Test Report");
            sparkReporter.config().setReportName("Amazon Selenium Test Execution Report");
            sparkReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");

            // Create ExtentReports instance
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Set system information
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Environment", "Test");
            extent.setSystemInfo("Tester Name", "Automation Team");
        }

        return extent;
    }

    /**
     * Create test in report
     *
     * @param testName name of the test
     * @return ExtentTest instance
     */
    public static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }

    /**
     * Get current test instance
     *
     * @return ExtentTest instance
     */
    public static ExtentTest getTest() {
        return test;
    }

    /**
     * Flush and close Extent Reports
     */
    public static void flushExtentReports() {
        if (extent != null) {
            extent.flush();
            System.out.println("Report saved at: " + reportPath);
        }
    }

    /**
     * Get report path
     *
     * @return path of the generated report
     */
    public static String getReportPath() {
        return reportPath;
    }
}

