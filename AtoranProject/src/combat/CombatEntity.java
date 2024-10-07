package combat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.Window;
import utilities.AnimationPlayerModule;

public class CombatEntity {
	public Move[] moveSet = {};
	public double damageResistence = 0.0;
	public int health = 0;
	public boolean dead = false;
	public double damageMultiplier = 1.0;
	private Point fieldPosition; // JLabel position on the field
	private String name;
	public JLabel sprite;
	public JLabel healthBar;
	public int maxHealth;
	public int facingLeft; // Direction entity is facing (== 1)
	public boolean flipIfFacingLeft; // Flips the JLabel image if the character is facing left
	private File imageFile; // Sprite image
	public boolean flipImages; // Boolean to flip animation graphic images
	
	public CombatEntity(String name, int health, Move[] moveSet, JLabel sprite, boolean flipImages) {
		this.health = health;
		this.moveSet = moveSet;
		this.name = name; 
		this.maxHealth = health;
		this.flipImages = flipImages;
		
		// Creates default sprite if it does not have one
		if (sprite == null) {
			JLabel defaultSprite = new JLabel(this.getName());
			defaultSprite.setPreferredSize(new Dimension(70, 70));
			defaultSprite.setSize(new Dimension(70, 70));
			defaultSprite.setBackground(new Color(20, 0, 255));
			defaultSprite.setOpaque(true);
			
			this.sprite = defaultSprite;
		} else {
			this.sprite = sprite;
		}
	}
	
	
	public void setImageFile(File image) {
		this.imageFile = image;
	}
	
	// Loads all the animations for each move in the moveset
	public void loadAnimations() {
		for (Move move : this.moveSet) {
			move.preloadAnimations();
		}
	}
	
	
	public String getName() {
		return this.name;
	}
	
	// Resets the character state, this will usually be overriden
	public void reset() {
		this.health = this.maxHealth;
		this.damageMultiplier = 1.0;
		this.damageResistence = 0.0;
		this.dead = false;
	}
	
	// Loads the sprite image
	public void loadSpriteIcon() {
		Image image = null;
		try {
			image = ImageIO.read(this.imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		image = Window.scaleImage(this.sprite.getWidth(), this.sprite.getHeight(), image);
		
		// Flips to appropriate direction
		if (facingLeft == 1 && flipIfFacingLeft == true) {
			image = AnimationPlayerModule.createMirror(image);
		} else if (facingLeft == -1 && flipIfFacingLeft == false) {
			image = AnimationPlayerModule.createMirror(image);
		}
		
		ImageIcon targetIcon = new ImageIcon(image);
		this.sprite.setIcon(targetIcon);
	}

	
	public void setMoveSet(Move[] moveSet) {
		this.moveSet = moveSet;
	}
	
	// Heals the character
	public void heal(int amount) {
		if (this.health + amount > this.maxHealth) {
			this.health = this.maxHealth;
		} else {
			this.health += amount;
		}
		
		this.updateHealthBar();
	}
	
	// Uses the move selected
	public void performTurn(Move move, CombatEntity target) {
		move.useMove(target);
	}
	
	// Updates the healthbar JLabel
	public void updateHealthBar() {
		double healthBarPercent = ((double)this.health/(double)this.maxHealth);
		int healthBarLength = Window.scaleInt((int)(healthBarPercent * 200.0));
		
		this.healthBar.setText("" + this.health + "/" + this.maxHealth);
		this.healthBar.setSize(new Dimension(healthBarLength, Window.scaleInt(15)));
	}
	
	// Damages the entity
	public void recieveDamage(int damage) {
		int totalDamage = (int)((double)(damage - (double)damage * this.damageResistence)); // Damage - resistence
		
		this.health = this.health - totalDamage;
		
		// Death
		if (this.health <= 0 ) {
			this.death();
		}
		
		// You can only have a shield for one hit, this removes it after being hit
		if (this.damageResistence > 0.0) {
			this.damageResistence = 0.0;
		}
	}
	
	// Kills the entity
	public void death() {
		this.dead = true;
		
		// Removes sprite
		Runnable removeSprite = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(2000);
				this.sprite.setVisible(false);
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		
		Thread removeSpriteThread = new Thread(removeSprite);
		removeSpriteThread.start();
	}
	
	
	public void setFieldPosition(Point position) {
		fieldPosition = position;
	}
	
	public Point getFieldPosition() {
		return fieldPosition;
	}
	
	// Chooses the move randomly if it is automatic
	protected Move chooseMove() {
		Random randomGenerator = new Random();
		
		int randomMove;
		
		if (this.moveSet.length == 1) {
			randomMove = 0;
		} else {
			randomMove = randomGenerator.nextInt(0, this.moveSet.length);
			System.out.println(randomMove);
		}
		
		Move move = moveSet[randomMove];
		
		return move;
	}
	
	// Used for enemies, this will automatically choose a move and perform it for autonomous entities
	public void automatedTurn(CombatEntity[] team, CombatEntity[] enemyTeam) {
		Move move = chooseMove(); // Random move (Unless this method is overriden)
		
		CombatEntity target;
		
		ArrayList<CombatEntity> potentialTargets = new ArrayList<CombatEntity>(); // A list of targets that the entity can perform the move on
		
		
		if (move.checkIfTargetIsValid("enemies") == true) { // This move can be used on enemies if true
			for (int i = 0; i < enemyTeam.length; i++) {
				CombatEntity entity = enemyTeam[i];
				
				if (entity.dead == false) {
					potentialTargets.add(entity);
				}
			}
		} else if (move.checkIfTargetIsValid("team") == true) { // This move can be used on self and teammates if true
			for (int i = 0; i < team.length; i++) {
				CombatEntity entity = team[i];
				
				if (entity.dead == false) {
					potentialTargets.add(entity);
				}
			}
		}
		
		// Randomly chooses a target that meets the qualifications
		Random randomGenerator = new Random();
		int arrayListSize = potentialTargets.size();
		if (arrayListSize == 0) {
			Combat.currentCombatInstance.turn();
			return;
		} else if (arrayListSize == 1){
			target = potentialTargets.get(0);
		} else {
			int randomTarget = randomGenerator.nextInt(0, arrayListSize);
			target = potentialTargets.get(randomTarget);
		}
		
		performTurn(move, target); // Performs the turn
		// Wait before next entity's turn
		Runnable fpsMethod = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(2000);
				Combat.currentCombatInstance.turn();
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		
		Thread fpsThread = new Thread(fpsMethod);
		fpsThread.start();
	}
	
	// Decides how to perform the turn
	public void myTurn(boolean automatic, CombatEntity[] team, CombatEntity[] enemyTeam) {
		if (automatic == true) { // Performs automatically
			automatedTurn(team, enemyTeam);
			
		} else { // Player chooses move
			CombatInterface.getTurn(moveSet);
		}
	}
}
