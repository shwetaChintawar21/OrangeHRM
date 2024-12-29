package Hooks;

import com.orangeHRM.managers.DriverManager;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hook {
    static WebDriver driver=DriverManager.Instance;

    @AfterStep
    public void takeScreenshotOfTestCase(Scenario scenario) {
        if (scenario.isFailed()) {
            String screenshotName = scenario.getName().replaceAll(" ", "_");
            byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(sourcePath, "image/png", screenshotName);
        }
    }

    @AfterAll
    public static void closeWindowAndBrowser(){
        driver = DriverManager.Instance;
        driver.close();
        driver.quit();
    }
}
