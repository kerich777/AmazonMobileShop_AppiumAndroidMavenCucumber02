package com.amazon.mShop.Android;

import com.amazon.mShop.Android.SearchForMShopItem;
import com.amazon.mShop.MShopInterface;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class MShopAndroidKeywords implements MShopInterface{

	/**
	 * search the amazon Mobile shop app for items
	 *
	 * @author  noname@holistictech.com
	 * @param   targetSearchItemNameStrg   The target product name search character string
	 * @param   expectedSearchItemNameStrg The expected product name search character string
	 * @param   expectedSearchedItemPrice  The expected product search item Price
	 * @return  nothing
	 */
	public void mShopSearchForItem(String targetSearchItemNameStrg, String expectedSearchItemNameStrg, String expectedSearchedItemPrice){
		SearchForMShopItem searchForMShopItem = new SearchForMShopItem();
		searchForMShopItem.clickSkipSignInButton();
		searchForMShopItem.clearSearchTextBox();
		searchForMShopItem.enterItemSearchString(targetSearchItemNameStrg);
		searchForMShopItem.clickSearchRsltItemName(expectedSearchItemNameStrg);
		//searchForMShopItem.enterAttendeeName(expectedSearchedItemPrice);
		
	}
	
	/**
	 * search the amazon Mobile shop app for items
	 *
	 * @author  noname@holistictech.com
	 * @param   username	Amazon Mobile shop app login: user name
	 * @param   password	Amazon Mobile shop app login: user password
	 * @return  nothing
	 */
	public void mShopLogin(String username, String password){
		SearchForMShopItem searchForMShopItem = new SearchForMShopItem();
		if (username == "skiplogin"){
			searchForMShopItem.clickSkipSignInButton();
		} else {
			//TODO: Login/Password function
		}
		
	}
	
	/**
	 * search the amazon Mobile shop app for items
	 *
	 * @author  noname@holistictech.com
	 * @param   searchItemCharStrg	The product search character string
	 * @return  nothing
	 */
	public void mShopSearchForItem(String targetSearchItemNameStrg){
		SearchForMShopItem searchForMShopItem = new SearchForMShopItem();
		searchForMShopItem.clearSearchTextBox();
		searchForMShopItem.enterItemSearchString(targetSearchItemNameStrg);
	}
	
	/**
	 * Verify amazon Mobile shop app search result
	 *
	 * @author  noname@holistictech.com
	 * @param   expectedSearchItemNameStrg The expected product name search character string
	 * @return  nothing
	 */
	public void mShopVerifySearchItem(String expectedSearchItemNameStrg){
		SearchForMShopItem searchForMShopItem = new SearchForMShopItem();
		searchForMShopItem.verifySearchItemName(expectedSearchItemNameStrg);
	}

	/**
	 * Click amazon Mobile shop app search result
	 *
	 * @author  noname@holistictech.com
	 * @param   expectedSearchItemNameStrg The expected product name search character string
	 * @return  nothing
	 */
    public void mShopClickSearchItem(String expectedSearchItemNameStrg){
    	SearchForMShopItem searchForMShopItem = new SearchForMShopItem();
		searchForMShopItem.clickSearchRsltItemName(expectedSearchItemNameStrg);
	}

	/**
	 * Verify amazon Mobile shop app search result Price
	 *
	 * @author  noname@holistictech.com
	 * @param   expectedSearchedItemPrice  	The expected search item price
	 * @return  nothing
	 */
    public void mShopVerifySearchItemPrice(String expectedSearchedItemPrice){
    	SearchForMShopItem searchForMShopItem = new SearchForMShopItem();
		searchForMShopItem.verifySearchItemName(expectedSearchedItemPrice);
	}
}
