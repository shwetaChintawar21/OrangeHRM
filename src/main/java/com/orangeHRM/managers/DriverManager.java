package com.orangeHRM.managers;

import com.orangeHRM.utilities.TestContext;
import org.openqa.selenium.WebDriver;

public class DriverManager {
    private static TestContext testContext = new TestContext();
    public static WebDriver Instance = testContext.getDriverManager().getDriver();
}
