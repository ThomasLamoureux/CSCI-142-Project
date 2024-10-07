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
import combat.CombatEntity;
import combat.CombatInterface;
import combat.Move;
import main.Window;
import utilities.AnimationPlayerModule;
import utilities.AnimationsPreloader;
import utilities.SoundPlayerModule.GameSound;

public class Bear extends CombatEntity{
	public static JLabel getSprite() {
		JLabel atoranSprite = new JLabel();
		atoranSprite.setPreferredSize(new Dimension(300, 300));
		atoranSprite.setSize(new Dimension(300, 300));
		atoranSprite.setBackground(new Color(20, 0, 255));
		atoranSprite.setOpaque(false);
		
		return atoranSprite;
	}

	// Warrior bear
	public Bear(boolean flip) {
		super("Bell Hunter Bear", 375, null, getSprite(), flip);
		
		File targetFile = new File("Resources/Images/Bear.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		Move[] moveSet = {new BearClawMove(this)};
		this.setMoveSet(moveSet);
	}	
	
	
	@Override
	public void reset() {
		this.health = this.maxHealth;
		this.damageMultiplier = 1.0;
		this.damageResistence = 0.0;
		this.dead = false;
		// Custom
		this.sprite = getSprite();
	}
	
	// Damage to a sigle enemy
	public static class BearClawMove extends Move {

		public BearClawMove(CombatEntity parent) {
			super("Bear Claw", new boolean[]{true, false, false}, parent);
		
			this.setDamage(75);
			this.setDescription("Targets a single enemy with a slashing attack");
			
			preloadAnimations();
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {
			JLabel targetSprite = target.sprite;
			
			JLabel attackLabel = new JLabel();
			attackLabel.setSize(new Dimension(500, 500));
			Window.scaleComponent(attackLabel);
			
			GameSound sound = new GameSound("Resources/Sounds/Claw.wav");
			
			attackLabel.setLocation(
					targetSprite.getX() - (attackLabel.getWidth() - targetSprite.getWidth())/2,
					targetSprite.getY() - (attackLabel.getHeight() - targetSprite.getHeight())/2
				);
			
			GraphicAnimation attackGraphic = new GraphicAnimation(attackLabel, 12, this.uniqueIndex[0], 0, 2);
			
			// Adds label and plays sound
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
			finalAnimation.keyframes[42] = new Keyframe(removeAttackLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
		
		
		@Override
		public void useMove(CombatEntity target) {
			target.recieveDamage(this.getDamage());
			
			this.runAnimation(target);
		}
		
		
		@Override
		protected void preloadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/BearClaw", new Dimension(500, 500), !this.getParent().flipImages)};
		}
	}
}
