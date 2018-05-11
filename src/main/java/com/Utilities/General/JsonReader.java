package com.Utilities.General;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;


import gherkin.deps.com.google.gson.JsonArray;
import gherkin.deps.com.google.gson.JsonElement;
import gherkin.deps.com.google.gson.JsonObject;
import gherkin.deps.com.google.gson.JsonParser;

public class JsonReader {
	static String homepath = System.getProperty("user.home");
	static String basepath = homepath +"/autorobot";
	
	public static File[] ConstructFileList() {
		File path = new File(basepath);
		FileFilter jsonFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.getName().contains(".json");
			}
		};
		
		File[] directories = path.listFiles(jsonFilter);
		return directories;
	}
	
	public static void readJson(File[]input) {
		for(File i : input) {
			try {
				FileReader reader = new FileReader(i);
				JsonParser parser = new JsonParser();
				JsonArray jArray = (JsonArray) parser.parse(reader);
				for(JsonElement o : jArray) {
					JsonObject obj = (JsonObject) o;
					System.out.println(obj.toString() + "\n");
				}
			} catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

		}
	}
	
	public static void main(String[] args) {
		File[] input = ConstructFileList();
		readJson(input);
	}
	
	
}
