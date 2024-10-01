package inventory;

import combat.Combat;
import combat.CombatEntity;

public class Item {
    private String itemName;
    private String itemdescription; //description will display on screen
    protected int uses;
    public String imagePath;
    public Item(String name, String description, int uses) {
        this.itemName = name;
        this.itemdescription = description;
        this.uses = uses;
    }


    public String getName() {
        return itemName;
    }

    public String getDescription() {
        return itemdescription;
    }
   
    public boolean useItem() {
        System.out.println("Using the item: " + itemName); //overwritten
        return true;
    }

    public String getDetails() {
        return "Item Name: " + itemName + ", Item Description: " + itemdescription; //overwrite to string
    }
    
    public int getUses() {
    	return uses;
    }
    
    
    public static class HealthPotion extends Item {

		public HealthPotion(String name, String description, int uses) {
			super(name, description, uses);
			this.imagePath = "Resources/Images/HealthPotion.png";
		}
		
		@Override
		public boolean useItem() {
			if (Combat.currentCombatInstance.teamTurn == 0 && this.uses > 0) {
				for (CombatEntity entity : Combat.currentCombatInstance.currentTeam.members) {
					if (entity.dead == false) {
						entity.heal(50);
					}
				}
				this.uses -= 1;
				return true;
			} else {
				return false;
			}
		}
    }
    
    
    public static class DefensePotion extends Item {

		public DefensePotion(String name, String description, int uses) {
			super(name, description, uses);
			this.imagePath = "Resources/Images/DefensePotion.png";
		}
		
		@Override
		public boolean useItem() {
			if (Combat.currentCombatInstance.teamTurn == 0 && this.uses > 0) {
				for (CombatEntity entity : Combat.currentCombatInstance.currentTeam.members) {
					if (entity.dead == false) {
						entity.damageResistence += 0.3;
					}
				}
				this.uses -= 1;
				return true;
			} else {
				return false;
			}
		}
    }
    
    
    public static class DamagePotion extends Item {

		public DamagePotion(String name, String description, int uses) {
			super(name, description, uses);
		}
		
		@Override
		public boolean useItem() {
			if (Combat.currentCombatInstance.teamTurn == 0 && this.uses > 0) {
				for (CombatEntity entity : Combat.currentCombatInstance.currentTeam.members) {
					if (entity.dead == false) {
						entity.damageMultiplier += 0.3;
					}
				}
				this.uses -= 1;
				return true;
			} else {
				return false;
			}
		}
    }
}
