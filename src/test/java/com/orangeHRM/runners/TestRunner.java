package com.orangeHRM.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/feature/login.feature"},
        glue = {"stepdefinition", "Hooks"},
        plugin = {"pretty", "html:target/Report/extentReport.html"},
        monochrome = true)


public class TestRunner {

}




