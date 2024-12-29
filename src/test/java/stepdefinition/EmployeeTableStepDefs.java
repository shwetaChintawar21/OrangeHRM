package stepdefinition;

import com.orangeHRM.managers.DriverManager;
import com.orangeHRM.pageobjects.EmployeeTablePage;
import com.orangeHRM.pageobjects.LoginActionPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class EmployeeTableStepDefs {
        private static final Logger logger = LogManager.getLogger(EmployeeTableStepDefs.class);
        private EmployeeTablePage employeeTablePage=new EmployeeTablePage(DriverManager.Instance);
        private Map<String, String> employeeDetails;

        @Given("I am logged in to OrangeHRM")
        public void iAmLoggedInToOrangeHRM() {
            LoginActionPage loginPage = new LoginActionPage(DriverManager.Instance);
            loginPage.login("Admin", "admin123");
        }

        @When("I navigate to PIM module")
        public void iNavigateToPIMModule() {
            employeeTablePage.clickPIM();
        }

        @When("I search for employee name {string}")
        public void iSearchForEmployeeName(String searchText) {
            employeeTablePage.searchEmployee(searchText);
        }

        @Then("I should see employee records in the table")
        public void iShouldSeeEmployeeRecordsInTheTable() {
            Assert.assertTrue(employeeTablePage.isTablePopulated(),
                    "Employee table is empty");
        }

        @Then("the search results should contain {string}")
        public void theSearchResultsShouldContain(String searchText) throws InterruptedException {
            System.out.println(searchText);
            Assert.assertTrue(employeeTablePage.searchResultsContain(searchText),
                    "Search results don't contain: " + searchText);
        }

        @When("I click on {string} column header")
        public void iClickOnColumnHeader(String columnName) {
            employeeTablePage.clickColumnHeader(columnName);
        }

        @Then("the {string} column should be sorted in ascending order")
        public void theColumnShouldBeSortedInAscendingOrder(String columnName) {
            Assert.assertTrue(employeeTablePage.isColumnSorted(columnName),
                    "Column is not sorted in ascending order");
        }

        @When("I view the employee list")
        public void iViewTheEmployeeList() {
            // Already on employee list page from background steps
        }

        @Then("I should see maximum {int} records per page")
        public void iShouldSeeMaximumRecordsPerPage(int maxRecords) {
            int actualRecords = employeeTablePage.getRecordsPerPage();
            Assert.assertTrue(actualRecords <= maxRecords,
                    "Records per page (" + actualRecords + ") exceeds maximum (" + maxRecords + ")");
        }

        @Then("pagination controls should be displayed if records exceed {int}")
        public void paginationControlsShouldBeDisplayedIfRecordsExceed(int recordCount) {
            if (employeeTablePage.getRecordsPerPage() > recordCount) {
                Assert.assertTrue(employeeTablePage.isPaginationVisible(),
                        "Pagination controls are not visible");
            }
        }

        @When("I search for employee id {string}")
        public void iSearchForEmployeeId(String employeeId) throws InterruptedException {
            employeeDetails = employeeTablePage.getEmployeeDetails(employeeId);
        }

        @Then("I should see the following employee details:")
        public void iShouldSeeTheFollowingEmployeeDetails(DataTable expectedDetails) {
            List<Map<String, String>> expectedRows = expectedDetails.asMaps();
            Map<String, String> expectedRow = expectedRows.get(0);
            expectedRow.forEach((key, value) -> {
                Assert.assertEquals(employeeDetails.get(key), value,
                        "Mismatch in " + key + " value");
            });
        }
    @When("I select multiple employees from the table")
    public void iSelectMultipleEmployeesFromTheTable() throws InterruptedException {
        // Select 3 employees (you can modify this number)
        employeeTablePage.selectEmployees(1);
    }
    @And("I perform bulk action {string}")
    public void iPerformBulkAction(String action) {
        if ("Delete".equalsIgnoreCase(action)) {
            employeeTablePage.performBulkDelete();
        } else {
            throw new IllegalArgumentException("Unsupported bulk action: " + action);
        }
    }

    @Then("selected employees should be removed from the table")
    public void selectedEmployeesShouldBeRemovedFromTheTable() {
        Assert.assertTrue(employeeTablePage.isSuccessMessageDisplayed(),
                "Success message was not displayed after deletion");

        Assert.assertTrue(employeeTablePage.verifyEmployeesDeleted(),
                "Not all selected employees were deleted from the table");
    }
}



