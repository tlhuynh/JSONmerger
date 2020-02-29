package jsonMerger;

import org.json.*;
import java.util.*;
import java.io.*;


public class jsonMerger {
	
	//Looking for the comment nodes from each file, 
	//they are the val of reponse key for each json object within the file
	public static JSONArray jsonParser(String fileName) {
		//return json arr
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
		}
		System.out.println("DEBUG json arr: " + res.toString());
		return res;
	}
	
	public static void updateChanges (JSONObject oldObj, JSONObject newObj) {
		for (String key : oldObj.keySet()) {
			if (newObj.has(key)) {
				//update old with val from new
				oldObj.put(key, newObj.get(key));
			}
		}
	}
	
	public static boolean objIDChecker (JSONObject obj1, JSONObject obj2) {
		//get value from id of the two objects to compare
		if (obj1.getString("id").equals(obj2.get("id"))) {
			return true;
		}
		return false;
	}
	
	public static boolean fileIDChecker (String fn1, String fn2) {
		//get the ID part from the filename
		//filename is in this format: "ArticleID_datetime.txt"
		String fn1_ID = fn1.substring(0, fn1.indexOf('_'));
		String fn2_ID = fn2.substring(0, fn2.indexOf('_'));
		
		//compare
		if (fn1_ID.equals(fn2_ID)) {
			return true;
		}
		
		return false;
	}
	
	public static int fileDateChecker (String fn1, String fn2 ) {
		/* compareTo() wrapper for date part only
		 * 
		 *  file name is in this format: "ArticleID_datetime.txt"
		 *  
		 *  positive for fn1 > fn2
		 *  negative for fn1 < fn2
		 *  zero for fn1 == fn2
		 *  
		 * */
		String f1Date = fn1.substring(fn1.indexOf('_') + 1, fn1.indexOf('.'));
		String f2Date = fn2.substring(fn2.indexOf('_') + 1, fn2.indexOf('.'));
		
		return f1Date.compareTo(f2Date);
	}
	
	
	//merger the files with same ID
	//take in the ID and the map of all files
	//out put an updated file
	public static void merger(String fileID, HashMap<String, ArrayList<String>> fMap) {
		
		

	}
	
	
	
	public static void main(String args[]) throws JSONException, IOException {
		//take in dir path and work with all of the files within
		String dirPath = "Disqus file"; //dir path here; change to test folder name
		File dir = new File (dirPath);
		File[] fileList = dir.listFiles();
		
		//Key: ID, Value: files with key's ID
		HashMap<String, ArrayList<String>> fMap = new HashMap<>();
		
		if (fileList != null) {
			for (File files : fileList) {
				String fileName = files.getName();
				System.out.println("DEBUG: filename " + fileName);
			}
		}
		
		
		
		
	}
	

}
