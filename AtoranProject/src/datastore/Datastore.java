
package datastore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Datastore {
    // Read JSON from a file
    public static String getJSONFromFile(String filename) {
        StringBuilder jsonText = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText.toString();
    }

    /*/ Read JSON from a URL
    public static String getJSONFromURL(String strUrl) {
        StringBuilder jsonText = new StringBuilder();
        try {
            URL url = new URL(strUrl);
            InputStream is = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText.append(line).append("\n");
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText.toString();
    }*/
    
    private static void saveData() {
    	
    }
    
    
    private static String readData(String index) {
    	
    	return null;
    }
    
    
    public static void updateDataString(String index, String newValues) {
    	
    }
    
    
    public static void updateDataInt(String index, int newValues) {
    	
    }
    
    

    public static void main(String[] args) {
        // Reading JSON from the file
        String strJson = getJSONFromFile("/Users/lukealley/Documents/Sigma-Studios/AtoranProject/src/datastore/data.json");
        System.out.println(strJson);
    }
}  

