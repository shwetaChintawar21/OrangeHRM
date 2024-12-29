package stepdefinition;

import com.orangeHRM.dataproviders.ConfigFileReader;
import com.orangeHRM.managers.DriverManager;
import com.orangeHRM.pageobjects.LoginActionPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class LoginStepDefs {
    private LoginActionPage loginActionPage = new LoginActionPage(DriverManager.Instance);
    ConfigFileReader configFileReader = new ConfigFileReader();
    Map<String, String> credentials = configFileReader.getCredentials("valid.username","valid.password");
    Map<String, String> invalidUserCredentials = configFileReader.getCredentials("invalid.username","valid.password");
    Map<String, String> invalidPassword = configFileReader.getCredentials("valid.username","invalid.password");
    Map<String, String> invaliduserPassword = configFileReader.getCredentials("invalid.username","invalid.password");
    Map<String, String> emptyUsername = configFileReader.getCredentials("empty.username","valid.password");
    Map<String, String> emptyCreds = configFileReader.getCredentials("empty.username","empty.password");

    @Given("Open the Browser and launch the application")
    public void open_the_browser_and_launch_the_application() {
        String homePageUrl = configFileReader.getUrl();
        int timeOutSeconds  = configFileReader.getTime();
        loginActionPage.setup(homePageUrl,timeOutSeconds);
    }

    @When("I entered valid username and password")
    public void i_entered_valid_username_and_valid_password() {
        loginActionPage.login(credentials.get("valid.username"), credentials.get("valid.password"));
    }
    @When("I entered invalid username and valid password")
    public void i_entered_invalid_username_and_valid_password() {
        loginActionPage.login(invalidUserCredentials.get("invalid.username"), invalidUserCredentials.get("valid.password"));
    }

    @When("I entered valid username and invalid password")
    public void i_entered_valid_username_and_invalid_password() {
        loginActionPage.login(invalidPassword.get("valid.username"), invalidPassword.get("invalid.password"));
    }

    @And("I entered invalid username and invalid password")
    public void i_entered_invalid_username_and_invalid_password() {
        loginActionPage.login(invaliduserPassword.get("invalid.username"),invaliduserPassword.get("invalid.password"));
    }

    @When("I entered empty username and valid password")
    public void user_enters_empty_username_and_valid_password() {
        loginActionPage.login(emptyUsername.get("empty.username"),emptyUsername.get("valid.password"));
    }
    @And("Verify error message {string} should be displayed")
    public void verify_username_error_message(String errorMsg) {
        Assert.assertEquals(errorMsg,loginActionPage.getRequiredMessage());
    }
    @When("I leaves username and password empty")
    public void user_leaves_credentials_empty() {
        loginActionPage.login(emptyCreds.get("empty.username"),emptyCreds.get("empty.password"));
    }

    @And("Error message {string} should be displayed")
    public void verify_invalid_credentials_error_message(String errorMsg) {
        Assert.assertEquals(errorMsg,loginActionPage.getErrorMessage());
    }

    @And("Click on login button")
    public void click_on_login_button() {
        loginActionPage.clickLogin();
    }
    @Then("I should be logged in successfully")
    public void verify_successful_login() throws InterruptedException {
        Assert.assertTrue("Login was not successful", loginActionPage.isDashboardDisplayed());
    }

    @And("Dashboard page should be displayed")
    public void verify_dashboard_page() throws InterruptedException {
       loginActionPage.isDashboardDisplayed();
        Assert.assertTrue("Dashboard is not displayed", loginActionPage.isDashboardDisplayed());
    }
    @And("Logout from application")
    public void logout_from_application() {
        loginActionPage.logout();
    }

}
