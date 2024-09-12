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
	private File file;
	
	public BIH(String filePath) {
		this.filePath = filePath;
		this.file = new File(filePath);
		
		this.convertToMap();
	}
	
	private void convertToBIH() {
		try {
			FileWriter File = new FileWriter(this.file); // Opens file using path. true = append mode on
			// Adds task to file
			
			String toWrite = "";
			
	        for (Map.Entry<String, String> entry : data.entrySet()) {
	            String key = entry.getKey();
	            String value = entry.getValue();
	            
	            String appendant = key + ";" + value + "\n";
	            
	            toWrite += appendant;
	        }
			
			File.write(toWrite);
			
			File.close();
		} catch(Exception Err) {
			System.out.println(Err);
		}
	}
	
	public static void main(String[] args) {
		BIH test = new BIH("Resources/data.bih");
		
		System.out.println(test.getIndex("monkey"));
	}
	
	public void convertToMap() {
		try {
			BufferedReader File = new BufferedReader(new FileReader(this.filePath)); // Open txt file
			String Line = File.readLine(); // Get the first line

			while (Line != null) {
				String current = Line; // Adds task to Tasks array
				
				String key = "";
				String value = "";
				
				boolean flip = false;
				
				for (char character : current.toCharArray()) {
					if (flip == false) {
						if (character == ';') {
							flip = true;
						} else {
							key += character;
						}
					} else {
						value += character;
					}
				}
				
				this.data.put(key, value);
				
				Line = File.readLine(); // Next line
			}
			
			File.close();
			
		} catch(Exception Err) {
			System.out.println(Err);
		}
	}
	
	public String getIndex(String index) {
		return this.data.get(index);
	}
	
	public void setIndex(String index, String newValue) {
		this.data.put(index, newValue);
	}
	
	public void SaveBIH() {
		this.convertToBIH();
	}

}
