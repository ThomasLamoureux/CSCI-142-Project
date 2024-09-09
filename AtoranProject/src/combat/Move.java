package combat;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import animations.Animation;
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
