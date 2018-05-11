package com.Utilities.General;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

public class TestData {
	private static TestData instance = null;
	protected static final Logger logger = Logger.getLogger(TestData.class.getName());

	//The testdata, which is a map of the component keys-> corresponding values are a list of maps containing attribute keys and their dynamic values.
	private static Map<String, List<HashMap<String, String>>> testData = new HashMap<String, List<HashMap<String, String>>>();
	private static HashMap<String, String> attributes = new HashMap<String, String>();
	
	//Static hash map used to keep track of orderlines for OL.  Key: Product Value:Line number
	public static HashMap<String, String> orderLines = new HashMap<String, String>();	
	
	//Hardcoded data keys & attribute types. Add more as needed.
	private final static String [] attributeTypes = {"name", "id", "notes", "activityActual", "teamActivityActual", "dayOfWeek", "callTime", "date", "orderLineCount", "size", "attendee", "count", "datetime", "address", "website"};
	private final static String [] testDataKeys   = {"Align", "CyclePlan", "Account", "MCCPSync", "MCCP", "MCCP-Target", "MCCP-Channel", "MCCP-Product", "CallReport", "EM", "ME", "Schedule","OM", "Network", "Utilities", "CLM"};

	
	private TestData(){
		
	}
	
	/**
	 * Return the instance of TestData. If TestData is already created, it will return itself, otherwise
	 * it will instantiate itself(TestData), attributeTypes, & testDataKeys then return.
	 *
	 * @return  The instance of TestData for the life span of the scenario.
	 */
	 public static TestData getInstance() {
        if (instance == null) {
        	for(int i = 0; i < attributeTypes.length; i++)
    			attributes.put(attributeTypes[i], "");

    		for(int i = 0; i < testDataKeys.length; i++)
    			testData.put(testDataKeys[i], new ArrayList<HashMap<String, String>>());
    		
            instance = new TestData();
        }
        return instance;
    }
	 
	 /**
	 * Sets the TestData instance to null.
	 *
	 * @return  Nothing.
	 */
	 public void nullTestData(){
		 instance = null;
	 }
	
	 /**
	 * Stores the given value in testData. For setting the desired attributeValue, the component & attribute are used to correctly
	 * identify the value in testData, as well as the value itself.
	 * 
	 * @param  component       Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute       Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @param  attributeValue  The value to store.
	 * @return                 Boolean.
	 */
	public boolean set(String component, String attribute, String attributeValue){
		
		//Verify that the testData key & attribute type is valid
		if(!areTestDataKeysAndAttributesValid(component,attribute)) return false;
		
		// Check for Index in attributeValue to enable indexValueMode
		boolean indexValueMode = false;
		char digitCheck = 'a';
		if (attributeValue.contains("_")){
			digitCheck = attributeValue.charAt(attributeValue.indexOf("_")-1);
			if(Character.isDigit(digitCheck)){
				indexValueMode = true;
			}
		}
		
		//Check if given attributeValue has a number (ex: account1_467), which would dictate where to place it in the testdata list by position not index(position 1 = index 0).
		if(indexValueMode == true){
			int listPosition = Character.getNumericValue(digitCheck);
			//Check if the index digit from attributeValue is greater than the size of testdata. If so, need to append another map to the list.
			if(listPosition > testData.get(component).size()){
				//Verify that the index digit from attributeValue is only 1 bigger than size of the list.
				if(listPosition != (testData.get(component).size() + 1)){
					LogMF.debug(logger, "Cannot add " + attributeValue + " with index of " + (listPosition-1) + 
							" since testData's size is " + testData.get(component).size(), Thread.currentThread().getStackTrace()[1].getMethodName());
					return false;
				}
				HashMap<String, String> attributesCopy = new HashMap<String, String>();
				attributesCopy.putAll(attributes);
				testData.get(component).add(attributesCopy);
				LogMF.debug(logger, "Adding attributes map to testDataKey '" + component + "'", Thread.currentThread().getStackTrace()[1].getMethodName());
			}

			testData.get(component).get(listPosition-1).put(attribute, attributeValue);
			LogMF.debug(logger, "Setting: testDataKey = " + component + ". Key = " + attribute + ", Value = " + attributeValue + 
					". Storing map in List position " + listPosition, Thread.currentThread().getStackTrace()[1].getMethodName());
			return true;
		}
		//When no index digits are given, the map and values will be updated in the first map in the list.
		else{
			//If testData is empty, then add the attributes map to the list
			if(testData.get(component).size() == 0){
				HashMap<String, String> attributesCopy = new HashMap<String, String>();
				attributesCopy.putAll(attributes);
				testData.get(component).add(attributesCopy);
				LogMF.debug(logger, "Adding attributes map to testDataKey '" + component + "'", Thread.currentThread().getStackTrace()[1].getMethodName());
			}

			testData.get(component).get(0).put(attribute, attributeValue);
			LogMF.debug(logger, "Setting: testDataKey = " + component + ". Key = " + attribute + ", Value = " + attributeValue + 
					". Storing map in List position 0.", Thread.currentThread().getStackTrace()[1].getMethodName());
			return true;
		}
	}
	
