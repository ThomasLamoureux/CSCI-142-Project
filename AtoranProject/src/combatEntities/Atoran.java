package combatEntities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import animations.Animation;
import animations.Keyframe;
import animations.Animation.CombinedAnimation;
import animations.Animation.GraphicAnimation;
import animations.Animation.MovementAnimation;
import combat.Combat;
import combat.CombatEntity;
import combat.CombatInterface;
import combat.Move;
import main.Window;
import utilities.AnimationPlayerModule;
import utilities.AnimationsPreloader;
import utilities.SoundPlayerModule.GameSound;

public class Atoran extends CombatEntity {
	private boolean empowered; // Special attribute for atoran
	
	// Creates the sprite for the etntity
	public static JLabel getAtoranSprite() {
		JLabel atoranSprite = new JLabel();
		atoranSprite.setPreferredSize(new Dimension(225, 225));
		atoranSprite.setSize(new Dimension(225, 225));
		atoranSprite.setBackground(new Color(20, 0, 255));
		atoranSprite.setOpaque(false);
		
		return atoranSprite;
	}

	// Atoran entity, the main character
	public Atoran(boolean flip) {
		super("Atoran", 425, null, getAtoranSprite(), flip);
		
		File targetFile = new File("Resources/Images/AtoranStand.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = true;
		
		Move[] moveSet = {new SlashMove(this), new SweepMove(this), new EmpowerMove(this)};
		this.setMoveSet(moveSet);
	}	
	
	// Empowers atoran
	protected void setEmpowered(boolean flip) {
		if (flip == true) {
			this.empowered = true;
			this.damageMultiplier += 2.0; // 3x damage mulitiplier
		} else {
			this.empowered = false;
			this.damageMultiplier -= 2.0;
		}
	}
	
	
	@Override
	public void reset() {
		this.health = this.maxHealth;
		this.damageMultiplier = 1.0;
		this.damageResistence = 0.0;
		this.dead = false;
		// Custom resetes
		this.sprite = getAtoranSprite();
		this.empowered = false;
		this.moveSet[2].disabled = false; // Sets EmpowerMove to enabled
	}
	
	// Empowers Atoran increasing the damage of his next attack
	public static class EmpowerMove extends Move{
		
		public EmpowerMove(CombatEntity parent) {
			super("Empower", new boolean[]{false, false, true}, parent);
			
			this.setDamage(0);
			this.setDescription("Empower yourself, increasing the damage of your next attack by 200%");
		}
		
		@Override
		protected void preloadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/EmpowerAnimation", new Dimension(225, 225), false)};
		}
		
		
		@Override
		public void useMove(CombatEntity target) {
			Atoran atoran = (Atoran) this.getParent();
			atoran.setEmpowered(true);
			this.disabled = true; // Cant be used until Atoran performs another move after use
			
			this.runAnimation(target);
		}
		
		@Override
		protected void runAnimation(CombatEntity target) {
			// Labal used for animation graphics
			JLabel label = new JLabel();
			label.setSize(new Dimension(225, 225));
			Window.scaleComponent(label);
			label.setLocation(this.getParent().sprite.getLocation());
			
			GameSound sound = new GameSound("Resources/Sounds/Empower.wav");
			
			GraphicAnimation graphic = new GraphicAnimation(label, 8, this.uniqueIndex[0], 0, 2);
			
			// Method to add label and play sound
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(label, JLayeredPane.MODAL_LAYER);
				sound.play();
			};
			graphic.keyframes[0] = new Keyframe(addLabel); // Sets it to be ran on the first keyframe
			
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(label);
			};
		
			// Animation
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(graphic);
			
			Animation finalAnimation = new CombinedAnimation(29, animationsList, new int[]{20});
			finalAnimation.keyframes[28] = new Keyframe(removeLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}

	// Does direct damage to a single enemy
	public static class SlashMove extends Move {

