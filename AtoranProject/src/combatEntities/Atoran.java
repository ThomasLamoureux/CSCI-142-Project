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
import combat.EntitiesAndMoves.BumpMove;
import combat.EntitiesAndMoves.SamohtMultiHit;
import combat.EntitiesAndMoves.SamohtSingleHit;
import combat.EntitiesAndMoves.SlashMove;
import combat.EntitiesAndMoves.SweepMove;
import main.Window;
import utilities.AnimationPlayerModule;
import utilities.AnimationsPreloader;

public class Atoran extends CombatEntity {
	private boolean empowered;
	
	public static JLabel getAtoranSprite() {
		JLabel atoranSprite = new JLabel("Atoran");
		atoranSprite.setPreferredSize(new Dimension(225, 225));
		atoranSprite.setSize(new Dimension(225, 225));
		atoranSprite.setBackground(new Color(20, 0, 255));
		atoranSprite.setOpaque(false);
		
		return atoranSprite;
	}

	// Atoran entity, the main character
	public Atoran(boolean flip) {
		super("Atoran", 250, null, getAtoranSprite(), flip);
		
		File targetFile = new File("Resources/Images/AtoranStand.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = true;
		
		Move[] moveSet = {new SlashMove(this), new SweepMove(this), new SweepMove(this),
				new SlashMove(this), new SweepMove(this), new SweepMove(this)};
		this.setMoveSet(moveSet);
	}	

	
	public static class SlashMove extends Move {

		public SlashMove(CombatEntity parent) {
			super("Slash", false, true, parent);
		
			this.setDamage(80);
			this.setDescription("Targets a single enemy with a slashing attack");
			
			preLoadAnimations();
		}
		
		
		private void empoweredSlash(CombatEntity target) {
			
		}
		
		private void slash(CombatEntity target) {
			
		}
		
		
		@Override
		public void useMove(CombatEntity target) {
			target.recieveDamage(damage);
			
			if (this.getParent().empowered == true) {
				
			}
			runAnimation(target);
		}
		
		
		@Override
		protected void preLoadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/NewSlashingAnimation", new Dimension(500, 500), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/SweepAnimation", new Dimension(300, 300), !this.getParent().flipImages),
					};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {
			boolean flipImage = false;
			if (this.getParent().facingLeft == 1) {
				flipImage = true;
			}
			
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension((int)(550 * 1.2), (int)(400 * 1.2)));
			Window.scaleComponent(animationLabel);
			
			Point targetDestination = new Point((int)
					(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), 
					target.sprite.getLocation().y + target.sprite.getHeight() - this.getParent().sprite.getHeight());
			
			animationLabel.setLocation(new Point(targetDestination.x - 100, targetDestination.y - this.getParent().sprite.getHeight()/2));

			Animation moveToTarget = new MovementAnimation(this.getParent().sprite, 24, "easeOutQuart", targetDestination, null);
			Animation moveBack = new MovementAnimation(this.getParent().sprite, 22, "easeOutQuart", this.getParent().sprite.getLocation(), targetDestination);

			String folderPath = "Resources/Animations/NewSlashingAnimation";
			
			Animation graphics = new GraphicAnimation(animationLabel, 6, folderPath, 0, 1, flipImage);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabel);
			};
			
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
			
			Animation finalAnimation = new CombinedAnimation(72, animationsList, new int[]{0, 30, 50});
			finalAnimation.keyframes[37] = new Keyframe(removeLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
\
	
	public static class SweepMove extends Move {

		public SweepMove(CombatEntity parent) {
			super("Sweep", false, true, parent);
		
			this.setDamage(50);
			this.setDescription("Targets all enemies on the field with a sweeping attack");
		}
		
		@Override
		public void useMove(CombatEntity target) {
			CombatEntity[] enemies = Combat.currentCombatInstance.notCurrentTeam.members;
			
			for (int i = 0; i < enemies.length; i++) {
				enemies[i].recieveDamage(this.getDamage());
			}
			
			runAnimation(target);
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {
			boolean flipImage = false;
			if (this.getParent().facingLeft == 1) {
				flipImage = true;
			}
			
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension((int)(550 * 1.2), (int)(400 * 1.2)));
			Window.scaleComponent(animationLabel);
			
			Point targetDestination = new Point((int)
					(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), 
					target.sprite.getLocation().y + target.sprite.getHeight() - this.getParent().sprite.getHeight());
			
			animationLabel.setLocation(new Point(targetDestination.x - Window.scaleInt(85), targetDestination.y - this.getParent().sprite.getHeight()/2));

			Animation moveToTarget = new MovementAnimation(this.getParent().sprite, 24, "easeOutQuart", targetDestination, null);
			Animation moveBack = new MovementAnimation(this.getParent().sprite, 22, "easeOutQuart", this.getParent().sprite.getLocation(), targetDestination);

			String folderPath = "Resources/Animations/SweepAnimation";
			
			Animation graphics = new GraphicAnimation(animationLabel, 7, folderPath, 0, 1, flipImage);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabel);
			};
			moveBack.keyframes[0] = new Keyframe(removeLabel);
			
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(animationLabel, JLayeredPane.MODAL_LAYER);
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
			
			Animation finalAnimation = new CombinedAnimation(68, animationsList, new int[]{0, 31, 46});
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