	/**
	 * Returns the given value in testData. For returning the desired attributeValue, the component & attribute are used to correctly
	 * identify the value in testData, as well as the value itself.
	 * 
	 * @param  component       Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute       Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @param  attributeValue  The value to retrieve.
	 * @return                 String of the desired value to retrieve.
	 */
	public String get(String component, String attribute, String... attributeValueArray){
		//Analyze attributeValueArray to determine how to process it
		boolean indexValueMode = false; 
		char digitCheck = 'a';
		if (attributeValueArray.length >= 1 && attributeValueArray[0].contains("_")){
			digitCheck = attributeValueArray[0].charAt(attributeValueArray[0].indexOf("_")-1);
			if(Character.isDigit(digitCheck)){
				indexValueMode = true;
			}
		}

		
		//Verify that the testData key & attribute type is valid
		if(!areTestDataKeysAndAttributesValid(component,attribute)) return null;
		
		
		//Check if given attributeValue has a number (ex: account1_467), which would dictate where to return the value in the testdata list.
		if(indexValueMode){
			int listPosition = Character.getNumericValue(digitCheck);
			//Check if the index digit from attributeValue is greater than the size of testdata.
			if(listPosition > testData.get(component).size()){
				LogMF.debug(logger, "Cannot get " + attributeValueArray[0] + " with index of " + (listPosition-1) + 
						" since testData's size is " + testData.get(component).size(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return null;
			}

			LogMF.debug(logger, "Getting: testDataKey = " + component + ". Key = " + attribute + ", Value = " + testData.get(component).get(listPosition-1).get(attribute) + 
					". Getting map in List position " + (listPosition), Thread.currentThread().getStackTrace()[1].getMethodName());
			return testData.get(component).get(listPosition-1).get(attribute);
		}
		//When no index digits are given, the value will be returned from the first map in the list.
		else{
			//If testData is empty, then return null since no testData has been created
			if(testData.get(component).size() == 0){
				LogMF.debug(logger, "testData is empty, cannot get attribute value", Thread.currentThread().getStackTrace()[1].getMethodName());
				return null;
			}

			LogMF.debug(logger, "Getting: testDataKey = " + component + ". Key = " + attribute + ", Value = " + testData.get(component).get(0).get(attribute) + 
					". Getting map in List position 0.", Thread.currentThread().getStackTrace()[1].getMethodName());
			return testData.get(component).get(0).get(attribute);
		}
	}
	

	
	/**
	 * Returns all values stored that coincide with the given component and attribute values.
	 * 
	 * @param  component  Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute  Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @return            An ArrayList of every value that coincides with the passed in component & attribute.
	 */
	public ArrayList<String> getAll(String component, String attribute){
		//Verify that the testData key & attribute type is valid
		if(!areTestDataKeysAndAttributesValid(component,attribute)) return null;
		
		//Loop through the list of component maps, and return the value of each attribute key.
		ArrayList<String> attributValues = new ArrayList<String>();
		for(int i = 0; i < testData.get(component).size(); i++){
			attributValues.add(testData.get(component).get(i).get(attribute));
			LogMF.debug(logger, "Added " + testData.get(component).get(i).get(attribute) + " to attributValues.", Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		return attributValues;
	}
	
	/**
	 * Verifies that the given component & attribute are contained within the predetermined data structures of testDataKeys & attributeTypes.
	 * 
	 * @param  component  Refers to the application component the data is from. Must match the components set from testDataKeys.
	 * @param  attribute  Refers to the data's attributes, or the data's metadata. Must match the attributes set from attributeTypes.
	 * @return            Boolean
	 */
	private boolean areTestDataKeysAndAttributesValid(String component, String attribute){
		if(!testData.containsKey(component)){
			LogMF.debug(logger, component + "is not a valid key! Please add it if needed.", Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		if(!Arrays.asList(attributeTypes).contains(attribute)){
			LogMF.debug(logger, attribute + "is not a valid attribute! Please add it if needed.", Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		return true;
	}
	

}