		public SlashMove(CombatEntity parent) {
			super("Slash", new boolean[]{true, false, false}, parent);
		
			this.setDamage(80);
			this.setDescription("Targets a single enemy with a slashing attack");
		}
		
		
		private void slashAnimation(CombatEntity target) {
			// Label used for nimation graphics
			JLabel attackLabel = new JLabel();
			attackLabel.setSize(new Dimension((int)(550 * 1.2), (int)(400 * 1.2)));
			Window.scaleComponent(attackLabel);
			
			GameSound sound = new GameSound("Resources/Sounds/SwordSwing1.wav");
			
			GraphicAnimation attackGraphic = new GraphicAnimation(attackLabel, 6, this.uniqueIndex[0], 0, 1);
			
			Runnable addAttackLabel = () -> {
				CombatInterface.layerOnePane.add(attackLabel, JLayeredPane.MODAL_LAYER);
				sound.play();
			};
			attackGraphic.keyframes[0] = new Keyframe(addAttackLabel); // Set to play during the first keyframe
			
			
			Runnable removeAttackLabel = () -> {
				CombatInterface.layerOnePane.remove(attackLabel);
			};
			
			// Causes target to shake after being hit
			Runnable shakeAnimation = () -> {
				AnimationPlayerModule.shakeAnimation(target);
				target.updateHealthBar();
			};
			attackGraphic.keyframes[1] = new Keyframe(shakeAnimation);
			
			// The destination Atoran will head towards during the movement animation (Infront of the target)
			Point targetDestination = new Point((int)
					(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), 
					target.sprite.getLocation().y + target.sprite.getHeight() - this.getParent().sprite.getHeight());

			
			attackLabel.setLocation(targetDestination.x - Window.scaleInt(40), targetDestination.y - Window.scaleInt(120));
			
			MovementAnimation moveToTarget = new MovementAnimation(this.getParent().sprite, 24, "easeOutQuart", targetDestination, null); // Moves to target
			MovementAnimation moveBack = new MovementAnimation(this.getParent().sprite, 22, "easeOutQuart", this.getParent().sprite.getLocation(), targetDestination); // Moves back

			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(moveToTarget);
			animationsList.add(attackGraphic);
			animationsList.add(moveBack);
			
			Animation finalAnimation = new CombinedAnimation(72, animationsList, new int[]{0, 30, 50});
			finalAnimation.keyframes[37] = new Keyframe(removeAttackLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
		
		// This animation is played instead if Atoran is empowered
		private void empoweredSlashAnimation(CombatEntity target) {
			GameSound sound = new GameSound("Resources/Sounds/SwordSwing1.wav");

			
			JLabel targetSprite = target.sprite;
			
			JLabel attackLabel = new JLabel();
			attackLabel.setSize(new Dimension((int)(550 * 1.2), (int)(400 * 1.2)));
			Window.scaleComponent(attackLabel);
			
			attackLabel.setLocation(
					targetSprite.getX() - (attackLabel.getWidth() - targetSprite.getWidth())/2,
					targetSprite.getY() - (attackLabel.getHeight() - targetSprite.getHeight())/2
				);
			
			GraphicAnimation attackGraphic = new GraphicAnimation(attackLabel, 20, this.uniqueIndex[1], 0, 1);
			
			Runnable addAttackLabel = () -> {
				CombatInterface.layerOnePane.add(attackLabel, JLayeredPane.MODAL_LAYER);
				sound.play();
			};
			attackGraphic.keyframes[0] = new Keyframe(addAttackLabel);
			
			
			Runnable removeAttackLabel = () -> {
				CombatInterface.layerOnePane.remove(attackLabel);
			};
			

			Runnable shakeAnimation = () -> {
				AnimationPlayerModule.shakeAnimation(target);
				target.updateHealthBar();
			};
			attackGraphic.keyframes[1] = new Keyframe(shakeAnimation);
			
			
			
			Point targetDestination = new Point((int)
					(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), 
					target.sprite.getLocation().y + target.sprite.getHeight() - this.getParent().sprite.getHeight());
		
			
			MovementAnimation moveToTarget = new MovementAnimation(this.getParent().sprite, 24, "easeOutQuart", targetDestination, null);
			MovementAnimation moveBack = new MovementAnimation(this.getParent().sprite, 22, "easeOutQuart", this.getParent().sprite.getLocation(), targetDestination);

			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(moveToTarget);
			animationsList.add(attackGraphic);
			animationsList.add(moveBack);
			
			Animation finalAnimation = new CombinedAnimation(87, animationsList, new int[]{0, 30, 65});
			finalAnimation.keyframes[50] = new Keyframe(removeAttackLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
		
		// If atoran is empowered
		private void empoweredSlash(CombatEntity target) {
			target.recieveDamage((int)(this.getDamage() * this.getParent().damageMultiplier));
			this.getParent().moveSet[2].disabled = false; // Enables the empower move
			empoweredSlashAnimation(target);
		}
		
		// If atoran is not empowered
		private void slash(CombatEntity target) {
			target.recieveDamage(this.getDamage());
			
			slashAnimation(target);
		}
		
		
		@Override
		public void useMove(CombatEntity target) {
			Atoran atoran = (Atoran) this.getParent();
			
			if (atoran.empowered == true) {
				empoweredSlash(target);
				atoran.setEmpowered(false); // Unempowers Atoran
			} else {
				slash(target);
			}
		}
		
		
		@Override
		protected void preloadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/NewSlashingAnimation", new Dimension((int)(550 * 1.2), (int)(400 * 1.2)), this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/EmpoweredSlash", new Dimension((int)(550 * 1.2), (int)(400 * 1.2)), this.getParent().flipImages)};
		}
	}

	// Damage to all enemies on the field
	public static class SweepMove extends Move {

		public SweepMove(CombatEntity parent) {
			super("Sweep", new boolean[]{true, false, false}, parent);
		
			this.setDamage(50);
			this.setDescription("Targets all enemies on the field with a sweeping attack");
			
			this.preloadAnimations();
		}
		
		// If Atoran is empowered
		private void empoweredSweep() {
			CombatEntity[] enemies = Combat.currentCombatInstance.notCurrentTeam.members;
			this.getParent().moveSet[2].disabled = false; // Enables empower move
			for (int i = 0; i < enemies.length; i++) { // Damage to all enemies
				enemies[i].recieveDamage((int)(this.getDamage() * this.getParent().damageMultiplier));
			}
			
			empoweredSweepAnimation();
		}
		
