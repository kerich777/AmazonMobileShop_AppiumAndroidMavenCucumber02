package com.Utilities.General;


import java.util.ArrayList;

public class KeywordFactory {
	
	public static <T> T getStepDefinition(String feature) {
		
		String platform = System.getProperty("platform");
		
		if(platform.contains("Align")) {
			platform = platform + System.getProperty("alignPlatform");
		}
		
		String className = feature + platform + "Keywords";

		String featureClassPackagePath = KeywordFactory.getClassPackage(feature, className);
		

		T keywordImplementation = null;
	
		try {
				
			keywordImplementation = (T) Class.forName(featureClassPackagePath).newInstance();
				
		} catch (Exception e) {
			e.printStackTrace();
		}

		return keywordImplementation;

	}
	
	private static String getClassPackage(String feature, String className) {
		ArrayList<String> features = new ArrayList<String>();
		features.add(feature);
		
		String platform = System.getProperty("platform");
		String workingDirectory = KeywordFactory.getLibraryDirectory();
		

		ArrayList<String> classPackageFullPath = Utilities.getJavaRunTimeResources(features, platform, workingDirectory);


		
		for(int i = 0; i < classPackageFullPath.size(); i++){
			
			
			String fullClassPath = classPackageFullPath.get(i);
			
			if(fullClassPath.contains(className)) {
				int packageStartIndex = fullClassPath.indexOf("com");
				return fullClassPath.substring(packageStartIndex);
			}
		}
		
		
		
		return null;
	}
	
	private static String getLibraryDirectory() {
		String path = KeywordFactory.class.getClassLoader().getResource("").getPath();
		path = path.replace("%20", " ");
		return path.split("target")[0];
	}
}