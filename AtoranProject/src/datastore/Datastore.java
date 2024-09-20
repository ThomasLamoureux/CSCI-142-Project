package datastore;

public class Datastore {
	private static String dataPath = "Resources/data.bih";

    // Read a specific field from the BIH data
    public static String readData(String index) {
    	BIH bih = new BIH(dataPath);
        try {
            return bih.getIndex(index);  
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update a string field in the BIH data and save it
    public static void updateDataString(String index, String newValues) {
        BIH bih = new BIH(dataPath);
        bih.setIndex(index, newValues); 
        bih.SaveBIH();  
        // This saves to BIH
    }

    // Update an integer field in the BIH data and save it
    public static void updateDataInt(String index, int newValues) {
        BIH bih = new BIH(dataPath);
        bih.setIndex(index, Integer.toString(newValues));  // Convert int to string and update the value
        bih.SaveBIH();  
    }
    



	public static boolean isLevelUnlocked(BIH bih, String string) {
		return false;
	}

	public static void writeData(String string, Object object) {
		
	}
}



