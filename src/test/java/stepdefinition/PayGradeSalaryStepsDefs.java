package stepdefinition;

import com.orangeHRM.managers.DriverManager;
import com.orangeHRM.pageobjects.PayGradeSalaryPage;
import io.cucumber.java.en.*;
import org.testng.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

    public class PayGradeSalaryStepsDefs {
        private static final Logger logger = LogManager.getLogger(PayGradeSalaryStepsDefs.class);
        private PayGradeSalaryPage payGradePage=new PayGradeSalaryPage(DriverManager.Instance);

        private String selectedCurrency;

        @When("I navigate to {string} menu")
        public void iNavigateToMenu(String menu) {
            logger.info("Navigating to {} menu", menu);
            payGradePage.navigateToAdmin();
        }

        @And("I click on {string} menu item")
        public void iClickOnMenuItem(String menuItem) {
            logger.info("Clicking on menu item: {}", menuItem);
            if (menuItem.equals("Job")) {
                payGradePage.clickJobMenu();
            } else if (menuItem.equals("Pay Grades")) {
                payGradePage.clickPayGrades();
            }
        }

        @And("I select pay grade {string}")
        public void iSelectPayGrade(String payGrade) {
            logger.info("Selecting pay grade: {}", payGrade);
            payGradePage.selectPayGrade(payGrade);
        }

        @And("I click on Add Currency button")
        public void iClickOnAddCurrencyButton() {
            logger.info("Clicking Add Currency button");
            payGradePage.clickAddCurrency();
        }

        @And("I select currency {string}")
        public void iSelectCurrency(String currency) {
            logger.info("Selecting currency: {}", currency);
            this.selectedCurrency = currency;
            payGradePage.selectCurrency(currency);
        }

        @And("I enter minimum salary {string}")
        public void iEnterMinimumSalary(String minSalary) {
            logger.info("Entering minimum salary: {}", minSalary);
            payGradePage.enterMinimumSalary(minSalary);
        }

        @And("I enter maximum salary {string}")
        public void iEnterMaximumSalary(String maxSalary) {
            logger.info("Entering maximum salary: {}", maxSalary);
            payGradePage.enterMaximumSalary(maxSalary);
        }

        @And("I click on save button")
        public void iClickOnSaveButton() {
            logger.info("Clicking save button");
            payGradePage.clickSave();
        }

        @Then("the salary details should be saved successfully")
        public void theSalaryDetailsShouldBeSavedSuccessfully() {
            logger.info("Verifying salary details saved successfully");
            Assert.assertTrue(payGradePage.isSuccessMessageDisplayed(),
                    "Success message not displayed");
        }
        @And("I enter pay Grade name {string}")
        public void iEnterPayGradeName(String payGradeName) {
            logger.info("Entering pay grade name: {}", payGradeName);
            payGradePage.enterPayGradeName(payGradeName);
        }
        @And("I click on Add")
        public void iClickOnAdd() {
            logger.info("Clicking Add button");
            payGradePage.clickAddButton();
        }

    }


