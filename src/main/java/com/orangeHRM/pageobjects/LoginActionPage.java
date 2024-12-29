package com.orangeHRM.pageobjects;

import com.orangeHRM.managers.DriverManager;
import com.orangeHRM.utilities.Wait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class LoginActionPage {

    public WebDriver driver = DriverManager.Instance;
    public WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    private static final Logger logger = LogManager.getLogger(LoginActionPage.class);
    public LoginActionPage(WebDriver webDriver) {
        webDriver=driver;
        PageFactory.initElements(webDriver, this);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public void setup(String homePageUrl, int timeOutSeconds) {
        Wait.untilPageReadyState(driver, Duration.ofSeconds(timeOutSeconds));
        driver.get(homePageUrl);
    }
    @FindBy(xpath="//input[@name='username'")
    private WebElement username;

    @FindBy(xpath="//input[@name='password'")
    private WebElement password;

    @FindBy(xpath="//p[text()='My Actions']")
    private WebElement dashboard;

    @FindBy(xpath="//button[@class='oxd-button oxd-button--medium oxd-button--main orangehrm-login-button']")
    private WebElement loginButton;

    @FindBy(xpath="//p[text()='Invalid credentials']")
    private WebElement errorMessage;

    @FindBy(xpath="//span[text()='Required']")
    private WebElement required;
    @FindBy(xpath = "//i[@class='oxd-icon bi-caret-down-fill oxd-userdropdown-icon']")
    private WebElement list;
    @FindBy(xpath = "//li[normalize-space()='Logout']")
    private WebElement logout;

    public String getErrorMessage() {
        return errorMessage.getText();
    }
    public void enterUsername(String user) {
        wait.until(ExpectedConditions.visibilityOf(username));
        username.sendKeys(user);
    }

    public void enterPassword(String pass) {
        password.sendKeys(pass);
    }

    public void clickLogin() {
        logger.debug("Attempting to click login button");
        driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--main orangehrm-login-button']")).click();
        logger.info("Successfully clicked login button");
    }

    public boolean isDashboardDisplayed() throws InterruptedException {
        logger.debug("Checking if dashboard is displayed");
        return dashboard.isDisplayed();
    }

    public void login(String string, String string2) {
        logger.debug("Attempting to enter userName"+string+"Attempting to enter userName"+string2);
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(string);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(string2);
        logger.info("Successfully entered usetname and password login button");
    }
    public String getRequiredMessage() {
        logger.debug("Attempting to get error message");
        return required.getText();
    }
    public void logout() {
        logger.debug("Attempting to logout");
        list.click();
        wait.until(ExpectedConditions.visibilityOf(logout));
        logout.click();
        logger.info("Successfully logout");
    }
}
