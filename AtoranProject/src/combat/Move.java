package combat;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import animations.Animation;
import utilities.AnimationPlayerModule;

public class Move {
	public String name = "default";
	private Map<String, Boolean> validTargets = new HashMap<String, Boolean>(); //team, enemies
	private int damage;
	private int critChance = 20;
	private double critDamage = 1.4;
	private CombatEntity parent;
	private String description;

	public Move(String name, boolean team, boolean enemies, CombatEntity parent) {
		this.name = name;
		this.parent = parent;
		validTargets.put("team", team);
		validTargets.put("enemies", enemies);
	}
	
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getCritChance() {
		return this.critChance;
	}
	
	public double getCritDamage() {
		return this.critDamage;
	}
	
	public void setParent(CombatEntity entity) {
		this.parent = entity;
	}
	
	public CombatEntity getParent() {
		return this.parent;
	}
	
	public void damageTarget(CombatEntity target) {
		Random randomGenerator = new Random();
		int randumNumber = randomGenerator.nextInt(0, 100);
		if (randumNumber <= this.critChance) {
			target.recieveDamage((int)((double)this.damage * this.critDamage));
		} else {
			target.recieveDamage(this.damage);
		}
	}
	
	protected void runAnimation(CombatEntity target) {
		target.updateHealthBar();
		
		Point[] destinations = {target.sprite.getLocation(), this.getParent().sprite.getLocation()};
		int[] framesToTake = {30, 38};
		
		Animation animation = new Animation(this.getParent().sprite, destinations, framesToTake, "easeOutQuart");
		
		AnimationPlayerModule.addAnimation(animation);
	}
	
	// Overrided
	public void useMove(CombatEntity target) {
		target.recieveDamage(damage);
	
		runAnimation(target);
	}
	
	public Boolean checkIfTargetIsValid(String target) {
		return validTargets.get(target);
	}
}
