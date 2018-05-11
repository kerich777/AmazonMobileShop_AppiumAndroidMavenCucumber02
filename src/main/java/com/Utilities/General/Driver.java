package com.Utilities.General;



import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

//import com.holistictech.automation.Utilities.WM.WMUtilities;
//import com.holistictech.automation.Utilities.WM.WMGeneralActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;




//import com.holistictech.automation.Utilities.API.BaseKeywords;
import com.Utilities.Android.AndroidUtilities;
//import com.holistictech.automation.Utilities.iPad.iPadUtilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.ios.IOSDriver;
//import io.appium.java_client.ios.IOSElement;

/**
 *
 * The purpose of this file is to create a platform driver and setup
 * the System Under Test to a default state.
 * @author josemelendez
 *
 */
public class Driver {

	private static WebDriver generalDriver;
//	private static BaseKeywords apiDriver;

	public static final long onlineImplicitWaitTime = 60;

	private Driver() {}
	public static WebDriver getGeneralDriver() {
		switch(System.getProperty("platform")) {
			case "Online":
                Driver.createOnlineDriver();
				break;
//			case "iPad":
//				Driver.createiPadDriver();
//				break;
//			case "WM":
//				Driver.createWMDriver();
//				break;
//			case "Align":
//				Driver.createAlignDriver();
//				break;
			case "Android":
				Driver.createAndroidDriver();
				break;
		}

		return generalDriver;
	}

//	public static Object getAPIDriver() {
//
//		Driver.createAPIDriver();
//		return apiDriver;
//	}

//	private static void createiPadDriver() {
//
//		if(generalDriver == null) {
//
//			DesiredCapabilities capabilities = iPadUtilities.getSimulatorCapabilities();
//
//
//			URL url = null;
//			try {
//				url = new URL("http://127.0.0.1:4723/wd/hub");
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			generalDriver =  new IOSDriver<IOSElement>(url, capabilities);
//			generalDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
//			generalDriver.manage().timeouts().setScriptTimeout(0, TimeUnit.SECONDS);
//		}
//	}

	private static void createOnlineDriver() {

		if(generalDriver == null) {
			try {
				generalDriver = createBrowserDriverOnGrid(System.getProperty("browser"));
				generalDriver.manage().timeouts().implicitlyWait(onlineImplicitWaitTime, TimeUnit.SECONDS);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

//	private static void createAlignDriver() {
//		if(generalDriver == null) {
//			try {
//				generalDriver = createBrowserDriverOnGrid(System.getProperty("browser"));
//				generalDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//				generalDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
//				generalDriver.get(System.getProperty("url"));
//				generalDriver.manage().window().maximize();
//                generalDriver.manage().deleteAllCookies();
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//		}
//	}

//	private static void createAPIDriver() {
//		if(apiDriver == null) {
//			apiDriver = new BaseKeywords();
//			apiDriver.setupSuiteCredential();
//		}
//	}

//	private static void createWMDriver() {
//		if(generalDriver == null) {
//			DesiredCapabilities capabilities = WMUtilities.getWMCapabilities();
//			try {
//				generalDriver = new IOSDriver(new URL(WMUtilities.WMappDriverUrl), capabilities);
//				WMGeneralActions.ensureFullScreen((IOSDriver) generalDriver);
//				generalDriver.manage().timeouts().implicitlyWait(WMUtilities.WMWaitTime, TimeUnit.MILLISECONDS);
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	/**
	 * Creates the webdriver and sets its capabilities based on the browser type. Then sets it to the Selenium grid hub.
	 *
	 * @param  browser     Which webdriver to load
	 * @return             WebDriver of the desired browser with its capabilities set and connected to the grid hub.
	 */
	public static WebDriver createBrowserDriverOnGrid(String browser) throws MalformedURLException{
		DesiredCapabilities capability = new DesiredCapabilities();
		switch(browser) {
			case "firefox":
				capability = DesiredCapabilities.firefox();
				break;
			case "chrome":
				capability = DesiredCapabilities.chrome();
				break;
			case "ie":
				capability = DesiredCapabilities.internetExplorer();
				capability.setCapability("ie.forceCreateProcessApi", true);
				capability.setCapability("ie.browserCommandLineSwitches", "-private");
				capability.setCapability("ie.ensureCleanSession", true);
				capability.setCapability("ie.unexpectedAlertBehaviour", "ignore");
				break;
			default:
				System.err.println("Did not found matching browser name");
				break;
		}

		if(System.getProperty("HubIP") != null && !System.getProperty("HubIP").isEmpty())
			return new RemoteWebDriver(new URL("http://"+System.getProperty("HubIP")+":4444/wd/hub"), capability);
		else
			return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);
	}

	public static void createAndroidDriver(){
		if(generalDriver == null){
			try {
				generalDriver = new AndroidDriver<MobileElement>(new URL(AndroidUtilities.AndroidAppDriverUrl), AndroidUtilities.getAndroidCapabilities());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			generalDriver.manage().timeouts().implicitlyWait(AndroidUtilities.AndroidWaitTime, TimeUnit.SECONDS);
		}
	}

	public static void quit() {
		if(generalDriver != null) {
			generalDriver.quit();
		}
	}
}
