Feature: Employee Table Validation in OrangeHRM

  Background:
    Given Open the Browser and launch the application
    When I entered valid username and password
    And Click on login button
    When I navigate to PIM module

  Scenario: Verify table pagination
    When I view the employee list
    Then I should see maximum 50 records per page
    And pagination controls should be displayed if records exceed 50

  Scenario: Verify employee record details
    When I search for employee id "0001"
    Then I should see the following employee details:
      | First (& Middle) Name | Last Name | Id   |
      | Orange                | Test      | 0001 |

  Scenario: Bulk Actions on Employee Table
    When I select multiple employees from the table
    And I perform bulk action "Delete"
    Then selected employees should be removed from the table

