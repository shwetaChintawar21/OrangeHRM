package com.orangeHRM.managers;

import com.orangeHRM.dataproviders.ConfigFileReader;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BrowserTimeoutHandler {
    private static final int MAX_RETRIES = 3;
    private static final int PAGE_LOAD_TIMEOUT = 120;
    private static final int SCRIPT_TIMEOUT = 60;
    private static final int POLLING_INTERVAL = 2;
    static ConfigFileReader configFileReader;
    public static void loadUrlWithTimeout(WebDriver driver) {
        int attempts = 0;
        configFileReader = new ConfigFileReader();
        while (attempts < MAX_RETRIES) {
            try {
                // Configure browser timeouts
                configureBrowserTimeouts(driver);

                // Clear existing page state
                clearBrowserState(driver);

                // Load URL
                driver.get(configFileReader.getUrl());

                // Wait for page load
                waitForPageLoad(driver);

                return; // Success

            } catch (TimeoutException e) {
                attempts++;
                logTimeoutError(driver, attempts, e);

                if (attempts == MAX_RETRIES) {
                    captureFailureEvidence(driver);
                    throw new RuntimeException("Failed to load page after " + MAX_RETRIES + " attempts", e);
                }

                recoverBrowserState(driver);
            }
        }
    }

    private static void configureBrowserTimeouts(WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(SCRIPT_TIMEOUT));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }

    private static void clearBrowserState(WebDriver driver) {
        try {
            // Stop any ongoing page loads
            ((JavascriptExecutor) driver).executeScript("window.stop();");

            // Clear cookies and storage
            driver.manage().deleteAllCookies();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.sessionStorage.clear();");
            js.executeScript("window.localStorage.clear();");
        } catch (Exception e) {
            System.out.println("Warning: Failed to clear browser state: " + e.getMessage());
        }
    }

    private static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        wait.pollingEvery(Duration.ofSeconds(POLLING_INTERVAL))
            .ignoring(StaleElementReferenceException.class)
            .until(webDriver -> {
                try {
                    String readyState = ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .toString();
                    return "complete".equals(readyState);
                } catch (Exception e) {
                    return false;
                }
            });
    }

    private static void recoverBrowserState(WebDriver driver) {
        try {
            driver.navigate().refresh();
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("Warning: Recovery failed: " + e.getMessage());
        }
    }

    private static void logTimeoutError(WebDriver driver, int attempt, TimeoutException e) {
        String browserName = getBrowserName(driver);
        System.out.println(String.format(
            "Timeout in %s browser. Attempt %d of %d. Error: %s",
            browserName, attempt, MAX_RETRIES, e.getMessage()
        ));
    }

    private static void captureFailureEvidence(WebDriver driver) {
        try {
            String browserName = getBrowserName(driver);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(),
                Paths.get("test-output/screenshots/" + browserName + "_timeout_" + timestamp + ".png"));

            // Capture page source
            String pageSource = driver.getPageSource();
            Files.write(Paths.get("test-output/pagesource/" + browserName + "_source_" + timestamp + ".html"),
                pageSource.getBytes());

        } catch (Exception e) {
            System.out.println("Failed to capture failure evidence: " + e.getMessage());
        }
    }

    private static String getBrowserName(WebDriver driver) {
        String browserName = "Unknown";
        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        browserName = caps.getBrowserName().toLowerCase();
        return browserName;
    }
}
