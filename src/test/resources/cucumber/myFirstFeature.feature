#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios 
#<> (placeholder)
#""
## (Comments)



#Sample Feature Definition Template
@tag
Feature: To check that main tutorial course pages have loaded in TheTestRoom.com

@12345 @tag1 @Android @Serial_CALC @MicromaxA311 @Serial @smoke
Scenario: To check that the WebDriver Cucumber tutorial main page has loaded
	Given I navigate to TheTestRoom.com
	When I navigate to Cucumber Tutorial page
	Then the page title should be visible

