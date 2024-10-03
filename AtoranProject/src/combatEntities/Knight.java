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

public class Knight extends CombatEntity {
	
	private static JLabel getSprite() {
		JLabel atoranSprite = new JLabel();
		atoranSprite.setPreferredSize(new Dimension(225, 225));
		atoranSprite.setSize(new Dimension(225, 225));
		atoranSprite.setBackground(new Color(20, 0, 255));
		atoranSprite.setOpaque(false);
		
		return atoranSprite;
	}
	
	public Knight(boolean flip) {
		super("Knight", 250, null, getSprite(), flip);
		
		File targetFile = new File("Resources/Images/Knight.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = true;
		
		Move[] moveSet = {new KnightSlashMove(this)};
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

	
	public static class KnightSlashMove extends Move {
		
		public KnightSlashMove(CombatEntity parent) {
			super("Slash", new boolean[]{true, false, false}, parent);
			
			this.setDamage(60);
			this.setDescription("Target enemies with a sweeping attack");
		}
		
		
		@Override
		protected void preloadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/NewSlashingAnimation", new Dimension((int)(550 * 1.2), (int)(400 * 1.2)), this.getParent().flipImages)};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {
			
			
			JLabel attackLabel = new JLabel();
			attackLabel.setSize(new Dimension((int)(550 * 1.2), (int)(400 * 1.2)));
			Window.scaleComponent(attackLabel);
			
			GraphicAnimation attackGraphic = new GraphicAnimation(attackLabel, 6, this.uniqueIndex[0], 0, 1);
			
			Runnable addAttackLabel = () -> {
				CombatInterface.layerOnePane.add(attackLabel, JLayeredPane.MODAL_LAYER);
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
			
			attackLabel.setLocation(targetDestination.x - Window.scaleInt(400), targetDestination.y - Window.scaleInt(120));
			
			MovementAnimation moveToTarget = new MovementAnimation(this.getParent().sprite, 24, "easeOutQuart", targetDestination, null);
			MovementAnimation moveBack = new MovementAnimation(this.getParent().sprite, 22, "easeOutQuart", this.getParent().sprite.getLocation(), targetDestination);

			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(moveToTarget);
			animationsList.add(attackGraphic);
			animationsList.add(moveBack);
			
			Animation finalAnimation = new CombinedAnimation(72, animationsList, new int[]{0, 30, 50});
			finalAnimation.keyframes[37] = new Keyframe(removeAttackLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
