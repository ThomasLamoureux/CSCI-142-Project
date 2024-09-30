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
import combat.Combat;
import combat.CombatEntity;
import combat.CombatInterface;
import combat.Move;
import combatEntities.DralyaDragonForm.DragonSlash;
import combatEntities.DralyaDragonForm.PowerfulDragonSlash;
import main.Window;
import utilities.AnimationPlayerModule;
import utilities.AnimationsPreloader;

public class DralyaHumanForm extends CombatEntity {
	public static JLabel getSprite() {
		JLabel atoranSprite = new JLabel("Atoran");
		atoranSprite.setPreferredSize(new Dimension(225, 225));
		atoranSprite.setSize(new Dimension(225, 225));
		atoranSprite.setBackground(new Color(20, 0, 255));
		atoranSprite.setOpaque(false);
		
		return atoranSprite;
	}

	// Atoran entity, the main character
	public DralyaHumanForm(boolean flip) {
		super("Dralya", 250, null, getSprite(), flip);
		
		File targetFile = new File("Resources/Images/DralyaHumanForm.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = true;
		
		Move[] moveSet = {new DragonSlash(this), new PowerfulDragonSlash(this)};
		this.setMoveSet(moveSet);
	}	
	
	public static class Sacrifice extends Move {

		public Sacrifice(CombatEntity parent) {
			super("Sacrifice", false, true, parent);
		
			this.setDamage(100);
			this.setDescription("Reduces the damage taken to allies based on Dralya's current HP % in turn for reducing her health to 0");
			
			preLoadAnimations();
		}
		
		@Override
		protected void preLoadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/DragonSlash", new Dimension(500, 500), !this.getParent().flipImages)};
		}
		
		
		@Override
		public void useMove(CombatEntity target) {
			CombatEntity parent = this.getParent();
			
			double healthPercentange = (double)parent.health / (double)parent.maxHealth;
			parent.recieveDamage(20000);
			
			for (CombatEntity entity : Combat.currentCombatInstance.currentTeam.members) {
				entity.damageResistence = healthPercentange;
			}
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {
			boolean flipImage = false;
			if (this.getParent().facingLeft == 1) {
				flipImage = true;
			}
			
			JLabel sprite = this.getParent().sprite;
			
			JLabel animationLabelOne = new JLabel();
			animationLabelOne.setSize(new Dimension((int)(500), (int)(500)));
			Window.scaleComponent(animationLabelOne);
			
			int offset = (animationLabelOne.getWidth() - target.sprite.getWidth())/2;
			Point animationLocation = new Point(
					target.sprite.getX() - offset,
					target.sprite.getY() - offset
					);

			
			animationLabelOne.setLocation(animationLocation);
			
			
			Point targetDestination = new Point((int)
					(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), 
					target.sprite.getLocation().y + target.sprite.getHeight() - this.getParent().sprite.getHeight());
			
			//animationLabel.setLocation(new Point(targetDestination.x - 100 * this.getParent().facingLeft, targetDestination.y - this.getParent().sprite.getHeight()/2));

			String slashPath = "Resources/Animations/DragonSlash";
			
			Animation slashGraphics = new GraphicAnimation(animationLabelOne, 11, this.uniqueIndex[0], 0, 1);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabelOne);
			};
			
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(animationLabelOne, JLayeredPane.MODAL_LAYER);
			};
			slashGraphics.keyframes[0] = new Keyframe(addLabel);
			
			Runnable shakeAnimation = () -> {
				AnimationPlayerModule.shakeAnimation(target);
				target.updateHealthBar();
			};
			slashGraphics.keyframes[1] = new Keyframe(shakeAnimation);
			
			JLabel animationLabelTwo = new JLabel();
			GraphicAnimation teleportOutAnimation = teleportOut(animationLabelTwo, sprite, targetDestination, sprite.getLocation(), this.uniqueIndex[2]);		
			
			JLabel animationLabelThree = new JLabel();
			GraphicAnimation teleportInAnimation = teleportIn(animationLabelThree, sprite, sprite.getLocation(), targetDestination, this.uniqueIndex[1]);

			JLabel animationLabelFour = new JLabel();
			GraphicAnimation teleportOutAnimationTwo = teleportOut(animationLabelFour, sprite, sprite.getLocation(), targetDestination, this.uniqueIndex[2]);		
			
			JLabel animationLabelFive = new JLabel();
			GraphicAnimation teleportInAnimationTwo = teleportIn(animationLabelFive, sprite, targetDestination, sprite.getLocation(), this.uniqueIndex[1]);
			
			
			Runnable removeLabelTwo = () -> {
				CombatInterface.layerOnePane.remove(animationLabelTwo);
			};
			
			Runnable removeLabelThree = () -> {
				CombatInterface.layerOnePane.remove(animationLabelThree);
			};
			
			Runnable removeLabelFour = () -> {
				CombatInterface.layerOnePane.remove(animationLabelFour);
			};
			
			Runnable removeLabelFive = () -> {
				CombatInterface.layerOnePane.remove(animationLabelFive);
			};
			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(teleportOutAnimation);
			animationsList.add(teleportInAnimation);
			animationsList.add(slashGraphics);
			animationsList.add(teleportOutAnimationTwo);
			animationsList.add(teleportInAnimationTwo);
			
			
			Animation finalAnimation = new CombinedAnimation(66, animationsList, new int[]{0, 12, 30, 45, 57});
			finalAnimation.keyframes[6] = new Keyframe(removeLabelTwo);
			finalAnimation.keyframes[20] = new Keyframe(removeLabelThree);
			finalAnimation.keyframes[41] = new Keyframe(removeLabel);
			finalAnimation.keyframes[52] = new Keyframe(removeLabelFour);
			finalAnimation.keyframes[65] = new Keyframe(removeLabelFive);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
