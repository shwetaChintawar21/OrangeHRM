Feature: Pay Grade Salary Management
  Background:
    Given Open the Browser and launch the application
    When I entered valid username and password
    And Click on login button

  Scenario: Add salary details with currency for pay grade
    When I navigate to "Admin" menu
    And I click on "Job" menu item
    And I click on "Pay Grades" menu item
    And I click on Add Currency button
    And I enter pay Grade name "Grade 190"
    And I click on Add
    And I select currency "USD - United States Dollar"
    And I enter minimum salary "30000"
    And I enter maximum salary "90000"
    And I click on save button
    Then the salary details should be saved successfully
