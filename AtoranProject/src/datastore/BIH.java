package datastore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class BIH {
	private Map<String, String> data = new HashMap<>();
	private String filePath;

	// Constructor, gets file path then converts to BIH
	public BIH(String filePath) {
		this.filePath = filePath;
		
		this.convertToMap();
	}
	
	// Converts from a Map to BIH format which is "key;value"
	private void convertToBIH() {
		try {
			FileWriter File = new FileWriter(new File(filePath)); 
			String toWrite = ""; // Will be written to the file
			
	        for (Map.Entry<String, String> entry : data.entrySet()) {
	            String key = entry.getKey();
	            String value = entry.getValue();
	            
	            String appendant = key + ";" + value + "\n"; // ; is seperator
	            
	            toWrite += appendant;
	        }
			
			File.write(toWrite); // Writes the file
			
			File.close();
		} catch(Exception Err) {
			System.out.println(Err);
		}
	}
	
	// Converts from BIH to map
	public void convertToMap() {
		try {
			BufferedReader File = new BufferedReader(new FileReader(this.filePath)); // Open txt file
			String Line = File.readLine(); // Get the first line

			// Loops through file, each line contains and key and value
			while (Line != null) {
				String current = Line; // Adds task to Tasks array
				
				String key = "";
				String value = "";
				
				boolean flip = false;
				
				for (char character : current.toCharArray()) {
					if (flip == false) {
						if (character == ';') { // Switches to value
							flip = true;
						} else { // Adds to key
							key += character;
						}
					} else { // Adds to value
						value += character;
					}
				}
				
				this.data.put(key, value); // Adds key and value to the map
				
				Line = File.readLine(); // Next line
			}
			
			File.close();
			
		} catch(Exception Err) {
			System.out.println(Err);
		}
	}
	// Gets the value out of the map
	public String getIndex(String index) {
		return this.data.get(index);
	}
	// Sets a value of the maps
	public void setIndex(String index, String newValue) {
		this.data.put(index, newValue);
	}
	// Saves
	public void SaveBIH() {
		this.convertToBIH();
	}

}
