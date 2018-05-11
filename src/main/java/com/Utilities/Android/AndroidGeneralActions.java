package com.Utilities.Android;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.Utilities.General.Driver;

import io.appium.java_client.AppiumDriver;

/**
 * AndroidGeneralActions is an abstraction of all the Selenium interactions with an Android app. 
 * For Android/mobile specific actions like swiping use AndroidElement or MobileElement.
 *
 * @author noname@holistictech.com
 */
public class AndroidGeneralActions {
	protected static final Logger LOGGER = Logger.getLogger(AndroidGeneralActions.class.getName());
	
	//@SuppressWarnings("rawtypes")
	//private AndroidDriver driver = null;
	private WebDriver driver = null;
	private WebDriverWait wait   = null;

	//@SuppressWarnings("rawtypes")
	public AndroidGeneralActions(){
		//driver = (AndroidDriver) Driver.getGeneralDriver();
		driver = Driver.getGeneralDriver();
		wait = new WebDriverWait(driver, AndroidUtilities.AndroidWaitTime);
	}
	
	/**
	 * Finds the given element. 
	 *
	 * @author  noname@holistictech.com
	 * @param   element  By locator of the element to find
	 * @return  WebElement  WebElement of the element if found, null if not found
	 */
	private WebElement findElement(By element){
		WebElement elementTofind = null;
		try{
			elementTofind = driver.findElement(element);
		}catch(NoSuchElementException e){
			LOGGER.info(element.toString() + " was not found.");
			return elementTofind;
		}
		return elementTofind;
	}
	
	/**
	 * Verifies that the element is clickable, meaning the element is visible & enabled.
	 *
	 * @author  noname@holistictech.com
	 * @param   element  By locator of the element to verify if clickable
	 * @return  WebElement  WebElement of the element if clickable, null if not clickable
	 */
	private WebElement verifyElementIsClickable(By element){
		WebElement elementToClick = null;
		try{
			elementToClick = this.wait.until(ExpectedConditions.elementToBeClickable((By) element));
		}catch(TimeoutException e){
			LOGGER.info(element.toString() + " is not clickable.");
			return elementToClick;
		}
		return elementToClick;
	}

	/**
	 * Verifies that the element is visible & enabled.
	 *
	 * @author  noname@holistictech.com
	 * @param   element  By locator of the element to verify if clickable
	 * @return  Boolean  true if clicked, false if not
	 */
	public boolean verifyElementIsVisable(By element){
		WebElement elementToVerify = null;
		try{
			elementToVerify = this.wait.until(ExpectedConditions.elementToBeClickable((By) element));
		}catch(TimeoutException e){
			LOGGER.info(element.toString() + " is not Visable or enabled.");
			return false;
		}
		elementToVerify.getText();
		return true;
	}
	
	/**
	 * Clicks the given element.
	 *
	 * @author  noname@holistictech.com
	 * @param   element  By locator of the element to click
	 * @return  Boolean  true if clicked, false if not
	 */
	public boolean clickElement(By element){
		WebElement elementToClick = verifyElementIsClickable(element);
		if(elementToClick == null){
			LOGGER.info(element.toString() + " is not clickable.");
			return false;
		}
		elementToClick.click();
		return true;
	}

	/**
	 * Clear Search text box element.
	 *
	 * @author  noname@holistictech.com
	 * @param   element       By locator of the element to clear text box
	 * @return  Boolean  true if text is entered, false if not
	 */
	public boolean clearTextBox(By element){
		WebElement textbox = findElement(element);
		if(textbox == null){
			LOGGER.info(element.toString() + " was not found.");
			return false;
		}
		textbox.clear();
		return true;
	}
	
	/**
	 * Enters text into the given element.
	 *
	 * @author  noname@holistictech.com
	 * @param   element       By locator of the element to enter text into
	 * @param   stringToType  The String to type into the element
	 * @return  Boolean  true if text is entered, false if not
	 */
	public boolean enterText(By element, String stringToType){
		WebElement textbox = findElement(element);
		if(textbox == null){
			LOGGER.info(element.toString() + " was not found.");
			return false;
		}
		clickElement(element);
		textbox.sendKeys(stringToType);
		hideKeyboard();
		return true;
	}
	
	/**
	 * Hides the keyboard.
	 *
	 * @author  noname@holistictech.com
	 * @return  Nothing
	 */
	public void hideKeyboard(){
		AppiumDriver adriver = (AppiumDriver) driver;
		adriver.hideKeyboard();
	}
}
