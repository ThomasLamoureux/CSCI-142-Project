package datastore;

public class Datastore {

    // Read a specific field from the BIH data
    public static String readData(BIH bih, String index) {
        try {
            return bih.getIndex(index);  
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update a string field in the BIH data and save it
    public static void updateDataString(String bihFilePath, String index, String newValues) {
        BIH bih = new BIH(bihFilePath);
        bih.setIndex(index, newValues); 
        bih.SaveBIH();  
        // This saves to BIH
    }

    // Update an integer field in the BIH data and save it
    public static void updateDataInt(String bihFilePath, String index, int newValues) {
        BIH bih = new BIH(bihFilePath);
        bih.setIndex(index, Integer.toString(newValues));  // Convert int to string and update the value
        bih.SaveBIH();  
    }
}



