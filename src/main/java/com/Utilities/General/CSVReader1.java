package com.Utilities.General;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.StringBuilder;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.opencsv.CSVReader;

public class CSVReader1 {

	/**
	 * Gets the test data and stores it in a map with the column name as the key and the row as the value.
	 *
	 * @param  fileName      Name of the csv file to load.
	 * @param  userLanguage  The language the test will run against.
	 * @return               HashMap of the testdata.
	 */
	public static Map<String, String> getTestData(String fileName, String userLanguage) throws IOException{
			Map<String, String> mapTest = new HashMap<String, String>();
			
			String fullPath = "";
			if(System.getProperty("os.name").contains("Windows"))
				fullPath = "C:/Users/VeevaAdmin/Desktop/" + fileName + ".csv";
			else
				fullPath = "~/Desktop/automation/iOS\\ Automation/dataSheets/" + fileName + ".csv";

			//String fullPath = "C:/Users/VeevaAdmin/Desktop/" + fileName + ".csv";
			CSVReader reader = new CSVReader(new FileReader(fullPath));

			//Get the header
			String[] header = reader.readNext();

			//Get the data
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null){
				if(Arrays.asList(nextLine).contains(userLanguage)){
					break;
				}
			}

			for(int i = 0; i < header.length; i++)
				mapTest.put(header[i], nextLine[i]);

			reader.close();
			return mapTest;
	}
	/**
	 * Returns the test tag that contains the caseID.
	 *
	 * @param  tagList  List of the test tags.
	 * @return          String of the tag that contains only digits, which is the caseID.
	 */
	public static String getFileName(List<String> tagList){
		for(int i = 0; i < tagList.size(); i++)
			if(tagList.get(i).matches("[0-9]+"))
				return tagList.get(i);
		return null;
	}

	/**
	 * Loads the system resources based on the user language.
	 *
	 * @param  userLanguage  The language the test is running against.
	 * @return               HashMap of both Resource_userLanguage @ SystemResource.
	 */
	public static Map<String, String> getSystemResource(String userLanguage) throws IOException{
		Map<String, String> mapTest = new HashMap<String, String>();
		
		try {
			//Get the values from the Resource_userLanguage file
			String separator = System.getProperty("file.separator");
			File resource = new File(ClassLoader.getSystemResource("Common Data" + separator + "Resource_" + userLanguage + ".csv").toURI());
			CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(resource), "UTF-8"));

			//Skip the header
			reader.readNext();

			//Get the data
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				mapTest.put(nextLine[0], nextLine[1]);
			}

			//Get the values from the SystemResource file
			File systemResource = new File(ClassLoader.getSystemResource("Common Data" + separator + "SystemResource.csv").toURI());
			reader = new CSVReader(new InputStreamReader(new FileInputStream(systemResource), "UTF-8"));

			String[] systemResourceHeader = reader.readNext();

			int languageColumn = Arrays.asList(systemResourceHeader).indexOf(userLanguage);

			//Get the data
			String[] systemResourceNextLine;
			while ((systemResourceNextLine = reader.readNext()) != null) {
				mapTest.put(systemResourceNextLine[0], systemResourceNextLine[languageColumn]);
			}

			reader.close();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return mapTest;
	}
	
	/**
	 * Reads in data from CSV file then appends to feature file as examples table
	 * @throws IOException 
	 * 
	 */
	public void appendExamples(String csvFile, String featureFile) throws IOException{
		String basePath = new File("").getAbsolutePath();
        String fullPath = "";
		if(System.getProperty("os.name").contains("Windows"))
			fullPath = "C:/Users/VeevaAdmin/Desktop/" + csvFile + ".csv";
		else
			fullPath = "~/Desktop/automation/Common Data/" + csvFile + ".csv";		

		CSVReader reader = new CSVReader(new FileReader(fullPath));

		//Get the header
		String[] header = reader.readNext();
		//Get the data
		String[] nextLine;
		//Build String
		String table = "";
		//Cucumber keyword for scenatio outline data
		table += "\nExamples: Test Data\n";
		
		//Loop over col headers
		for (int i = 0; i < header.length; i++) {
			table += "|"+header[i];
		}
		table += "|\n";
		
		//Add lines under cols
		while ((nextLine = reader.readNext()) != null){
		for(int i = 0; i < header.length; i++){
			table += "|"+nextLine[i];
		}
		table += "|\n";
		}
		reader.close();
		System.out.println(table);
		// Append data to file as table		
		try {
		    Files.write(Paths.get(featureFile+".feature"), table.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}

}
