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
import combat.Move;
import main.Window;
import utilities.AnimationPlayerModule;
import utilities.AnimationsPreloader;
import combat.CombatEntity;
import combat.CombatInterface;


public class Slime extends CombatEntity {
	public static JLabel getSprite() {
		JLabel slimeSprite = new JLabel();
		slimeSprite.setPreferredSize(new Dimension(125, 125));
		slimeSprite.setSize(new Dimension(125, 125));
		slimeSprite.setBackground(new Color(20, 0, 255));
		slimeSprite.setOpaque(false);
		
		return slimeSprite;
	}
	
	
	public Slime(boolean flip) {
		super("Slime", 80, null, getSprite(), flip);
		
		File targetFile = new File("Resources/Images/BlueslimeStill.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		Move[] moveSet = {new BumpMove(this)};
		this.setMoveSet(moveSet);
	}
	
	@Override
	public void reset() {
		this.health = this.maxHealth;
		this.damageMultiplier = 1.0;
		this.damageResistence = 0.0;
		this.dead = false;
		
		this.sprite = getSprite();
	}
	
	
	public static class BumpMove extends Move {
		public BumpMove(CombatEntity parent) {
			super("Bump", new boolean[]{true, false, false}, parent);
			
			this.setDamage(30);
			this.setDescription("Bumps the opponent");
		}
		
		
		@Override
		protected void preloadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/HitEffect", new Dimension(250, 250), false)};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {			
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension(250, 250));
			Window.scaleComponent(animationLabel);
			
			Point targetDestination = new Point((int)
					(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), 
					target.sprite.getLocation().y + target.sprite.getHeight() - this.getParent().sprite.getHeight());
			
			Point labelPosition = new Point(target.sprite.getX() + (target.sprite.getWidth() - Window.scaleInt(250))/2,
					target.sprite.getY() + (target.sprite.getHeight() - Window.scaleInt(250))/2);
			
			
			animationLabel.setLocation(labelPosition);

			Animation moveToTarget = new MovementAnimation(this.getParent().sprite, 24, "easeOutQuart", targetDestination, null);
			Animation moveBack = new MovementAnimation(this.getParent().sprite, 22, "easeOutQuart", this.getParent().sprite.getLocation(), targetDestination);
			
			Animation graphics = new GraphicAnimation(animationLabel, 7, this.uniqueIndex[0], 0, 1);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabel);
			};
			moveBack.keyframes[0] = new Keyframe(removeLabel);
			
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(animationLabel, JLayeredPane.MODAL_LAYER);
			};
			graphics.keyframes[0] = new Keyframe(addLabel);
			
			Runnable shakeAnimation = () -> {
				AnimationPlayerModule.shakeAnimation(target);
				target.updateHealthBar();
			};
			graphics.keyframes[1] = new Keyframe(shakeAnimation);
			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(moveToTarget);
			animationsList.add(graphics);
			animationsList.add(moveBack);
			
			Animation finalAnimation = new CombinedAnimation(82, animationsList, new int[]{0, 40, 60});
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
