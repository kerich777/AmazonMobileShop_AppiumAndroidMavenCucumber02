package com.Utilities.General;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.GregorianCalendar;

import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.ServiceException;
//import com.holistictech.automation.infrastructure.utilities.PostToGoogleSheets.PostResults;
//import com.holistictech.automation.infrastructure.utilities.testrail.MainClass;

import cucumber.api.Scenario;

public class Utilities {
	
	private static final Logger LOGGER = Logger.getLogger(Utilities.class.getName());
	public static Map<String, String> ApplicationTranslations = null;

	/**
	 * Formats the given name as 'Last name, First name'.
	 *
	 * @param  name  The name to format. Example: 'Bob Adams'
	 * @return       String of the formatted name
	 */
	public static String formatAsLastNameFirstName(String name){
		//when name format as "first last"
		if (name.indexOf(",")== -1)
		{
			String nameFormatted[] = name.split(" ");
			return nameFormatted[1] + ", " + nameFormatted[0];
		}
		else
			return name;
	}

	/**
	 * Formats the given name as 'First name Last name'.
	 *
	 * @param  name  The name to format. Excepts a ',' in the name. Example: 'Adams, Bob'
	 * @return       String of the formatted name
	 */
	public static String formatAsFirstNameLastName(String name){
		if (name.indexOf(",")!= -1)
		{
			name = name.replace(",", "");
			String nameFormatted[] = name.split(" ");
			String correctedName = "";
			
			for(int i = 1; i < nameFormatted.length; i++)
				correctedName += nameFormatted[i] + " ";
			
			return correctedName + nameFormatted[0];
		}
		else
		{
			//name is already in format "firstName lastName"
			return name;
		}

	}

	/**
	 * Updates the resource value where '{}' exists with the new values.
	 * Example: resource = {0} of {1} accounts were selected. newValues = 3,5. Updated resource = 3 of 5 accounts were selected.
	 *
	 * @param  resourceString  The resource value that will be updated with the new values
	 * @param  newValues       List of the new values to replace in the resource value string. They are placed in order.
	 * @return                 String of the updated resource value
	 */
	public static String updateResourceBracesWithDesiredValues(String resourceString, List<String> newValues){
		for(int i = 0; i < newValues.size(); i++)	
			resourceString = resourceString.replace("{" + i + "}", newValues.get(i));
		return resourceString;
	}
	
	/**
	 * Returns the robot resource files based on the platform and the test tags that specify the components.
	 *
	 * @param  resourcePagesToLoad  A List of the tags from the test. Will load the resources based on the components listed.
	 * @param  platform             The platform the test is running against. Will load resources based on the platform.
	 * @param  pathToLibraries      The path to '/src/Libraries'
	 * @return                      ArrayList of the necessary robot resource files to load.
	 * TODO:                        Update it to only load the exact page resources instead of the all the resources that are under a component tag.
	 */
	public static ArrayList<String> getRunTimeResourcePages(List<String> resourcePagesToLoad, String platform, String pathToLibraries){
		ArrayList<String> pathsToLoad = new ArrayList<String>();
		ArrayList<String> FullPathsToLoad = new ArrayList<String>();
		File file = new File(pathToLibraries);

		//Recursively get all the paths by platform & component.
		getPlatformDirectories(file, pathsToLoad, resourcePagesToLoad, platform);
		String separator = System.getProperty("file.separator");

		//Loop through the files under the paths and only store the '.robot' resources.
		for(int i = 0; i < pathsToLoad.size(); i++){
			File resourcePages = new File(pathsToLoad.get(i));
			String[] names = resourcePages.list();
			for(String name : names){
				//Ignore the java files & the platform config file since that one is already loaded. Reloading the config file could override variables set through command line.
				if(!name.contains(".java") && !name.contains(platform + "Configs.robot"))
					FullPathsToLoad.add(pathsToLoad.get(i) + separator + name);
			}
		}
		return FullPathsToLoad;
	}

