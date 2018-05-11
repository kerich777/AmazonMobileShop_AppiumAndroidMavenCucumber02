
# Proper way to tag scenarios
# Scenarios should all have the following tags:
# Main component (@AE)
# Testrail ID for posting (@12345)
# Which platforms are ready to run (@Online)
# If it can run in parallel or not (@Parallel, @Serial)
# If it's a smoketest (@smoke)
# example

#Sample Feature Definition Template
@tag
Feature: Amazon Mobile Shopping

@12345 @MSHOP @Android @Serial_MSHOP @MicromaxA311 @Serial @smoke
Scenario: Launching Amazon Mobile Shop App and search for product item
	Given I log into Amazon Mobile Shop as user "skiplogin" with password "qasource3"
	When I search the amazon Mobile shop for item "Telescope"
	Then I verify the expected search results "telescope for kids" 
	And I click the search results "telescope for kids"
#	Then I verify the product item's price "$25.00" is displayed


    