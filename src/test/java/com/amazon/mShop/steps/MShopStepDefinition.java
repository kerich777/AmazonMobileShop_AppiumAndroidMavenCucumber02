package com.amazon.mShop.steps;

import com.Utilities.General.KeywordFactory;
import com.amazon.mShop.*;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MShopStepDefinition {
	private MShopInterface mshop = (MShopInterface) KeywordFactory.getStepDefinition("MSHOP");
	
	
	/**
	 * search the amazon Mobile shop app for items
	 *
	 * @author  noname@holistictech.com
	 * @param   targetSearchItemNameStrg   The target product name search character string
	 * @param   expectedSearchItemNameStrg The expected product name search character string
	 * @param   expectedSearchedItemPrice  The expected product search item Price
	 * @return  nothing
	 */
	@Given ("^I search the amazon Mobile shop for item \"(.*?)\" and verify search result \"(.*?)\" and price of item is \"(.*?)\"$") 
	public void mShopSearchForItem(String targetSearchItemNameStrg, String expectedSearchItemNameStrg, String expectedSearchedItemPrice) {
		this.mshop.mShopSearchForItem(targetSearchItemNameStrg, expectedSearchItemNameStrg, expectedSearchedItemPrice);
	}

	/**
	 * search the amazon Mobile shop app for items
	 *
	 * @author  noname@holistictech.com
	 * @param   username	Amazon Mobile shop app login: user name
	 * @param   password	Amazon Mobile shop app login: user password
	 * @return  nothing
	 */
    @Given("^I log into Amazon Mobile Shop as user \"(.*?)\" with password \"(.*?)\"$")
	public void mShopLogin(String username, String password) {
		this.mshop.mShopLogin(username, password);
	}

	/**
	 * search the amazon Mobile shop app for items
	 *
	 * @author  noname@holistictech.com
	 * @param   searchItemCharStrg	The product search character string
	 * @return  nothing
	 */
    @When("^I search the amazon Mobile shop for item \"(.*?)\"$")
	public void mShopSearchForItem(String targetSearchItemNameStrg) {
		this.mshop.mShopSearchForItem(targetSearchItemNameStrg);
	}
 
	/**
	 * Verify amazon Mobile shop app search result
	 *
	 * @author  noname@holistictech.com
	 * @param   expectedSearchItemNameStrg The expected product name search character string
	 * @return  nothing
	 */
    @Then("^I verify the expected search results \"(.*?)\"$")
	public void mShopVerifySearchItem(String expectedSearchItemNameStrg) {
		this.mshop.mShopVerifySearchItem(expectedSearchItemNameStrg);
	}

	/**
	 * Click amazon Mobile shop app search result
	 *
	 * @author  noname@holistictech.com
	 * @param   expectedSearchItemNameStrg The expected product name search character string
	 * @return  nothing
	 */
    @And("^I click the search results \"(.*?)\"$")
	public void mShopClickSearchItem(String expectedSearchItemNameStrg) {
		this.mshop.mShopClickSearchItem(expectedSearchItemNameStrg);
	}

	/**
	 * Verify amazon Mobile shop app search result Price
	 *
	 * @author  noname@holistictech.com
	 * @param   searchedItemPrice  	The expected search item price
	 * @return  nothing
	 */
    @Then("^I verify the product item's price \"(//d+)\" is displayed$")
	public void mShopVerifySearchItemPrice(String expectedSearchedItemPrice) {
		this.mshop.mShopVerifySearchItemPrice(expectedSearchedItemPrice);
	}
    

}
