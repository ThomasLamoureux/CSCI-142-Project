package combatEntities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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

public class DralyaDragonForm extends CombatEntity {
	
	public static JLabel getDralyaDragonSprite() {
		JLabel sprite = new JLabel();
		sprite.setPreferredSize(new Dimension(300, 300));
		sprite.setSize(new Dimension(300, 300));
		sprite.setBackground(new Color(20, 0, 255));
		sprite.setOpaque(false);
		
		return sprite;
	}
	
	// Animation used for when the dragon teleports to a location
	public static GraphicAnimation teleportIn(JLabel animationLabel, JLabel sprite, Point destination, Point location, int index) {
		animationLabel.setSize(new Dimension((int)(300), (int)(300)));
		Window.scaleComponent(animationLabel);
		
		// Offsets the position of the label based of the 2 sprite's width
		int offset = (animationLabel.getWidth() - sprite.getWidth())/2;
		Point animationLocationThree = new Point(
				location.x - offset,
				location.y - offset
				);

		
		animationLabel.setLocation(animationLocationThree);
		
		
		GraphicAnimation teleportInAnimation = new GraphicAnimation(animationLabel, 7, index, 0, 1);
		
		Runnable addLabel = () -> {
			CombatInterface.layerOnePane.add(animationLabel, JLayeredPane.MODAL_LAYER);
		};
		
		// Sets sprite to visible
		Runnable setVisible = () -> {
			sprite.setVisible(true);;
		};
		
		teleportInAnimation.keyframes[0] = new Keyframe(addLabel);
		teleportInAnimation.keyframes[4] = new Keyframe(setVisible);
		
		return teleportInAnimation;
	}
	
	// Animation used for when the dragon teleports from a location
	public static GraphicAnimation teleportOut(JLabel animationLabel, JLabel sprite, Point destination, Point location, int index) {
		animationLabel.setSize(new Dimension((int)(300), (int)(300)));
		Window.scaleComponent(animationLabel);
		
		// Offsets the position of the label based of the 2 sprite's width
		int offset = (animationLabel.getWidth() - sprite.getWidth())/2;
		Point animationLocationTwo = new Point(
				location.x - offset,
				location.y - offset
				);

		
		animationLabel.setLocation(animationLocationTwo);
		
		
		GraphicAnimation teleportOutAnimation = new GraphicAnimation(animationLabel, 6, index, 0, 1);
		
		Runnable addLabel = () -> {
			CombatInterface.layerOnePane.add(animationLabel, JLayeredPane.MODAL_LAYER);
		};
		
		// Sets the dargon invisible before moving
		Runnable setInvisibleAndMove = () -> {
			sprite.setVisible(false);;
			sprite.setLocation(destination);
		};

		
		teleportOutAnimation.keyframes[0] = new Keyframe(addLabel);
		teleportOutAnimation.keyframes[2] = new Keyframe(setInvisibleAndMove);
		
		
		return teleportOutAnimation;
	}
	
	// Dralya the greatest of dragons
	public DralyaDragonForm(boolean flip) {
		super("Dralya", 525, null, getDralyaDragonSprite(), flip);
		
		File targetFile = new File("Resources/Images/DralyaDragonForm.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		DragonSlash move1 = new DragonSlash(this);
		PowerfulDragonSlash move2 = new PowerfulDragonSlash(this);
		// Damage editing
		move1.setDamage(80);
		move2.setDamage(80);
		
		Move[] moveSet = {move1, move2, new FireLances(this)};
		this.setMoveSet(moveSet);
	}	
	
	
	@Override
	public void reset() {
		this.health = this.maxHealth;
		this.damageMultiplier = 1.0;
		this.damageResistence = 0.0;
		this.dead = false;
		// Custom
		this.sprite = getDralyaDragonSprite();
	}
	
	
	// Damage to a single enemy
	public static class DragonSlash extends Move {

		public DragonSlash(CombatEntity parent) {
			super("Dragon Slash", new boolean[]{true, false, false}, parent);
		
			this.setDamage(100);
			this.setDescription("Targets a single enemy with a slashing attack");
		}
		
		@Override
		protected void preloadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/DragonSlash", new Dimension(500, 500), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportIn", new Dimension(300, 300), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportOut", new Dimension(300, 300), !this.getParent().flipImages),
					};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {
			JLabel sprite = this.getParent().sprite;
			
			JLabel animationLabelOne = new JLabel();
			animationLabelOne.setSize(new Dimension((int)(500), (int)(500)));
			Window.scaleComponent(animationLabelOne);
			
			GameSound sound = new GameSound("Resources/Sounds/Claw.wav");
			
			int offset = (animationLabelOne.getWidth() - target.sprite.getWidth())/2;
			Point animationLocation = new Point(
					target.sprite.getX() - offset,
					target.sprite.getY() - offset
					);

			
			animationLabelOne.setLocation(animationLocation);
			
			
			Point targetDestination = new Point((int)
					(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), 
					target.sprite.getLocation().y + target.sprite.getHeight() - this.getParent().sprite.getHeight());

			
			Animation slashGraphics = new GraphicAnimation(animationLabelOne, 11, this.uniqueIndex[0], 0, 1);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabelOne);
			};
			
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(animationLabelOne, JLayeredPane.MODAL_LAYER);
				sound.play();
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
	
