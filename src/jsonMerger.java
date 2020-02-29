import org.json.*;
import java.util.*;
import java.io.*;


public class jsonMerger {
	public static void main(String args[]) {
		//take in dir path and work with all of the files within
		String dirPath = "/Users/VieYoun/Desktop/Disqus file";
		File dir = new File (dirPath);//dir path here
		File[] fileList = dir.listFiles();
		
		JSONObject resJSON = new JSONObject();
		
		if (fileList != null) {
			for (File files : fileList) {
				JSONObject toMergeFile = new JSONObject(files);
				
				//call function to merge here
				//resJSON = merger(resJSON, toMergeFile);
			}
		}
		
		
		
		
	}
	
	public static JSONObject merger(JSONObject json1, JSONObject json2) {
		JSONObject mergedRes = new JSONObject();
		
		
		
		
		
		return mergedRes;
	}
}
