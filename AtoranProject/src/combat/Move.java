package combat;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import utilities.Animation;
import utilities.AnimationPlayerModule;

public class Move {
	public String name = "default";
	private Map<String, Boolean> validTargets = new HashMap<String, Boolean>(); //team, enemies
	private int damage;
	private CombatEntity parent;

	public Move(String name, boolean team, boolean enemies, CombatEntity parent) {
		this.name = name;
		this.parent = parent;
		validTargets.put("team", team);
		validTargets.put("enemies", enemies);
	}
	
	public void selected() {
		
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public void setParent(CombatEntity entity) {
		this.parent = entity;
	}
	
	public CombatEntity getParent() {
		return this.parent;
	}
	
	// Overrided
	public void useMove(CombatEntity target) {
		target.recieveDamage(damage);
		System.out.println(target.health);
		System.out.println(target.name);
		
		Point[] destinations = {target.sprite.getLocation(), this.getParent().sprite.getLocation()};
		int[] framesToTake = {16, 20};
		
		Animation animation = new Animation(this.getParent().sprite, destinations, framesToTake);
		
		AnimationPlayerModule.addAnimation(animation);
	}
	
	public Boolean checkIfTargetIsValid(String target) {
		return validTargets.get(target);
	}
}
