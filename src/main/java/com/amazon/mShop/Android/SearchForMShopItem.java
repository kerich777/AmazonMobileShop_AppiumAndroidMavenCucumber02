package com.amazon.mShop.Android;


import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import com.Utilities.General.AbstractPageObject;

public class SearchForMShopItem extends AbstractPageObject{
	
	By skipSignInButton   = By.id("com.amazon.mShop.android:id/skip_sign_in_button");
	By searchBarText      = By.id("com.amazon.mShop.android:id/rs_search_src_text");
	By searchRSLTItemID   = By.id("com.amazon.mShop.android:id/rs_search_dropdown_item_text");
	

	public SearchForMShopItem() {
		PageFactory.initElements(this.driver, this);
	}
	
	/**
	 * Clicks on the skip Sign In Button on the splash screen when the amazon mShop app opens
	 *
	 * @author  noname@holistictech.com
	 * @return  nothing
	 */
	public boolean clickSkipSignInButton(){
		return androidGeneralActions.clickElement(this.skipSignInButton);
	}
	
	/**
	 * Find and Clear Search Text Box Element
	 * 
	 * @author  noname@holistictech.com
	 * @return  nothing
	 */
	public boolean clearSearchTextBox(){
		return androidGeneralActions.clearTextBox(this.searchBarText);
	}	
	
	/**
	 * Enters the product Item String in to the text Search Box
	 *
	 * @author  noname@holistictech.com
	 * @param   searchItemCharStrg	The product search character string
	 * @return  nothing
	 */
	public boolean enterItemSearchString(String searchItemCharStrg){
		return androidGeneralActions.enterText(this.searchBarText, searchItemCharStrg);
	}
	

	/**
	 * Verify Search Result Element
	 * 
	 * @author  noname@holistictech.com
	 * @param   searchRsltTargetItemCharStrg	The target product item Name search character string
	 * @return  nothing
	 */
	public boolean verifySearchItemName(String searchRsltTargetItemCharStrg){
		By searchRsltElement = By.name(searchRsltTargetItemCharStrg);
		return androidGeneralActions.verifyElementIsVisable(searchRsltElement);
	}
	
	/**
	 * Select Search Result Element
	 * 
	 * @author  noname@holistictech.com
	 * @param   searchRsltTargetItemCharStrg	The target product item Name search character string
	 * @return  nothing
	 */
	public boolean clickSearchRsltItemName(String searchRsltTargetItemCharStrg){
		By searchRsltElement = By.name(searchRsltTargetItemCharStrg);
		return androidGeneralActions.clickElement(searchRsltElement);
	}	
	
	
	
//	/**
//	 * Enters the Attendee Name
//	 *
//	 * @author  noname@holistictech.com
//	 * @param   attendeeName  The name of the attendee of the person joining the meeting
//	 * @return  nothing
//	 */
//	public boolean enterAttendeeName(String attendeeName){
//		return androidGeneralActions.enterText(this.attendeeNameTextbox, attendeeName);
//	}
//	
//	/**
//	 * Clicks the Join Now button
//	 *
//	 * @author  noname@holistictech.com
//	 * @return  nothing
//	 */
//	public boolean clickJoinNowButton(){
//		//return androidGeneralActions.clickElement(this.joinNowButton);
//		 androidGeneralActions.clickElement(this.joinNowButton);
//		 try {
//			Thread.sleep(20000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 return true;
//	}
}
