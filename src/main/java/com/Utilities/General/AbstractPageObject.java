package com.Utilities.General;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.Utilities.Android.AndroidGeneralActions;
//import com.Utilities.Online.OnlineGeneralActions;
//import com.holistictech.automation.Utilities.iPad.IPadGeneralActions;
//import com.Utilities.iPad.PredicateGenerator;
//import com.holistictech.automation.Utilities.iPad.iOSBy;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;


public abstract class AbstractPageObject {
	
	protected static final Logger logger = Logger.getLogger(AbstractPageObject.class.getName());

	//Current driver
	protected WebDriver driver = null;
	
	// API driver
	protected Object apiDriver = null;
	
	//Implicit wait
	protected int DRIVER_WAIT = 30;       // 30 seconds
	
	//Predicate Generator for creating iOS locators 
//	public PredicateGenerator predicateGenerator = new PredicateGenerator();
	
//	//For iPad actions
//	public IPadGeneralActions iPadGeneralActions = new IPadGeneralActions();
	
	//For Android actions
	public AndroidGeneralActions androidGeneralActions = new AndroidGeneralActions();
	
//	//iPad iOSBy to create predicates
//	public iOSBy IOSBy = new iOSBy();
	
	//To handle timing issues
	protected WebDriverWait  wait = null;
	
	@FindBy(id="webTabView")
	protected WebElement iframe;
	
//	//For online Selenium actions
//	public OnlineGeneralActions onlineGeneralActions = new OnlineGeneralActions();
	
	public AbstractPageObject() {
		this.driver = Driver.getGeneralDriver();
		this.wait = new WebDriverWait(driver, this.DRIVER_WAIT);
	}
	
	
	public void setImplicitWait(int new_driver_wait) {
		this.driver.manage().timeouts().implicitlyWait(new_driver_wait, TimeUnit.SECONDS);
	}
	
	public void defaultImplicitWait() {
		this.driver.manage().timeouts().implicitlyWait(this.DRIVER_WAIT, TimeUnit.SECONDS);
	}
	
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	
	public void switchToIframe() {
		this.switchToDefaultFrame();
		WebDriverWait wait = new WebDriverWait(this.driver, 30);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
	}
	
	public void switchToWebTabViewFrame() {
		WebDriverWait wait = null;
		Timer timer = new Timer();
		timer.startTimerInSeconds(30);
		this.setImplicitWait(2);
		while(!timer.isDone()) {
			wait = new WebDriverWait(this.driver, 2);
			try {
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(".//*[@id='webTabView']")));
				logger.info("successfully change frame to iframe WebTabView");
				break;
			} catch(Exception e) {
				logger.info("Exception occurred trying to switch to WebTabViewFrame: " + e.getMessage());
			}
		}
		this.defaultImplicitWait();
	}
	
	public void switchFrame(By locator) {
		this.switchToDefaultFrame();
		WebDriverWait wait = new WebDriverWait(this.driver, 30);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}
	
	public void switchFrame(WebElement iframeElement) {
		this.switchToDefaultFrame();
		WebDriverWait wait = new WebDriverWait(this.driver, 30);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeElement));
	}
	
	public void switchToDefaultFrame() {
		this.driver.switchTo().defaultContent();
	}
	
	@SuppressWarnings("unchecked")
	public IOSDriver<IOSElement> getIPadDriver() {
		return ((IOSDriver<IOSElement>) this.driver);
	}
	
//	public PredicateGenerator getPredicateGenerator() {
//		return this.predicateGenerator;
//	}
	
	public void waitForAlignDOMToBeReady() {
		
		 ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
		    public Boolean apply(WebDriver driver) {
		      return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
		    }
		  };

		  WebDriverWait wait = new WebDriverWait(driver,30);
		  try {
			  try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        wait.until(expectation);
		  } catch(Throwable error) {
		          Assert.assertFalse(true,"Timeout waiting for Page Load Request to complete.");
		  }
	}
	
	public String getAppTranslation(String key){
		return Utilities.getAppTranslationResources().get(key);
	}
	
	
}