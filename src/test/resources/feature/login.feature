Feature: OrangeHRM Login Functionality Testing

  Background:
    Given Open the Browser and launch the application

  Scenario: Successful login with valid credentials
    When I entered valid username and password
    And Click on login button
    Then I should be logged in successfully
    And Dashboard page should be displayed
    And Logout from application

  Scenario: Failed login with invalid username and valid password
    When I entered invalid username and valid password
    And Click on login button
    Then Error message "Invalid credentials" should be displayed

  Scenario: Failed login with valid username and invalid password
    When I entered valid username and invalid password
    And Click on login button
    Then Error message "Invalid credentials" should be displayed

  Scenario: Failed login with invalid username and invalid password
    When I entered invalid username and invalid password
    And Click on login button
    And Error message "Invalid credentials" should be displayed

  Scenario: Failed login with empty username
    When I entered empty username and valid password
    And Click on login button
    And Verify error message "Required" should be displayed

  Scenario: Failed login with empty credentials
    When I leaves username and password empty
    And Click on login button
    Then Verify error message "Required" should be displayed
