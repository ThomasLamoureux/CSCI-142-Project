package combat;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import animations.Animation;
import utilities.AnimationPlayerModule;

public class Move {
	public String name;
	private Map<String, Boolean> validTargets = new HashMap<String, Boolean>(); //team, enemies
	private int damage;
	private CombatEntity parent;
	private String description;
	protected int[] uniqueIndex;
	public boolean disabled = false;

	public Move(String name, boolean[] targets, CombatEntity parent) {
		this.name = name;
		this.parent = parent;
		validTargets.put("enemies", targets[0]);
		validTargets.put("team", targets[1]);
		validTargets.put("self", targets[2]);
	}
	
	// Meant to be overriden, preloads all the images for graphic animations
	protected void preloadAnimations() {
		
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
	
	public void setParent(CombatEntity entity) {
		this.parent = entity;
	}
	
	public CombatEntity getParent() {
		return this.parent;
	}
	
	// Meant to be override, runs the animation for the move
	protected void runAnimation(CombatEntity target) {
		target.updateHealthBar();
	}
	
	// Uses the move, doing damage then running the animation
	public void useMove(CombatEntity target) {
		target.recieveDamage((int)(damage * this.getParent().damageMultiplier));
	
		runAnimation(target);
	}
	
	// Checks to see if the target meets the valid target requirements
	public Boolean checkIfTargetIsValid(String target) {
		return validTargets.get(target);
	}
}
