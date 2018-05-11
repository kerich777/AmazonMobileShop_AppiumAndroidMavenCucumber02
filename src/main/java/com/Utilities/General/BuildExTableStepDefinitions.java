package com.Utilities.General;


import java.io.IOException;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class BuildExTableStepDefinitions {
	CSVReader1 reader = new CSVReader1();
	
		@Given("^I append Example table \"(.*?)\" to \"(.*?)\"$")
		public void appendTable(String csvFile, String featureFile) throws IOException {
			reader.appendExamples(csvFile, featureFile);
		}
}