		// If Atoran is not empowered
		private void sweep() {
			CombatEntity[] enemies = Combat.currentCombatInstance.notCurrentTeam.members;
			
			for (int i = 0; i < enemies.length; i++) {
				enemies[i].recieveDamage((int)(this.getDamage() * this.getParent().damageMultiplier));
			}
			
			sweepAnimation();
		}
		
		// Animation for empowered sweep
		private void empoweredSweepAnimation() {
			GameSound sound = new GameSound("Resources/Sounds/SwordSwing1.wav");
			
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension((int)(550 * 1.8), (int)(400 * 1.8)));
			Window.scaleComponent(animationLabel);
			
			Point targetDestination = new Point(Window.scaleInt(960) + Window.scaleInt(100) * this.getParent().facingLeft, Window.scaleInt(675));
			
			animationLabel.setLocation(new Point(targetDestination.x - Window.scaleInt(85), targetDestination.y - this.getParent().sprite.getHeight()/2 - Window.scaleInt(150)));

			Animation moveToTarget = new MovementAnimation(this.getParent().sprite, 24, "easeOutQuart", targetDestination, null);
			Animation moveBack = new MovementAnimation(this.getParent().sprite, 22, "easeOutQuart", this.getParent().sprite.getLocation(), targetDestination);
			
			Animation graphics = new GraphicAnimation(animationLabel, 18, this.uniqueIndex[1], 0, 3);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabel);
			};
			moveBack.keyframes[0] = new Keyframe(removeLabel);
			
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(animationLabel, JLayeredPane.MODAL_LAYER);
				sound.play();
			};
			graphics.keyframes[0] = new Keyframe(addLabel);
			
			Runnable shakeAnimation = () -> {
				CombatEntity[] enemies = Combat.currentCombatInstance.notCurrentTeam.members;
				
				for (int i = 0; i < enemies.length; i++) {
					AnimationPlayerModule.shakeAnimation(enemies[i]);
					enemies[i].updateHealthBar();
				}
			};
			graphics.keyframes[1] = new Keyframe(shakeAnimation);
			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(moveToTarget);
			animationsList.add(graphics);
			animationsList.add(moveBack);
			
			Animation finalAnimation = new CombinedAnimation(92, animationsList, new int[]{0, 30, 70});
			finalAnimation.keyframes[47] = new Keyframe(removeLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
		
		// Animation for uneqmpowered sweep
		private void sweepAnimation() {
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension((int)(550 * 1.2), (int)(400 * 1.2)));
			Window.scaleComponent(animationLabel);
			
			GameSound sound = new GameSound("Resources/Sounds/SwordSwing1.wav");
			
			Point targetDestination = new Point(Window.scaleInt(960) + Window.scaleInt(100) * this.getParent().facingLeft, Window.scaleInt(675));
			
			animationLabel.setLocation(new Point(targetDestination.x - Window.scaleInt(85), targetDestination.y - this.getParent().sprite.getHeight()/2));

			Animation moveToTarget = new MovementAnimation(this.getParent().sprite, 24, "easeOutQuart", targetDestination, null);
			Animation moveBack = new MovementAnimation(this.getParent().sprite, 22, "easeOutQuart", this.getParent().sprite.getLocation(), targetDestination);
			
			Animation graphics = new GraphicAnimation(animationLabel, 6, this.uniqueIndex[0], 0, 1);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabel);
			};
			moveBack.keyframes[0] = new Keyframe(removeLabel);
			
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(animationLabel, JLayeredPane.MODAL_LAYER);
				sound.play();
			};
			graphics.keyframes[0] = new Keyframe(addLabel);
			
			Runnable shakeAnimation = () -> {
				CombatEntity[] enemies = Combat.currentCombatInstance.notCurrentTeam.members;
				
				for (int i = 0; i < enemies.length; i++) {
					AnimationPlayerModule.shakeAnimation(enemies[i]);
					enemies[i].updateHealthBar();
				}
			};
			graphics.keyframes[1] = new Keyframe(shakeAnimation);
			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(moveToTarget);
			animationsList.add(graphics);
			animationsList.add(moveBack);
			
			Animation finalAnimation = new CombinedAnimation(72, animationsList, new int[]{0, 30, 50});
			finalAnimation.keyframes[36] = new Keyframe(removeLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
		
		
		@Override
		protected void preloadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/SweepAnimation", new Dimension((int)(550 * 1.2), (int)(400 * 1.2)), this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/EmpoweredSweep", new Dimension((int)(550 * 1.8), (int)(400 * 1.8)), this.getParent().flipImages)};
		}
		
		
		@Override
		public void useMove(CombatEntity target) {
			Atoran atoran = (Atoran) this.getParent();
			
			if (atoran.empowered == true) {
				empoweredSweep();
				atoran.setEmpowered(false);
			} else {
				sweep();
			}
		}
	}
}
