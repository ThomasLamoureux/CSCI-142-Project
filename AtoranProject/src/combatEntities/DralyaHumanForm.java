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
		JLabel atoranSprite = new JLabel();
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
		
		Move[] moveSet = {new DragonSlash(this), new PowerfulDragonSlash(this), new Sacrifice(this)};
		this.setMoveSet(moveSet);
	}	
	
	public static class Sacrifice extends Move {

		public Sacrifice(CombatEntity parent) {
			super("Sacrifice", new boolean[]{false, false, true}, parent);
		
			this.setDamage(0);
			this.setDescription("Reduces the damage taken to allies for one attack based on Dralya's current HP % in turn for reducing her health to 0");
			
			preLoadAnimations();
		}
		
		@Override
		protected void preLoadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportIn", new Dimension(500, 500), !this.getParent().flipImages)};
		}
		
		
		@Override
		public void useMove(CombatEntity target) {
			CombatEntity parent = this.getParent();
			
			double healthPercentange = (double)parent.health / (double)parent.maxHealth;
			parent.recieveDamage(20000);
			
			for (CombatEntity entity : Combat.currentCombatInstance.currentTeam.members) {
				entity.damageResistence = healthPercentange;
			}
			
			this.runAnimation(target);
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {			
			JLabel animationLabelOne = new JLabel();
			animationLabelOne.setSize(new Dimension(500, 500));
			Window.scaleComponent(animationLabelOne);
			
			int offset = (animationLabelOne.getWidth() - this.getParent().sprite.getWidth())/2;
			Point animationLocation = new Point(
					this.getParent().sprite.getX() - offset,
					this.getParent().sprite.getY() - offset
					);

			
			animationLabelOne.setLocation(animationLocation);
			
			Animation graphic = new GraphicAnimation(animationLabelOne, 24, this.uniqueIndex[0], 0, 4);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabelOne);
			};
			
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(animationLabelOne, JLayeredPane.MODAL_LAYER);
			};
			graphic.keyframes[0] = new Keyframe(addLabel);
			
			Runnable shakeAnimation = () -> {
				AnimationPlayerModule.shakeAnimation(this.getParent());
				this.getParent().updateHealthBar();
			};
			
			Runnable remove = () -> {
				this.getParent().sprite.setVisible(false);
			};
			graphic.keyframes[8] = new Keyframe(shakeAnimation);
			graphic.keyframes[16] = new Keyframe(remove);
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(graphic);
			
			
			Animation finalAnimation = new CombinedAnimation(31, animationsList, new int[]{6});
			finalAnimation.keyframes[30] = new Keyframe(removeLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
