package com.w2e.java;


//import io.cucumber.junit.Cucumber;
//import io.cucumber.junit.CucumberOptions;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

import org.junit.runner.RunWith;

//@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
glue = {"com.w2e.java"}, 
plugin = {"pretty","html:target/site/htmlReport","json:target/reports/cucumber-ShoppingTestWebFeature-report.json"},
monochrome = true ,
tags = {"@regression"}

)
public class testrunner extends AbstractTestNGCucumberTests {
	
}
