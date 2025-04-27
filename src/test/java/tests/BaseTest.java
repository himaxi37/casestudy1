package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import utils.ExtentManager;
import utils.Log;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    public static ExtentReports extent;
    public ExtentTest test;

    @BeforeTest
    public void setUp() {
        extent = ExtentManager.getInstance();
        Log.info("Test execution started");
    }

    @AfterTest
    public void tearDown() {
        extent.flush();
        Log.info("Test execution completed");
    }
}