	/**
	 * Recursively get all the paths that correspond to the test's platform & components.
	 *
	 * @param  dir            File that starts at '/src/Libraries' then updates as it looks for other directories.
	 * @param  pathsToLoad    Empty ArrayList that will fill with the desired paths to the resources.
	 * @param  componentTags  List of the test tags. Will use these tags as well as the platform to grab the paths by.
	 * @param  platform       String of the platform the tests runs on. Will use these tags as well as the platform to grab the paths by.
	 * @return                Void. But sets pathsToLoad with the needed paths to load.
	 */
	public static void getPlatformDirectories(File dir, ArrayList<String> pathsToLoad, List<String> componentTags, String platform) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					//Check for component/platform. Ex) AE/Online.
					if(file.getCanonicalPath().contains(platform)){
						for(int i = 0; i < componentTags.size(); i++){
							if(file.getCanonicalPath().contains(componentTags.get(i)) && !componentTags.get(i).equals(platform)){
								pathsToLoad.add(file.getCanonicalPath());
							}
						}
					}
					//Check for Utilities/platform.
					if(file.getCanonicalPath().contains(platform) && file.getCanonicalPath().contains("Utilities"))
						pathsToLoad.add(file.getCanonicalPath());
					//Check for Utilities/General.
					if(file.getCanonicalPath().contains("Utilities") && file.getCanonicalPath().contains("General"))
						pathsToLoad.add(file.getCanonicalPath());
					getPlatformDirectories(file, pathsToLoad, componentTags, platform);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Replaces the 'XXX' within given String and replaces it with the new value.
	 *
	 * @author Tyler Roddan
	 * @param  value        The String of an element which contains 'XXX' that will be replaced.
	 *         replaceWith  The string to replace 'XXX' with
	 * @return              String of updated value
	 */
	public static String replaceXXXFromString(String value, String replaceWith){
		return value.replaceFirst("XXX", replaceWith);
	}

	/**
	 * Replaces the 'XXX' within given By locator and replaces it with the new value.
	 * Does this by turning the By into a string, getting the substring of the path, replacing the 'XXX', & recreating the By
	 * based on its findby method.
	 *
	 * @param  locator      By locator to update
	 * @param  replaceWith  String to replace 'XXX' with
	 * @return              By which is the newly updated By with its path
	 */
	public static By replaceXXXFromByLocator(By locator, String replaceWith) {
		String elementDescription = locator.toString().substring(locator.toString().lastIndexOf(": ")+2, locator.toString().length()); //locator.toString().lastIndexOf("]")+1);
		if(locator.toString().contains("xpath:"))
			return By.xpath(elementDescription.replace("XXX", replaceWith));
		else if(locator.toString().contains("id:"))
			return By.id(elementDescription.replace("XXX", replaceWith));
		else if(locator.toString().contains("className:"))
			return By.className(elementDescription.replace("XXX", replaceWith));
		else if(locator.toString().contains("cssSelector:"))
			return By.cssSelector(elementDescription.replace("XXX", replaceWith));
		else if(locator.toString().contains("linkText:"))
			return By.linkText(elementDescription.replace("XXX", replaceWith));
		else if(locator.toString().contains("name:"))
			return By.name(elementDescription.replace("XXX", replaceWith));
		else if(locator.toString().contains("partialLinkText:"))
			return By.partialLinkText(elementDescription.replace("XXX", replaceWith));
		else
			return By.tagName(elementDescription.replace("XXX", replaceWith));
	}

	/**
	 * Appends the abbreviated language to the username. Example: 'sjonesfr'
	 *
	 * @param  username      The username to modify
	 *         userLanguage  The language that the test will run against
	 * @return               The updated username with the abbreviated language Appended
	 */
	public static String setUserNameByLanguage(String username, String userLanguage){
		if(userLanguage.equalsIgnoreCase("fr"))
			return username + "fr";
		else
			return username;
	}

	/**
	 * Appends the abbreviated language to the full username. Example: 'Sarah JonesFR'
	 *
	 * @param  fullUsername      The full username to modify
	 *         userLanguage      The language that the test will run against
	 * @return                   The updated full username with the abbreviated language Appended
	 */
	public static String setFullUserNameByLanguage(String fullUsername, String userLanguage){
		if(userLanguage.equalsIgnoreCase("fr"))
			return fullUsername + "FR";
		else
			return fullUsername;
	}

	/**
	 * Returns the java keyword resource files based on the platform and the test tags that specify the components.
	 *
	 * @param  resourcePagesToLoad  A List of the tags from the test. Will load the resources based on the components listed.
	 * @param  platform             The platform the test is running against. Will load resources based on the platform.
	 * @param  pathToLibraries      The path to '/src/Libraries'
	 * @return                      ArrayList of the necessary java keyword resource files to load.
	 */
	public static ArrayList<String> getJavaRunTimeResources(List<String> resourcePagesToLoad, String platform, String pathToLibraries){

		String separator = System.getProperty("file.separator");
		pathToLibraries = pathToLibraries + separator + "src" + separator + "main" ;
		ArrayList<String> pathsToLoad = new ArrayList<String>();
		ArrayList<String> FullPathsToLoad = new ArrayList<String>();
		File file = new File(pathToLibraries);

		//Recursively get all the paths by platform & component.
		getPlatformAndProductDirectories(file, pathsToLoad, resourcePagesToLoad, platform);

		//Loop through the files under the paths and only store the 'java keywords' resources.
		for(int i = 0; i < pathsToLoad.size(); i++){
			File resourcePages = new File(pathsToLoad.get(i));
			String[] names = resourcePages.list();
			for(String name : names){
				//Get all the java files that contain 'Keywords' Ex) 'LoginOnlineKeywords'
				if(name.contains("Keywords"))
					FullPathsToLoad.add(pathsToLoad.get(i) + separator + name);
			}
		}

		//Transform the paths from 'C:\automation\autorobot\src\Libraries\Products\CRM\Account\Online\AccountOnlineKeywords.java'
		//to 'Products.CRM.Account.Online.AccountOnlineKeywords'
		for(int i = 0; i < FullPathsToLoad.size(); i++){
			FullPathsToLoad.set(i, FullPathsToLoad.get(i).replace(pathToLibraries, ""));
			FullPathsToLoad.set(i, FullPathsToLoad.get(i).replace("\\", "."));
			FullPathsToLoad.set(i, FullPathsToLoad.get(i).replace("/", "."));
			FullPathsToLoad.set(i, FullPathsToLoad.get(i).replaceFirst(".", ""));
			FullPathsToLoad.set(i, FullPathsToLoad.get(i).replace(".java", ""));
		}
		return FullPathsToLoad;
	}