	// Damages a single enemy
	public static class PowerfulDragonSlash extends Move {
		private int bloodCost = 0; // Move will bleed the user by however this much is 

		public PowerfulDragonSlash(CombatEntity parent) {
			super("Empowered Dragon Slash", new boolean[]{true, false, false}, parent);
		
			this.setDamage(150);
			this.setDescription("Targets a single enemy with a powerful slashing attack at the cost of bleeding 30 HP.");
		}
		
		public void setBloodCost(int blood) {
			this.bloodCost = blood;
		}
		
		@Override
		public void useMove(CombatEntity target) {
			target.recieveDamage((int)(this.getDamage() * this.getParent().damageMultiplier));
			
			this.getParent().recieveDamage(bloodCost);
			this.getParent().updateHealthBar();
			
			runAnimation(target);
		}
		
		
		@Override
		protected void preloadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/DragonPowerfulSlash", new Dimension(500, 500), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportIn", new Dimension(300, 300), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportOut", new Dimension(300, 300), !this.getParent().flipImages),
					};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {
			JLabel sprite = this.getParent().sprite;
			
			JLabel animationLabelOne = new JLabel();
			animationLabelOne.setSize(new Dimension((int)(600), (int)(600)));
			Window.scaleComponent(animationLabelOne);
			
			GameSound sound = new GameSound("Resources/Sounds/Claw.wav");
			
			int offset = (animationLabelOne.getWidth() - target.sprite.getWidth())/2;
			Point animationLocation = new Point(
					target.sprite.getX() - offset,
					target.sprite.getY() - offset
					);

			
			animationLabelOne.setLocation(animationLocation);
			
			
			Point targetDestination = new Point((int)
					(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), 
					target.sprite.getLocation().y + target.sprite.getHeight() - this.getParent().sprite.getHeight());

			
			Animation slashGraphics = new GraphicAnimation(animationLabelOne, 25, this.uniqueIndex[0], 0, 1);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabelOne);
			};
			
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(animationLabelOne, JLayeredPane.MODAL_LAYER);
				sound.play();
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
	
	// Damage to a single enemy
	public static class FireLances extends Move {

		public FireLances(CombatEntity parent) {
			super("Fire Lances", new boolean[]{true, false, false}, parent);
		
			this.setDamage(80);
			this.setDescription("Targets a single enemy with a slashing attack");
		}
		
		
		@Override
		protected void preloadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/FireLance", new Dimension(300, 300), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/Explosion", new Dimension(300, 300), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportIn", new Dimension(300, 300), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/DragonTeleportOut", new Dimension(300, 300), !this.getParent().flipImages),
					};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity target) {
			JLabel sprite = this.getParent().sprite;
			
			Point[] targetDestinations = {
					new Point(960 - 240 * this.getParent().facingLeft, 650),
					new Point(960 - 320 * this.getParent().facingLeft, 620),
					new Point(960 - 400 * this.getParent().facingLeft, 670),
					new Point(960 - 480 * this.getParent().facingLeft, 630),
					new Point(960 - 560 * this.getParent().facingLeft, 680),
					new Point(960 - 640 * this.getParent().facingLeft, 660),
					new Point(960 - 720 * this.getParent().facingLeft, 610),
					new Point(960 - 800 * this.getParent().facingLeft, 690)
					};
			Point[] startingLocations = {
					new Point(960 + 800 * this.getParent().facingLeft, -250),
					new Point(960 + 720 * this.getParent().facingLeft, -250),
					new Point(960 + 640 * this.getParent().facingLeft, -250),
					new Point(960 + 560 * this.getParent().facingLeft, -250),
					new Point(960 + 480 * this.getParent().facingLeft, -250),
					new Point(960 + 400 * this.getParent().facingLeft, -250),
					new Point(960 + 320 * this.getParent().facingLeft, -250),
					new Point(960 + 240 * this.getParent().facingLeft, -250)
					};
			int[] startingTimes = {
					60,
					74,
					92,
					85,
					89,
					100,
					78,
					82
			};
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			int[] animationStartingTimes = new int[28];
			int index = 0;
			for (int i = 0; i < 8; i++) {
				Point targetDestination = Window.scalePoint(targetDestinations[i]);
				Point startingLocation = Window.scalePoint(startingLocations[i]);
				animationStartingTimes[index] = startingTimes[i];
				animationStartingTimes[index + 1] = startingTimes[i];
				animationStartingTimes[index + 2] = startingTimes[i] + 35;
				index += 3;
				
				JLabel lanceLabel = new JLabel();
				lanceLabel.setSize(new Dimension((int)(250), (int)(250)));
				Window.scaleComponent(lanceLabel);

				lanceLabel.setLocation(startingLocation);
				
				GraphicAnimation lanceGraphics = new GraphicAnimation(lanceLabel, 20, this.uniqueIndex[0], 0, 4);
				lanceGraphics.setLooped(true);
				lanceGraphics.setLoopStartIndex(1);
				
				Runnable addLanceLabel = () -> {
					CombatInterface.layerOnePane.add(lanceLabel, JLayeredPane.MODAL_LAYER);
				};
				lanceGraphics.keyframes[0] = new Keyframe(addLanceLabel);
				
				
				MovementAnimation lanceMovement =  new MovementAnimation(lanceLabel, 35, "linear", targetDestination, null);
				
				
				JLabel explosionLabel = new JLabel();
				explosionLabel.setSize(new Dimension((int)(250), (int)(250)));
				Window.scaleComponent(explosionLabel);
				

				Point explosionLocation = new Point(targetDestination);
				explosionLabel.setLocation(explosionLocation);
				
				GraphicAnimation explosionGraphics = new GraphicAnimation(explosionLabel, 21, this.uniqueIndex[1], 0, 3);
				
				Runnable removeExplosionLabel = () -> {
					CombatInterface.layerOnePane.remove(explosionLabel);
				};
				
				Runnable addEplosionLabel = () -> {
					CombatInterface.layerOnePane.add(explosionLabel, JLayeredPane.MODAL_LAYER);
					CombatInterface.layerOnePane.remove(lanceLabel);
				};
				explosionGraphics.keyframes[0] = new Keyframe(addEplosionLabel);
				explosionGraphics.keyframes[20] = new Keyframe(removeExplosionLabel);
				
				animationsList.add(lanceGraphics);
				animationsList.add(lanceMovement);
				animationsList.add(explosionGraphics);
			}
			
			
			Runnable shakeAnimation = () -> {
				AnimationPlayerModule.shakeAnimation(target);
				target.updateHealthBar();
			};
			
			
			Point targetDestination = new Point(1020, 100);
			

			JLabel animationLabelTwo = new JLabel();
			GraphicAnimation teleportOutAnimation = teleportOut(animationLabelTwo, sprite, targetDestination, sprite.getLocation(), this.uniqueIndex[3]);		
			
			JLabel animationLabelThree = new JLabel();
			GraphicAnimation teleportInAnimation = teleportIn(animationLabelThree, sprite, sprite.getLocation(), targetDestination, this.uniqueIndex[2]);

			JLabel animationLabelFour = new JLabel();
			GraphicAnimation teleportOutAnimationTwo = teleportOut(animationLabelFour, sprite, sprite.getLocation(), targetDestination, this.uniqueIndex[3]);		
			
			JLabel animationLabelFive = new JLabel();
			GraphicAnimation teleportInAnimationTwo = teleportIn(animationLabelFive, sprite, targetDestination, sprite.getLocation(), this.uniqueIndex[2]);
			
			
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
			
			// Changes the sprite to a flying version
			Runnable changeToFlyingDragon = () -> {
				File targetFile = new File("Resources/Images/DragonFly.png");
				Image image = null;
				try {
					image = ImageIO.read(targetFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				image = Window.scaleImage(300, 300, image);
				
				if (this.getParent().facingLeft == 1 && this.getParent().flipIfFacingLeft == true) {
					image = AnimationPlayerModule.createMirror(image);
				} else if (this.getParent().facingLeft == -1 && this.getParent().flipIfFacingLeft == false) {
					image = AnimationPlayerModule.createMirror(image);
				}
				
				ImageIcon icon = new ImageIcon(image);
				this.getParent().sprite.setIcon(icon);
			};
			
			Icon currentIcon = this.getParent().sprite.getIcon();
			
			Runnable changeToNormalForm = () -> {
				this.getParent().sprite.setIcon(currentIcon);
			};
			
			
			animationsList.add(teleportOutAnimation);
			animationsList.add(teleportInAnimation);
			animationsList.add(teleportOutAnimationTwo);
			animationsList.add(teleportInAnimationTwo);
			animationStartingTimes[index] = 0;
			animationStartingTimes[index + 1] = 12;
			animationStartingTimes[index + 2] = 110;
			animationStartingTimes[index + 3] = 122;
			
			GameSound sound = new GameSound("Resources/Sounds/FireLance.wav");
			
			Runnable playSound = () -> {
				sound.play();
			};
			
			Animation finalAnimation = new CombinedAnimation(160, animationsList, animationStartingTimes);
			finalAnimation.keyframes[6] = new Keyframe(removeLabelTwo);
			finalAnimation.keyframes[3] = new Keyframe(changeToFlyingDragon);
			finalAnimation.keyframes[20] = new Keyframe(removeLabelThree);
			finalAnimation.keyframes[30] = new Keyframe(playSound);
			finalAnimation.keyframes[117] = new Keyframe(removeLabelFour);
			finalAnimation.keyframes[114] = new Keyframe(changeToNormalForm);
			finalAnimation.keyframes[128] = new Keyframe(removeLabelFive);
			finalAnimation.keyframes[100] = new Keyframe(shakeAnimation);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
