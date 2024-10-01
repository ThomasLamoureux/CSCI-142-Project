package inventory;

public class Inventory {
    private Item[] items; //item array
    private int itemCount; //item counta
    public Inventory(int size) {
        items = new Item[size]; //resets to array 0
        itemCount = 0;
    }

    public void addItem(Item item) { //item add to inventory 
        if (itemCount < items.length) { //checking inventory space
            items[itemCount] = item;
            itemCount++;
            System.out.println(item.getName() + " added to inventory.");
        } else {
            System.out.println("Inventory is full. Cannot add " + item.getName());
        }
    }

   
    public void removeItem(String itemName) { //removal
        for (int i = 0; i < itemCount; i++) {
            if (items[i] != null && items[i].getName().equals(itemName)) {
                System.out.println(items[i].getName() + " removed from inventory.");
                items[i] = null; 
                shiftItems(i);
                itemCount--;
                return;
            }
        }
        System.out.println(itemName + " not found in inventory."); //error message 
    }

  
    private void shiftItems(int index) {
        for (int i = index; i < itemCount - 1; i++) { 
            items[i] = items[i + 1];
        }
        items[itemCount - 1] = null; //slot clearing (I think)
    }
    
    
    public void displayInventory() { //inventory display (witerlly praying this works)
        if (itemCount == 0) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Inventory contains:");
            for (int i = 0; i < itemCount; i++) {
                if (items[i] != null) {
                    System.out.println(items[i]);
                }
            }
        }
    }
    
    public Item[] getInventory() {
    	return this.items;
    }
}

