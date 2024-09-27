package inventory;

public class Item {
	
		
		    private String itemName;
		    private String itemdescription; //description will display on screen
		    public Item(String name, String description) {
		        this.itemName = name;
		        this.itemdescription = description;
		    }

	
		    public String getName() {
		        return itemName;
		    }

		    public String getDescription() {
		        return itemdescription;
		    }
		   
		    public void useItem() {
		        System.out.println("Using the item: " + itemName); //overwritten 
		    }

		    @Override
		    public String toString() {
		        return "Item Name: " + itemName + ", Item Description: " + itemdescription; //overwrite to string
		    }
		}
