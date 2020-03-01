package jsonMerger;

import org.json.*;
import java.util.*;
import java.io.*;


public class jsonMerger {
	
	//Looking for the comment nodes from each file, 
	//they are the val of reponse key for each json object within the file
	public static void /*JSONArray*//*ArrayList<JSONObject>*/ jsonParser(ArrayList<JSONObject> jsonArr, String fileName) {
		//return json arr
		//JSONArray res = new JSONArray();
		//ArrayList<JSONObject> res = new ArrayList<>();
		
		try {
			File txtFile = new File(fileName);	//to get full path. see other way to get full path instead 
			
			BufferedReader br = new BufferedReader(new FileReader(txtFile));
			
			String line;
			
			while ((line = br.readLine()) != null) {
				//System.out.println("DEBUG reader: " + line);
				
				JSONObject jObj = new JSONObject(line);	//turn the contents to json objects
				
				//look for the response array for comment nodes
				JSONArray comments = jObj.getJSONArray("response");
				for (int i = 0; i < comments.length(); i++) {
					//res.put(comments.getJSONObject(i));
					jsonArr.add(comments.getJSONObject(i));
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
		//System.out.println("DEBUG json arr: " + jsonArr.toString());
//		return res;
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
		else {
			return false;
		}
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
	public static ArrayList<JSONObject> merger(String fileID, HashMap<String, ArrayList<String>> fMap, String sourcePath) throws FileNotFoundException, UnsupportedEncodingException {
		//grab the array list with the corresponding ID
		ArrayList<String> sameIDFileLs = fMap.get(fileID);
		System.out.println("SameFileLs: " + sameIDFileLs);
		//can't use JSONArray
		//JSONArray jsonObjArr = new JSONArray();
		ArrayList<JSONObject> jsonObjArr = new ArrayList<>();
		
		//going through the list
		for (int i = 0; i < sameIDFileLs.size(); i++) {
			//use dirpath to get full adrr for json
			String fullFilePath = sourcePath + sameIDFileLs.get(i);
			//System.out.println("DEBUG fullfp: " + fullFilePath);
			if (jsonObjArr.isEmpty()) {	//for first entry of jsonObjArr
				//jsonObjArr = jsonParser(fullFilePath);
				jsonParser(jsonObjArr, fullFilePath);
				//System.out.println("jsonObjArr: " + jsonObjArr.toString());
			}
			else {	//for the following entries
				//JSONArray newUpdateObjArr = new JSONArray();
				ArrayList<JSONObject> newUpdateObjArr = new ArrayList<>();
				//jsonObjArr = jsonParser(fullFilePath);
				jsonParser(newUpdateObjArr, fullFilePath);
				//check each object for duplicates
				for (JSONObject updatejsonObj : newUpdateObjArr) {
					boolean contains = false;	//to check if the object was there or not
					//check each obj within the jsonArry
					for (JSONObject jsonObj : jsonObjArr) {
						if (objIDChecker(updatejsonObj, jsonObj)) {
							updateChanges(jsonObj, updatejsonObj);
							contains = true;
						}
					}
					if (!contains) {	//just add if not contained
						jsonObjArr.add(updatejsonObj);
					}
				}
			}
		}
		return jsonObjArr;
	}
	
	
	
	public static void main(String args[]) throws JSONException, IOException {
		//take in dir path and work with all of the files within
		String dirPath = "./Disqus file"; //dir path here; change to test folder name
		File dir = new File (dirPath);
		File[] fileList = dir.listFiles();
		
		//Key: ID, Value: files with key's ID
		HashMap<String, ArrayList<String>> fMap = new HashMap<>();
		
		if (fileList != null) {
			for (File files : fileList) {
				String fileName = files.getName();
				String fileID = fileName.substring(0, fileName.indexOf('_'));
				
				//System.out.println("DEBUG filename: " + fileName);
				//System.out.println("DEBUG ID: " + fileID);
				
				if (!fMap.containsKey(fileID) && !fileID.equals(".DS")) {
					//create a key slot and an arr list for value
					ArrayList<String> sameIDList = new ArrayList<String>();
					sameIDList.add(fileName);	//add the file name
					fMap.put(fileID, sameIDList);
				}
				else if (fMap.containsKey(fileID) && !fileID.equals(".DS")) {
					ArrayList<String> updateSameIDList = fMap.get(fileID);	//initialized with the old list first
					//adding in order of oldest to most recent
					for (int i = 0; i < updateSameIDList.size(); i++) {
						//if the current file is less than, then it is older
						if (fileDateChecker(fileName, updateSameIDList.get(i)) < 0) {
							updateSameIDList.add(i, fileName);
							break;
						}
						else if (i == updateSameIDList.size() - 1) {
							updateSameIDList.add(updateSameIDList.size() - 1, fileName);	//add to the end
							break;
						}
					}
					fMap.put(fileID, updateSameIDList);
				}
			}
		}
		
		//FILE MAPPING COMPLETE//
		
		//when call merger, pass in file Name, the map, and the dir path to use for json in there
//		if (fileList != null) {
//			for (File file : fileList) {
//				String fullPath = file.toString();
//				String full_fileID = file.getName().substring(0, file.getName().indexOf('_'));
//				
//				//System.out.println("DEBUG: FILEPATH " + fullPath);
//				//System.out.println("DEBUG: ID " + full_fileID);
//				ArrayList<JSONObject> jsonObjArr = merger(full_fileID, fMap, fullPath);
//				//Generate final file of same ID
//				String finalFile = "./MergedRes/" + full_fileID + ".txt";		
//				PrintWriter wr = new PrintWriter(finalFile, "UTF-8");
//				for (JSONObject obj : jsonObjArr) {
//					wr.println(obj.toString());
//				}
//				wr.close();
//			}
//		}
		//System.out.println("DEBUG keySEt: " + fMap.get("7036771832").toString());
		
		for (String ID : fMap.keySet()) {
			String sourcePath = dirPath + "/";
			//System.out.println("DEBUG fullpath: " + sourcePath);
			ArrayList<JSONObject> jsonObjArr = merger(ID, fMap, sourcePath);
			//Generate final file of same ID
			String finalFile = "./MergedRes/" + ID + ".txt";		
			PrintWriter wr = new PrintWriter(finalFile, "UTF-8");
			for (JSONObject obj : jsonObjArr) {
				wr.println(obj.toString());
			}
			wr.close();	
		}
		
	}
}
