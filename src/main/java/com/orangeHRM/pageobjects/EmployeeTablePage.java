package com.orangeHRM.pageobjects;

import com.orangeHRM.managers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EmployeeTablePage {
    public WebDriver driver = DriverManager.Instance;
    private static final Logger logger = LogManager.getLogger(EmployeeTablePage.class);
    public WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    public EmployeeTablePage(WebDriver webDriver) {
        webDriver=driver;
        PageFactory.initElements(webDriver, this);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
        private List<String> selectedEmployeeIds = new ArrayList<>();
        @FindBy(xpath = "//div[@class='oxd-table-row oxd-table-row--with-border oxd-table-row--clickable']")
        private List<WebElement> tableRows;

        @FindBy(xpath="//span[text()='PIM']")
        private WebElement pim;

        @FindBy(css = ".oxd-table-header .oxd-table-header-cell")
        private List<WebElement> columnHeaders;

        @FindBy(xpath = "//div[@class='oxd-grid-4 orangehrm-full-width-grid']//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")
        private WebElement searchInput;

        @FindBy(css = "button[type='submit']")
        private WebElement searchButton;

        @FindBy(className = "oxd-pagination")
        private WebElement pagination;

        @FindBy(css = ".orangehrm-horizontal-padding span")
        private WebElement recordCount;

        @FindBy(css = ".oxd-table-card input[type='checkbox']")
        private List<WebElement> employeeCheckboxes;

        @FindBy(css = ".oxd-button--medium.oxd-button--label-danger")
        private WebElement deleteSelectedButton;

        @FindBy(xpath ="//button[text()=' Yes, Delete ']")
        private WebElement confirmDeleteButton;

        @FindBy(css = ".oxd-text.oxd-text--p.oxd-text--toast-message")
        private WebElement successMessage;

        public void searchEmployee(String searchText) {
            logger.info("Searching for employee: " + searchText);

                wait.until(ExpectedConditions.elementToBeClickable(searchInput));
                searchInput.click();
                searchInput.sendKeys(searchText);
                searchButton.click();
        }

        public boolean isTablePopulated() {
            return !tableRows.isEmpty();
        }

        public boolean searchResultsContain(String searchText) throws InterruptedException {
            return tableRows.stream()
                    .anyMatch(row -> row.getText()
                            .contains(searchText));
        }

        public void clickColumnHeader(String columnName) {
            logger.info("Clicking column header: " + columnName);
            try {
                WebElement header = columnHeaders.stream()
                        .filter(h -> h.getText().trim().equals(columnName))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Column not found: " + columnName));
                header.click();
            } catch (Exception e) {
                logger.error("Failed to click column header: " + e.getMessage());
                throw new RuntimeException("Column header click failed", e);
            }
        }

        public boolean isColumnSorted(String columnName) {
            List<String> columnValues = getColumnValues(columnName);
            List<String> sortedValues = new ArrayList<>(columnValues);
            Collections.sort(sortedValues);
            logger.info("Sorted values: {}", sortedValues);
            logger.info("Column values: {}", columnValues);
            return columnValues.equals(sortedValues);
        }

        public List<String> getColumnValues(String columnName) {
            int columnIndex = getColumnIndex(columnName);
            return tableRows.stream()
                    .map(row -> getCellValue(row, columnIndex))
                    .collect(Collectors.toList());
        }

        private int getColumnIndex(String columnName) {
            for (int i = 0; i < columnHeaders.size(); i++) {
                if (columnHeaders.get(i).getText().trim().equals(columnName)) {
                    return i;
                }
            }
            throw new NoSuchElementException("Column not found: " + columnName);
        }

        private String getCellValue(WebElement row, int columnIndex) {
            List<WebElement> cells = row.findElements(By.cssSelector(".oxd-table-cell"));
            return cells.get(columnIndex).getText().trim();
        }

        public int getRecordsPerPage() {
            return tableRows.size();
        }

        public boolean isPaginationVisible() {
            return pagination.isDisplayed();
        }

        public Map<String, String> getEmployeeDetails(String employeeId) throws InterruptedException {
            logger.info("Getting details for employee ID: {}", employeeId);
            searchEmployee(employeeId);
            Thread.sleep(2000);
            WebElement targetRow = tableRows.stream()
                    .filter(row -> row.getText().contains(employeeId))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Employee not found: " + employeeId));

            Map<String, String> details = new HashMap<>();
            List<WebElement> cells = targetRow.findElements(By.cssSelector(".oxd-table-cell"));

            for (int i = 0; i < columnHeaders.size() && i < cells.size(); i++) {
                String header = columnHeaders.get(i).getText().trim();
                String value = cells.get(i).getText().trim();
                details.put(header, value);
            }

            return details;
        }

        public void clickPIM() {
            logger.debug("Clicking PIM");
            pim.click();
            logger.info("Clicked PIM");
        }
    public void selectEmployees(int count) throws InterruptedException {
            for (int i = 0; i < count && i < employeeCheckboxes.size(); i++) {
            WebElement checkbox = employeeCheckboxes.get(i);
            WebElement row = checkbox.findElement(By.xpath("./ancestor::div[contains(@class, 'oxd-table-row')]"));
            String employeeId = row.findElement(By.cssSelector(".oxd-table-cell")).getText();
            selectedEmployeeIds.add(employeeId);

            if (!checkbox.isSelected()) {
                Thread.sleep(5000);
                Actions actions=new Actions(driver);
                actions.moveToElement(checkbox).click().build().perform();
            }
        }
    }

    public void performBulkDelete() {
        deleteSelectedButton.click();
        confirmDeleteButton.click();
    }

    public boolean verifyEmployeesDeleted() {
        wait.until(ExpectedConditions.visibilityOf(successMessage));
        // Wait for table to refresh
        try {
            Thread.sleep(2000); // Brief wait for table update
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Get current table rows
        List<WebElement> currentRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".oxd-table-row.oxd-table-row--with-border")));

        // Check if deleted employees are still present
        for (String id : selectedEmployeeIds) {
            for (WebElement row : currentRows) {
                if (row.getText().contains(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

