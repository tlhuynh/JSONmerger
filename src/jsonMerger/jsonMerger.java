package jsonMerger;

import org.json.*;
import java.util.*;
import java.io.*;


public class jsonMerger {
	
	
	//Looking for the comment nodes from each file, 
	//they are the val of reponse key for each json object within the file
	public static JSONArray jsonParser(String fileName) {
		JSONArray res = new JSONArray();
		try {
			File txtFile = new File(fileName);
			
			BufferedReader br = new BufferedReader(new FileReader(txtFile));
			
			String line;
			
			while ((line = br.readLine()) != null) {
				System.out.println("DEBUG reader: " + line);
				JSONObject jObj = new JSONObject(line);	//turn the contents to json objects
				//look for the response array for comment nodes
				JSONArray comments = jObj.getJSONArray("response");
				for (int i = 0; i < comments.length(); i++) {
					//res.put(comments.getJSONObject(i));
					res.put(comments.getJSONObject(i));
					
				}
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Can't read/write...");
		}
		System.out.println("DEBUG json arr: " + res.toString());
		return res;
	}
	
	
	
	
	public static JSONObject merger(JSONObject json1, JSONObject json2) {
		JSONObject mergedRes = new JSONObject();
		
		
		
		
		
		return mergedRes;
	}
	
	
	
	public static void main(String args[]) {
		//take in dir path and work with all of the files within
		String dirPath = "Disqus file"; //dir path here; change to test folder name
		File dir = new File (dirPath);
		File[] fileList = dir.listFiles();
		
		if (fileList != null) {
			for (File files : fileList) {
				String fileName = files.toString();
				System.out.println("DEBUG Files: " + fileName);
				JSONArray tempJSONArr = jsonParser(fileName);
			}
		}
		
		
		
		
	}
	

}
