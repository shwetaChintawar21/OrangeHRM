package com.orangeHRM.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

    public class PayGradeSalaryPage {
        private WebDriver driver;
        private WebDriverWait wait;

        @FindBy(xpath = "//span[text()='Admin']")
        private WebElement adminMenu;

        @FindBy(xpath = "//span[text()='Job ']")
        private WebElement jobMenu;

        @FindBy(xpath = "//a[text()='Pay Grades']")
        private WebElement payGradesLink;

        @FindBy(css = ".oxd-table-row")
        private List<WebElement> payGradeRows;

        @FindBy(css = ".oxd-button--secondary")
        private WebElement addCurrencyButton;

        @FindBy(css = ".oxd-select-wrapper")
        private List<WebElement> dropdowns;

        @FindBy(css = ".oxd-select-dropdown .oxd-select-option")
        private List<WebElement> dropdownOptions;

        @FindBy(xpath = "//label[contains(text(),'Minimum Salary')]/following::input[1]")
        private WebElement minSalaryInput;

        @FindBy(xpath = "//label[contains(text(),'Maximum Salary')]/following::input[1]")
        private WebElement maxSalaryInput;

        @FindBy(css = "button[type='submit']")
        private WebElement saveButton;

        @FindBy(css = ".oxd-toast")
        private WebElement toastMessage;

        public PayGradeSalaryPage(WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            PageFactory.initElements(driver, this);
        }

        public void navigateToAdmin() {
            wait.until(ExpectedConditions.elementToBeClickable(adminMenu)).click();
        }

        public void clickJobMenu() {
            wait.until(ExpectedConditions.elementToBeClickable(jobMenu)).click();
        }

        public void clickPayGrades() {
            wait.until(ExpectedConditions.elementToBeClickable(payGradesLink)).click();
        }

        public void selectPayGrade(String payGrade) {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".oxd-table-row")));
            WebElement targetRow = payGradeRows.stream()
                    .filter(row -> row.getText().contains(payGrade))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Pay Grade not found: " + payGrade));

            targetRow.click();
        }

        public void clickAddCurrency() {
            wait.until(ExpectedConditions.elementToBeClickable(addCurrencyButton)).click();
        }

        public void selectCurrency(String currency) {
            try {
                // Wait for and click currency dropdown
                WebElement currencyDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                        dropdowns.get(0)));
                currencyDropdown.click();

                // Wait for dropdown options and select
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector(".oxd-select-dropdown .oxd-select-option")));

                WebElement currencyOption = dropdownOptions.stream()
                        .filter(option -> option.getText().trim().equals(currency))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Currency not found: " + currency));

                currencyOption.click();
            } catch (Exception e) {
                throw new RuntimeException("Failed to select currency: " + e.getMessage());
            }
        }

        public void enterMinimumSalary(String salary) {
            wait.until(ExpectedConditions.elementToBeClickable(minSalaryInput))
                    .sendKeys(salary);
        }

        public void enterMaximumSalary(String salary) {
            wait.until(ExpectedConditions.elementToBeClickable(maxSalaryInput))
                    .sendKeys(salary);
        }

        public void clickSave() {
            wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        }

        public boolean isSuccessMessageDisplayed() {
            try {
                wait.until(ExpectedConditions.visibilityOf(toastMessage));
                return toastMessage.getText().contains("Successfully Updated");
            } catch (Exception e) {
                return false;
            }
        }


        public void validateSalaryRange(String minSalary, String maxSalary) {
            double min = Double.parseDouble(minSalary);
            double max = Double.parseDouble(maxSalary);
            if (min >= max) {
                throw new IllegalArgumentException("Minimum salary must be less than maximum salary");
            }
        }

        public void enterPayGradeName(String payGradeName) {
            WebElement payGradeNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='oxd-input-group oxd-input-field-bottom-space']//div//input[@class='oxd-input oxd-input--active']")));
            payGradeNameInput.sendKeys(payGradeName);
            WebElement saveButton= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()=' Save ']")));
            saveButton.click();
        }

        public void clickAddButton() {
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Add']")));
            addButton.click();
        }
    }