	/**
	 * Recursively get all the paths that correspond to the test's platform & components.
	 *
	 * @param  dir            File that starts at '/src/Libraries' then updates as it looks for other directories.
	 * @param  pathsToLoad    Empty ArrayList that will fill with the desired paths to the resources.
	 * @param  componentTags  List of the test tags. Will use these tags as well as the platform to grab the paths by.
	 * @param  platform       String of the platform the tests runs on. Will use these tags as well as the platform to grab the paths by.
	 * @return                Void. But sets pathsToLoad with the needed paths to load.
	 */
	public static void getPlatformAndProductDirectories(File dir, ArrayList<String> pathsToLoad, List<String> componentTags, String platform) {
		try {
			//Sets the product based on the given tag Ex) CRM_PRODUCT -> CRM
			String product = "";
			for(String list : componentTags)
				if(list.contains("_PRODUCT"))
					product = list.replace("_PRODUCT", "");


			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {

					//Check for product/component/platform. Ex) CRM/AE/Online.
					if(file.getCanonicalPath().contains(platform)){
						for(int i = 0; i < componentTags.size(); i++){
							if(file.getCanonicalPath().contains(componentTags.get(i)) /*&& file.getCanonicalPath().contains(product)*/){
								pathsToLoad.add(file.getCanonicalPath());
							}
						}
					}

					//Check for Utilities/platform. Ex) Utilities/Online
					if(file.getCanonicalPath().contains(platform) && file.getCanonicalPath().contains("Utilities"))
						pathsToLoad.add(file.getCanonicalPath());
					//Check for Utilities/General.
					if(file.getCanonicalPath().contains("Utilities") && file.getCanonicalPath().contains("General"))
						pathsToLoad.add(file.getCanonicalPath());
					//Get all files under product/General/platform Ex) CRM/General/Online/Login
					if(file.getCanonicalPath().contains(product) && file.getCanonicalPath().contains("General") && file.getCanonicalPath().contains(platform))
						pathsToLoad.add(file.getCanonicalPath());
					getPlatformAndProductDirectories(file, pathsToLoad, componentTags, platform);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stores a map that contains the application translations.
	 *
	 * @param  userLanguage  The user language the script is testing against.
	 * @return               Nothing.
	 */
	public static void loadAppTranslationResources(String userLanguage) throws IOException{
		ApplicationTranslations = CSVReader1.getSystemResource(userLanguage);
	}

	/**
	 * Returns the map of the application translations.
	 *
	 * @return  Map, with key = resource key & value = translation value.
	 */
	public static Map<String, String> getAppTranslationResources(){
		return ApplicationTranslations;
	}

	public static String getWorkingDirectory() {
		String path = Utilities.class.getClassLoader().getResource("").getPath();
		return path.split("target")[0];
	}

	/**
	 * Formats the given date in the correct locale format based on the 'USERLANGUAGE' variable.
	 * The format is short (5/18/2016 for US, 18/05/2016 for French).
	 *
	 * @param  date  The date to format. Can accept {today}, {tomorrow}
	 * @return       String of the formatted date 
	 */
	public String formatShortDateBasedOnLanguage(String date){
		Locale locale = null;
		//TODO: add more locales when needed.
		if(System.getProperty("USERLANGUAGE").equals("en_US"))
			locale = Locale.US;
		else if(System.getProperty("USERLANGUAGE").equals("fr"))
			locale = Locale.FRANCE;

		//TODO: add more cases ex) passing in specific date, etc.
		if(date.equalsIgnoreCase("{today}")){
			//Get short date format(ex: 5/19/16) based on locale. Get todays date
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
			Date today = new Date();

			//Format the date with the full year(ex: 16 -> 2016)
			SimpleDateFormat formatter = (SimpleDateFormat) dateFormat;  
			String pattern = formatter.toPattern().replaceAll("y+","yyyy");
			formatter.applyPattern(pattern); 
			return formatter.format(today);
		}
		else if(date.equalsIgnoreCase("{tomorrow}")){
			//Get short date format(ex: 5/19/16) based on locale. Get todays date.
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
			Date tomorrow = new Date();

			//Adds one day to 'tomorrow'
			Calendar calender = Calendar.getInstance(); 
			calender.setTime(tomorrow); 
			calender.add(Calendar.DATE, 1);
			tomorrow = calender.getTime();

			//Format the date with the full year(ex: 16 -> 2016)
			SimpleDateFormat formatter = (SimpleDateFormat) dateFormat;  
			String pattern = formatter.toPattern().replaceAll("y+","yyyy");
			formatter.applyPattern(pattern); 
			return formatter.format(tomorrow);
		}
		else if(date.equalsIgnoreCase("{year}")){
			//Get short date format(ex: 5/19/16) based on locale. Get todays date.
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
			Date nextYear = new Date();

			//Adds one year to 'nextYear'
			Calendar calender = Calendar.getInstance(); 
			calender.setTime(nextYear); 
			calender.add(Calendar.YEAR, 1);
			nextYear = calender.getTime();

			//Format the date with the full year(ex: 16 -> 2016)
			SimpleDateFormat formatter = (SimpleDateFormat) dateFormat;  
			String pattern = formatter.toPattern().replaceAll("y+","yyyy");
			formatter.applyPattern(pattern); 
			return formatter.format(nextYear);
		}
		return "";
	}

	/**
	 * If the given String is surrounded by quotes, will remove the surrounding quotes
	 * Note: only works if " is the first and last character of the string
	 *
	 * @param  data  The String to be stripped
	 * @return       String without quotes, original string if errors occured
	 */
	public String stripQuotesFromAroundString(String data)
	{
		if(data.charAt(0) == '"' && data.charAt(data.length()-1) == '"')
		{
			data = data.substring(1, data.length()-1);
		}
		return data;
	}

	/**
	 * Finds the absolute path to the given resource file name.
	 * @param fileName The name of the file to find in the test resources folder.
	 * @return the absolute path to the given file name. Otherwise, returns null.
	 */
	public static String getResourceFilePath(String fileName) {
		String workingDirectory = getWorkingDirectory();
		String fileSeparator = System.getProperty("file.separator");
		String featureFilesPath = workingDirectory + fileSeparator + "src" + fileSeparator + 
				fileSeparator + "test" + fileSeparator + "resources";

		File directory = new File(featureFilesPath);
		return getFullFilePath(directory, fileName);
	}

	/**
	 * Searches from the given directory for the absolute path of the given file name.
	 * @param directory  The directory where search should start.
	 * @param fileName   The file name to find in the given directory.
	 * @return the absolute path of the given file name. Otherwise, returns null.
	 */
	public static String getFullFilePath(File directory, String fileName) {
		String filePath = null;
		try {

			String platform = System.getProperty("platform");
			File[] files = directory.listFiles();
			for (File file : files) {
				if (!file.isDirectory()) {

					if(file.getCanonicalPath().contains(platform) && 
							file.getName().equals(fileName)) {
						filePath = file.getCanonicalPath();
						break;
					}
				}else {
					filePath = getFullFilePath(file, fileName);
					if(filePath != null) {
						return filePath;
					}
				}
			}


		} catch (IOException e) {
			e.printStackTrace();
		}

		return filePath;
	}

	/**
	 * Helper method which calls TestData.set(). It determines if the data should or should not be stored, as well as
	 * appending a random string into its data.
	 * 
	 * @param  component  Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute  Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @param  data       The value to store.
	 * @return            String of the data, whether it be unchanged or newly created.
	 */
	public String setDynamicData(String component, String attribute, String data){
		if(!data.contains("{")) return data;

		if(data.contains("{randnum}")){
			TestData testData = TestData.getInstance();
			String newData = replaceRandnumWithDigits(data);
			Assert.assertTrue(testData.set(component, attribute, newData));
			return newData;
		}

		return null;
	}

	/**
	 * Helper method which calls TestData.set(). It determines if the data should or should not be stored. 
	 * data can include an index immediately preceding the '_'. 
	 * For example value1_# and value2_# can be used to store a second version with the same component and attribute. 
	 * 
	 * @param  component  Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute  Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @param  data       The value to store.
	 * @return            String of the data, whether it be unchanged or newly created.
	 */	
	public String setTestData(String component, String attribute, String data){
		TestData testData = TestData.getInstance();
		if(data.contains("{randnum}")){
			data = replaceRandnumWithDigits(data);
		}
		
		
		Assert.assertTrue(testData.set(component, attribute, data));
		return data;

	}

	/**
	 * TestData allows the entry of data with a prefix, for example "prefix_value"
	 * This function strips off the prefix and the '_' returning the value you actually care about
	 * 
	 * @param valueWithPrefix
	 * @return
	 */
	public String removeTestDataPrefix(String valueWithPrefix){
		String temp[] = valueWithPrefix.split("_");
		return temp[1];
	}

	/**
	 * Helper method which calls TestData.get(). It determines if the data exists in TestData and returns the value.
	 * 
	 * @param  component  Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute  Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @param  data       The data to return. Acts as an alias.
	 * @return            String of the data.
	 */
	public String getDynamicData(String component, String attribute, String data){
		if(!data.contains("{")) return data;

		if(data.contains("{randnum}")){
			TestData testData = TestData.getInstance();
			String savedData = testData.get(component, attribute, data);
			Assert.assertNotNull(savedData);
			return savedData;
		}

		return null;
	}

	/**
	 * Helper method which calls TestData.get(). It determines if the data exists in TestData and returns the value.
	 * 
	 * @param  component  Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute  Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @param  data       The data to return. Acts as an alias.
	 * @return            String of the data.
	 */
	public String getTestData(String component, String attribute, String... data){
		TestData testData = TestData.getInstance();
		String savedData = null;
		//System.out.println("Current Data Table");
		
		if(data.length < 1){
			savedData = testData.get(component, attribute);	
		}
		else{
			savedData = testData.get(component, attribute, data[0]);
		}
		return savedData;
	}

	/**
	 * Helper method which calls TestData.get(). It determines if the data exists in TestData and returns true or false
	 * 
	 * @param  component  Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute  Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @param  data       The data to return. Acts as an alias.
	 * @return            String of the data.
	 */
	public boolean isTestDataStored(String component, String attribute, String... data){
		TestData testData = TestData.getInstance();
		String savedData = null;
		
		if(data.length < 1){
			savedData = testData.get(component, attribute);	
		}
		else{
			savedData = testData.get(component, attribute, data[0]);
		}
		if (savedData == null){
			return false;
		}
		else return true;
	}	
	
	/**
	 * Helper method which calls TestData.getAll().
	 * 
	 * @param  component  Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute  Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @return            ArrayList of the data.
	 */
	public ArrayList<String> getAllDynamicData(String component, String attribute){
		TestData testData = TestData.getInstance();
		return testData.getAll(component, attribute);
	}

	/**
	 * Replaces '{randnum}' with a String of random numbers ranging from 1000-9999.
	 * 
	 * @param  data  The data to update with random numbers.
	 * @return       String of the updated data.
	 */
	public String replaceRandnumWithDigits(String data){
		String rand = String.valueOf(ThreadLocalRandom.current().nextInt(1000,9999));
		return data.replaceAll("\\{randnum\\}", rand);
	}

	/**
	 * Gets the current time in x:xx am/pm format
	 * 
	 * @return       String of the current time
	 */
	public String getCurrentTime()
	{
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("h:mm a");

		return dateFormat.format(currentDate);
	}

	/**
	 * Gets the current day name in Monday/Tuesday... format
	 * 
	 * @author Shen Tian
	 * @return       String of the current day's name
	 */
	public String getCurrentDayName()
	{
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("EEEE");

		return dateFormat.format(currentDate);
	}

	/**
	 * Given a translation resource value in English, gives the translated equivalent
	 * 
	 * @param englishTranslation
	 * @return String of the translated equivalent from the resource files, null if translation could not be found
	 * @throws IOException 
	 */
	public String getStringTranslation(String translateMe) throws IOException {

		if(System.getProperty("USERLANGUAGE").equals("en_US"))
			return translateMe;
		else
		{
			Map<String, String> englishTranslations = CSVReader1.getSystemResource("en_US");

			String englishKey = getKeyFromValue(englishTranslations, translateMe);	 
			String translatedString = getAppTranslationResources().get(englishKey);
			return translatedString;					 
		}
	}

	/**
	 * Given a string containing decimal number, returns Number 
	 * 
	 * @param translateMe
	 * @return
	 * @author ademcooper
	 */
	public Number getDecimalTranslation(String translateMe){
		Number translatedDecimal = null;
		if(System.getProperty("USERLANGUAGE").equals("en_US")){
			NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
			try {
				translatedDecimal = nf.parse(translateMe);
			} catch (ParseException e) {
				System.err.println("Failed to parse string to an English number.");
				e.printStackTrace();
			}
		}
		else{
			NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);	
			try {
				translatedDecimal = nf.parse(translateMe);
			} catch (ParseException e) {
				System.err.println("Failed to parse string to a French number.");
				e.printStackTrace();
			}
		}
		return translatedDecimal;


	}

	/**
	 *  Returns the key associated with a given value in a given map
	 * 
	 * @param hm A map with String values and keys 
	 * @param value The value in the map that you want to search for
	 * @return
	 */
	public String getKeyFromValue(Map<String, String> hm, String value) {

		for (String s : hm.keySet()) 
		{
			String mapValue = hm.get(s);
			if (mapValue.equals(value)) 
				return s;
		}
		return null;
	}	

	/**
	 *  Swaps out an '*' if found in the given string and replaces it with the release type of either LR or GR.
	 * 
	 * @author Tyler Roddan
	 * @param valueToUpdate  The value to swap out the '*' for the release type, is found
	 * @return               The updated String
	 */
	public String replaceAsteriskWithReleaseType(String valueToUpdate) {
		if(valueToUpdate.contains("*"))
			valueToUpdate = valueToUpdate.replace("*", System.getProperty("RELEASE_TYPE"));
		return valueToUpdate;
	}	

	/**
	 * Helper function that compares two List<string> objects 
	 * 
	 * @param expectedList
	 * @param actualList
	 * @return
	 * @author ademcooper
	 */
	public boolean doListsMatch(List<String> expectedList, List<String> actualList){
		boolean itemFound = false;
		if(expectedList.size() == actualList.size()){
			for(String temp : expectedList){
				if(actualList.contains(temp)){
					itemFound = true;
				}
			}
		}
		return itemFound;
	}

	/**
	 * Constructs a map from data table strings.  Standard format is "Key:Value" seperated by "," for each item in Map
	 * EXAMPLE: Product:TestPR 1,Quantity:10
	 * @param info  String to be converted to map
	 * @return
	 */
	public static Map<String, String> buildMap(String info){
		Map<String, String> infoHash = new HashMap<String, String>();
		// seperate string into array
		String[] pairList = info.split(",");
		String[] pair;

		// loop through pairs and add to hash
		for (String p : pairList) {
			//System.out.println("pair : " + p);
			pair = p.split(":");
			infoHash.put(pair[0], pair[1]);
		}
		return infoHash;
	}

	public static String convertNumberToMonth(String monthDayYear){
		String monthString;
		switch (monthDayYear) {
		case "01":  monthString = "January";
		break;
		case "02":  monthString = "February";
		break;
		case "03":  monthString = "March";
		break;
		case "04":  monthString = "April";
		break;
		case "05":  monthString = "May";
		break;
		case "06":  monthString = "June";
		break;
		case "07":  monthString = "July";
		break;
		case "08":  monthString = "August";
		break;
		case "09":  monthString = "September";
		break;
		case "10": monthString = "October";
		break;
		case "11": monthString = "November";
		break;
		case "12": monthString = "December";
		break;
		default: monthString = "Invalid month";
		break;
		}
		return monthString;
	}

	/**
	 * A general function to create test rail plan for current running
	 * automation tests.
	 *
	 * @author  Jose Melendez Castro
	 * @return  nothing
	 */
//	public static void createTestRailTestPlan() {
//
//		try {
//
//			String postToTestRail = System.getProperty("postToTestRail");
//
//			if(postToTestRail != null && postToTestRail.equalsIgnoreCase("false")) {
//				LOGGER.debug("Posting to test rail is disable. Skipping test rail test plan creation.");
//				return;
//			}
//
//			String platform = System.getProperty("platform");
//
//			if(platform.equalsIgnoreCase("Align")) {
//				platform = System.getProperty("alignPlatform");
//			}
//			String milestone = System.getProperty("testRailMilestone");
//			String project = System.getProperty("testRailProject");
//			String configurations = System.getProperty("testRailTestPlanConfigurations");
//
//			String[] testRailArgs = new String[] {
//					"-c", project
//					, platform, milestone
//					,configurations
//			};
//			MainClass.main(testRailArgs);
//		}catch(Exception e) {
//			LogMF.info(LOGGER, "Fail to create test plan. Error: {0}.", e.getMessage());
//		}
//	}
//
//	/*
//	 * Posts scenario results to Test Rail
//	 * Notes: In order to post results, the following System Property values are required:
//	 * platform, version, USERLANGUAGE, browser(online)
//	 * 
//	 * @param scenario The scenario that we want to report
//	 */
//	public void postTestRailResults(Scenario scenario)
//	{
//		String caseID;
//		String postToTestRail = System.getProperty("postToTestRail");
//
//		if(postToTestRail != null && postToTestRail.equalsIgnoreCase("false")) {
//			LOGGER.debug("Posting to test rail is disable. To enable posting to test rail set "
//					+ "system property postToTestRail to true.");
//			return;
//		}
//
//		try {	
//			//Getting the various fields needed for posting
//			String platform = getPlatform();
//			String milestone = getMilestone();
//			String testplan_name = getTestPlanName();
//			String savedID = getTestData("Utilities", "id");
//			if(savedID == null)
//				caseID = getScenarioCaseID(scenario, platform).toString();
//			else
//				caseID = savedID;
//	
//			String config = getTestRailConfig(platform);
//			String testCaseStatus = scenario.getStatus().equalsIgnoreCase("Passed") ? "Passed" : "Failed";
//			String comment = "";//Not sure how/where to get this
//
//			String [] testRailArgs;
//			if(comment == "") {
//				testRailArgs = new String[] {"-p",platform, milestone, caseID, config, testCaseStatus};
//			}
//			else {
//				testRailArgs = new String[] {"-p",platform, milestone, caseID, config, testCaseStatus, comment};
//			}
//
//				/*
//				System.out.println("Posting results to TestRail:");
//				System.out.println("TestPlan_Name:" + testplan_name);
//				System.out.println("Case ID:" + caseID);
//				System.out.println("Config:" + config);
//				System.out.println("Status:" + testCaseStatus);
//				System.out.println("failMessage:" + comment);
//			 	*/
//			MainClass.main(testRailArgs);
//
//		} catch (Exception e) {
//			LogMF.info(LOGGER, "Fail to post to test rail. Error {0}.", e.getMessage());
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	/**
	 * Gets the current platform for current automation run.
	 *
	 * @author  Jose Melendez Castro
	 * @return  String
	 */
	public static String getPlatform() {
		String platform = System.getProperty("platform");

		if(platform.equalsIgnoreCase("Align")) {
			platform = System.getProperty("alignPlatform");
		}

		return platform;
	}

	/*
	 * Gets the major milestone from given version
	 * 
	 * Note: This is just temporary till we can get the test rail code/jar into Nexus and get the Maven dependencies to work correctly  
	 * Not meant to be permanent solution
	 * @return the milestone on TestRail that you want to commit too
	 * 
	 */
	public static String getMilestone()
	{
		String milestone = System.getProperty("testRailMilestone");

		if(milestone != null) {
			return milestone;
		}

		String version = System.getProperty("version");
		String[] splitVersion = version.split(Pattern.quote("."));
		if(splitVersion.length == 3)//Prod, 27.0.1, no t/c build
			return version;
		else
			return version.substring(0, version.lastIndexOf("."));
	}

	/**
	 * Gets the Test Rail plan name from System property.
	 *
	 * @author  Jose Melendez Castro
	 * @return  String
	 */
	public static String getTestPlanName() {
		String testPlanName = System.getProperty("testRailPlanName");

		if(testPlanName != null) {
			return testPlanName;
		}

		testPlanName = getPlatform() + " " +  getMilestone() ;
		return null;
	}

	/*
	 * Depending on the OS, gets the location of the TestRail Jar
	 * 
	 * Note: This is just temporary till we can get the test rail code/jar into Nexus and get the Maven dependencies to work correctly  
	 * Not meant to be permanent solution
	 * @return The full path of the TestRail jar
	 */
	public static String getTestRailJarLocation()
	{
		String testRailLocation = null;

		if(isWindows())
			testRailLocation = "C:/automation/infrastructure/utilities/TestRail.jar";
		else
			testRailLocation = System.getProperty("user.home") + "/Desktop/automation/infrastructure/utilities/TestRail.jar";

		return testRailLocation;
	}

	/*
	 * Parses the case id from the scenario tags
	 * Supports either @1234567890 or @OL_1234567890 format if the test is multiplatform
	 * 
	 * @param scenario The scenario that we are getting the case id from
	 * @param The platform that we are running/want to report results too
	 * @retun long the case id 
	 */
	public static String getScenarioCaseID(Scenario scenario, String platform)
	{
		String generalRegex = "@C\\d+";
		String nonPlatRegex = "^@\\d+";		
		String platRegex = "^@";

		switch (platform) {		
		case "API":
			platRegex = platRegex + "API_\\d+";
			break;
		case "iPad":
			platRegex = platRegex + "iPad_\\d+";
			break;					
		case "Online":
			platRegex = platRegex + "OL_\\d+";
			break;
		case "WM":
			platRegex = platRegex + "WM_\\d+";
			break;
		default:
			platRegex= platRegex + "NOPLATFORMSPECIFIED";
			break;
		}

		for(String tagName : scenario.getSourceTagNames())
		{					
			if(tagName.matches(nonPlatRegex)) {
				return tagName.substring(1);
			}
			else if(tagName.matches(platRegex)) {
				return tagName.substring(tagName.indexOf('_')+1);
			}else if(tagName.matches(generalRegex))	 {
				return tagName.substring(2).trim();
			}
		}
		return null;
	}

	/*
	 *	Gets a TestRail friendly representation of the browser being tested
	 *
	 * @return String the browser that you are testing on
	 */
	public static String getTestRailBrowser()
	{
		switch(System.getProperty("browser")) {
		case "firefox":
			return "FF";
		case "chrome":
			return "Chrome";
		case "IE7":
			return "IE7";
		case "IE8":
			return "IE8";
		case "IE9":
			return "IE9";
		case "IE10":
			return "IE10";
		default:
			return null;	
		}
	}

	/*
	 *	Gets a the testrail config code that you want to report the test case too
	 *	Test Case Jar requires that it be in the following format:
	 *  Mode:Language:OS
	 *  The currently available fields are:
	 *  Language: English, French, Psuedo
	 *  Mode: Chrome, DEV, FF, IE10, IE7, IE8, IE9, Multicurrency, Safari, SIM
	 *  OS: IOS6,IOS7,IOS8,IOS9,iRep,UNKNOWN,W10,W7,W8,Win Modern, XP
	 *
	 * @param platform the platform that you are running your test against
	 * @return String the browser that you are testing on
	 */
	public static String getTestRailConfig(String platform)
	{		

		if(System.getProperty("testRailTestPlanConfigurations") != null) {
			return System.getProperty("testRailTestPlanConfigurations");
		}

		StringBuilder sb = new StringBuilder();
		String os = "";

		switch (platform) {		
		case "API":
			sb.append("DEV:");
			os = "UNKNOWN";
			break;
		case "iPad"://will need to update this as more iPad functionality comes along and we are able to determine IOS version
			sb.append("SIM:");
			os = "UNKNOWN";
			break;					
		case "Online":
			sb.append("IE9:");
			os = "W7";
			break;
		case "WM":
			sb.append("SIM:");
			os = "W8";
			break;
		default:
			break;
		}		

		if(System.getProperty("USERLANGUAGE").equals("en_US"))
			sb.append("English:");
		else if(System.getProperty("USERLANGUAGE").equals("fr"))
			sb.append("French:");
		else
			sb.append("Psuedo:");

		sb.append(os);

		//System.out.println("Config:" + sb.toString());
		return sb.toString();
	}

	/*
	 * Posts results to Google Sheets. 
	 * Note: This only works for pod testing as the sheet name is hard-coded to 'SanityPodSmoke'.
	 *
	 * @author  Tyler Roddan
	 * @param   scenario  The scenario that of the result to be posted.
	 * @return  Nothing
	 */
//	public static void postToGoogleSpreadsheets(Scenario scenario) throws InvalidEntryException, AuthenticationException, ServiceException, Exception{
//		String caseID = getScenarioCaseID(scenario, System.getProperty("platform"));
//		String googleSpreadsheetName = "SanityPodSmoke";
//		String testCaseStatus = scenario.getStatus().equalsIgnoreCase("Passed") ? "Passed" : "Failed";
//		String columnHeader = System.getProperty("version") + "_" + System.getProperty("org");
//
//		String[] postingArguments = {caseID, columnHeader, testCaseStatus, googleSpreadsheetName};
//		PostResults.main(postingArguments);
//	}

	public static String getOsName()
	{
		return System.getProperty("os.name");
	}

	public static boolean isWindows()
	{
		return getOsName().startsWith("Windows");
	}		


	public static String getDate(int monthOffset, int dayOffset, int yearOffset, boolean... reverseDateFormat) {

		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/Los_Angeles"));

		if(reverseDateFormat.length > 0) {
			if(reverseDateFormat[0]) {
				if(calendar.get(Calendar.MONTH) + monthOffset + 1 > 12) {
					return (calendar.get(Calendar.YEAR) + 1) + "-" + ((calendar.get(Calendar.MONTH) + monthOffset + 1) % 12) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) + dayOffset);
				}
				return (calendar.get(Calendar.YEAR) + yearOffset) + "-" + (calendar.get(Calendar.MONTH) + monthOffset + 1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) + dayOffset);
			}
		}

		if(calendar.get(Calendar.MONTH) + monthOffset + 1 > 12) {
			return ((calendar.get(Calendar.MONTH) + monthOffset + 1) % 12) + "/" + (calendar.get(Calendar.DAY_OF_MONTH) + dayOffset) + "/" + (calendar.get(Calendar.YEAR) + 1);
		}
		return (calendar.get(Calendar.MONTH) + monthOffset + 1) + "/" + (calendar.get(Calendar.DAY_OF_MONTH) + dayOffset) + "/" + (calendar.get(Calendar.YEAR) + yearOffset);

	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0;
	}

	/**
	 * Generates a throwaway alpha string.
	 * @author Julie Phakoom
	 * 
	 * @param length - Length of string to generate
	 * @return Random string.
	 */
	public static String getRandomString(int length) {
		String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		if( length < 1 ) length = 1;

		String randomString = "";
		for( int i = 0; i < length; ++i ) {
			randomString += letters.charAt(ThreadLocalRandom.current().nextInt(0, letters.length()));
		}

		return randomString;
	}

	/**
	 * Generates a throwaway alpha string of default length 10.
	 * @author Julie Phakoom
	 * 
	 * @return Random string.
	 */
	public static String getRandomString() {
		return getRandomString(10);
	}
}
