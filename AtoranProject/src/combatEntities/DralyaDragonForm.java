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

public class DralyaDragonForm extends CombatEntity {
	
	public static JLabel getDralyaDragonSprite() {
		JLabel sprite = new JLabel("Dralya");
		sprite.setPreferredSize(new Dimension(300, 300));
		sprite.setSize(new Dimension(300, 300));
		sprite.setBackground(new Color(20, 0, 255));
		sprite.setOpaque(false);
		
		return sprite;
	}
	
	public static GraphicAnimation teleportIn(JLabel animationLabel, JLabel sprite, Point destination, Point location, int index) {
		animationLabel.setSize(new Dimension((int)(300), (int)(300)));
		Window.scaleComponent(animationLabel);
		
		int offsetThree = (animationLabel.getWidth() - sprite.getWidth())/2;
		Point animationLocationThree = new Point(
				location.x - offsetThree,
				location.y - offsetThree
				);

		
		animationLabel.setLocation(animationLocationThree);
		
		
		String TeleportOutIn = "Resources/Animations/DragonTeleportIn";
		
		
		GraphicAnimation teleportInAnimation = new GraphicAnimation(animationLabel, 7, index, 0, 1);
		
		Runnable addLabel = () -> {
			CombatInterface.layerOnePane.add(animationLabel, JLayeredPane.MODAL_LAYER);
			System.out.println("WWWWWWWWWWWW");
		};
		
		
		Runnable setVisible = () -> {
			sprite.setVisible(true);;
		};
		
		teleportInAnimation.keyframes[0] = new Keyframe(addLabel);
		teleportInAnimation.keyframes[4] = new Keyframe(setVisible);
		
		return teleportInAnimation;
	}
	
	public static GraphicAnimation teleportOut(JLabel animationLabel, JLabel sprite, Point destination, Point location, int index) {
		animationLabel.setSize(new Dimension((int)(300), (int)(300)));
		Window.scaleComponent(animationLabel);
		
		int offsetTwo = (animationLabel.getWidth() - sprite.getWidth())/2;
		Point animationLocationTwo = new Point(
				location.x - offsetTwo,
				location.y - offsetTwo
				);

		
		animationLabel.setLocation(animationLocationTwo);
		
		
		String TeleportOutPath = "Resources/Animations/DragonTeleportOut";
		
		
		GraphicAnimation teleportOutAnimation = new GraphicAnimation(animationLabel, 6, index, 0, 1);
		
		Runnable addLabel = () -> {
			CombatInterface.layerOnePane.add(animationLabel, JLayeredPane.MODAL_LAYER);
		};
		
		
		Runnable setInvisibleAndMove = () -> {
			sprite.setVisible(false);;
			sprite.setLocation(destination);
		};

		
		teleportOutAnimation.keyframes[0] = new Keyframe(addLabel);
		teleportOutAnimation.keyframes[2] = new Keyframe(setInvisibleAndMove);
		
		
		return teleportOutAnimation;
	}
	
	public DralyaDragonForm(boolean flip) {
		super("Dralya", 250, null, getDralyaDragonSprite(), flip);
		
		File targetFile = new File("Resources/Images/DralyaDragonForm.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		Move[] moveSet = {new DragonSlash(this), new PowerfulDragonSlash(this)};
		this.setMoveSet(moveSet);
	}	
	
	
	
	public static class DragonSlash extends Move {

		public DragonSlash(CombatEntity parent) {
			super("Dragon Slash", false, true, parent);
		
			this.setDamage(100);
			this.setDescription("Targets a single enemy with a slashing attack");
			
			preLoadAnimations();
		}
		
		@Override
		protected void preLoadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/DragonSlash", new Dimension(500, 500), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportIn", new Dimension(300, 300), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportOut", new Dimension(300, 300), !this.getParent().flipImages),
					};
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
	
	public static class PowerfulDragonSlash extends Move {

		public PowerfulDragonSlash(CombatEntity parent) {
			super("Empowered Dragon Slash", false, true, parent);
		
			this.setDamage(150);
			this.setDescription("Targets a single enemy with a slashing attack");
			
			preLoadAnimations();
		}
		
		
		@Override
		protected void preLoadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/DragonPowerfulSlash", new Dimension(500, 500), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportIn", new Dimension(300, 300), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportOut", new Dimension(300, 300), !this.getParent().flipImages),
					};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {
			boolean flipImage = false;
			if (this.getParent().facingLeft == 1) {
				flipImage = true;
			}
			
			JLabel sprite = this.getParent().sprite;
			
			JLabel animationLabelOne = new JLabel();
			animationLabelOne.setSize(new Dimension((int)(600), (int)(600)));
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

			String slashPath = "Resources/Animations/DragonPowerfulSlash";
			
			Animation slashGraphics = new GraphicAnimation(animationLabelOne, 25, this.uniqueIndex[0], 0, 1);
			
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
			
			
			Animation finalAnimation = new CombinedAnimation(80, animationsList, new int[]{0, 12, 30, 59, 71});
			finalAnimation.keyframes[6] = new Keyframe(removeLabelTwo);
			finalAnimation.keyframes[20] = new Keyframe(removeLabelThree);
			finalAnimation.keyframes[55] = new Keyframe(removeLabel);
			finalAnimation.keyframes[66] = new Keyframe(removeLabelFour);
			finalAnimation.keyframes[79] = new Keyframe(removeLabelFive);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
