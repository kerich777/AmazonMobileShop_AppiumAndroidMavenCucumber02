package com.Utilities.General;


import cucumber.api.Scenario;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class TestRailStepDefinitions {

	@And("^I report to testRail with case id \"(.*?)\"$")
	public void manualTestRailReport(String id) {
		Utilities utils = new Utilities();
		utils.setTestData("Utilities", "id", id);
	}
}