package com.Utilities.Android;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class AndroidUtilities {
	public final static long AndroidWaitTime = 5;
	public final static String AndroidAppDriverUrl = "http://127.0.0.1:4723/wd/hub";
	
	public static DesiredCapabilities getAndroidCapabilities(){
		// Path to Eclipse project
		File classpathRoot = new File(System.getProperty("user.dir"));
		
		// Path to <project folder>/Apps -> Amazon
		File appDir = new File(classpathRoot, "/Apps/Amazon/");
		
		// Path to <project folder>/Apps -> Amazon/Amozon apk file
		File app = new File(appDir, "in.amazon.mShop.android.shopping.apk");
		//File app = new File(appDir, "Amazon_App.apk");
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("appPackage", (System.getProperty("appPackage") != null && !System.getProperty("appPackage").isEmpty()) ? System.getProperty("appPackage") : "com.amazon.mShop.android");
		capabilities.setCapability("appActivity", (System.getProperty("appActivity") != null && !System.getProperty("appActivity").isEmpty()) ? System.getProperty("appActivity") : "com.amazon.mShop.home.web.MShopWebGatewayActivity");
		capabilities.setCapability("browserName", (System.getProperty("browserName") != null && !System.getProperty("browserName").isEmpty()) ? System.getProperty("browserName") : "");

		capabilities.setCapability("noReset", (System.getProperty("noReset") != null && !System.getProperty("noReset").isEmpty()) ? System.getProperty("noReset") : "true");
		capabilities.setCapability("device", (System.getProperty("device") != null && !System.getProperty("device").isEmpty()) ? System.getProperty("device") : "Android");
		capabilities.setCapability("takesScreenshot", (System.getProperty("takesScreenshot") != null && !System.getProperty("takesScreenshot").isEmpty()) ? System.getProperty("takesScreenshot") : "true");
		
		capabilities.setCapability("platformVersion", (System.getProperty("platformVersion") != null && !System.getProperty("platformVersion").isEmpty()) ? System.getProperty("platformVersion") : "4.4.2");
		capabilities.setCapability("platformName", (System.getProperty("platformName") != null && !System.getProperty("platformName").isEmpty()) ? System.getProperty("platformName") : "Android");
		capabilities.setCapability("deviceName", (System.getProperty("deviceName") != null && !System.getProperty("deviceName").isEmpty()) ? System.getProperty("deviceName") : "MicromaxA311");
		capabilities.setCapability("app", (System.getProperty("app") != null && !System.getProperty("app").isEmpty()) ? System.getProperty("app") : app.getAbsolutePath());
		return capabilities;
	}

}
