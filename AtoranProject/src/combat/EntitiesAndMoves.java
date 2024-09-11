package combat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import animations.Animation;
import animations.Keyframe;
import main.Window;
import utilities.AnimationPlayerModule;

public class EntitiesAndMoves {
	
	public static JLabel getAtoranSprite() {
		JLabel atoranSprite = new JLabel("Atoran");
		atoranSprite.setPreferredSize(new Dimension(200, 200));
		atoranSprite.setSize(new Dimension(200, 200));
		atoranSprite.setBackground(new Color(20, 0, 255));
		atoranSprite.setOpaque(false);
		
		return atoranSprite;
	}
	
	public static JLabel getSlimeSprite() {
		JLabel slimeSprite = new JLabel("Atoran");
		slimeSprite.setPreferredSize(new Dimension(100, 100));
		slimeSprite.setSize(new Dimension(100, 100));
		slimeSprite.setBackground(new Color(20, 0, 255));
		slimeSprite.setOpaque(false);
		
		return slimeSprite;
	}
	
	// Atoran entity, the main character
	public static class AtoranEntity extends CombatEntity {
		public AtoranEntity() {
			super("Atoran", 250, null, getAtoranSprite());
			
			File targetFile = new File("Resources/Images/AtoranStand.png");
			this.setImageFile(targetFile);
			
			this.flipIfFacingLeft = true;
			
			Move[] moveSet = {new SlashMove(this), new SweepMove(this)};
			this.setMoveSet(moveSet);
		}	
	}
	
	
	public static class SlimeEntity extends CombatEntity {
		public SlimeEntity() {
			super("Slime", 25, null, getSlimeSprite());
			
			File targetFile = new File("Resources/Images/BlueslimeStill.png");
			this.setImageFile(targetFile);
			
			this.flipIfFacingLeft = false;
			
			Move[] moveSet = {new BumpMove(this)};
			this.setMoveSet(moveSet);
		}
	}

	
	public static class BanditEntity extends CombatEntity {

		public BanditEntity() {
			super("Bandit", 50, null, null);
			
			this.flipIfFacingLeft = true;
			
			File targetFile = new File("Resources/Images/AtoranStand.png");
			this.setImageFile(targetFile);
			
			Move[] moveSet = null;
		}
	}

	
	public static class SlashMove extends Move {

		public SlashMove(CombatEntity parent) {
			super("Slash", false, true, parent);
		
			this.setDamage(20);
		}
		
		@Override
		protected void runAnimation(CombatEntity target) {
			Point targetDestination = new Point((int)(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), target.sprite.getLocation().y);
			//targetDestination = Window.scalePoint(targetDestination);
			
			Point[] destinations = {targetDestination, null, this.getParent().sprite.getLocation()};
			int[] framesToTake = {24, 28, 22};
			Keyframe[] keyframes = new Keyframe[24 + 28 + 22];
			
			Image[] images = AnimationPlayerModule.createIconsFromFolder("Resources/Animations/SlashingAnimation");
			
			
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension(434, 230));

			int index = 24;
			for (int i = 0; i < images.length; i++) {
				final int fi = i;
				Runnable method = () -> {
					Image image;
					if (this.getParent().facingLeft == -1) {
						image = AnimationPlayerModule.createMirror(images[fi]);
					} else {
						image = images[fi];
					}
					
					image = Window.scaleImage(434, 230, image);
					
					animationLabel.setIcon(new ImageIcon(image));
					Point spriteLocation = this.getParent().sprite.getLocation();
					Point location = new Point(spriteLocation.x - 90, spriteLocation.y - 100);
					animationLabel.setLocation(location);
					if (fi == 0) {
						CombatPlayerInteractions.layerOnePane.add(animationLabel, JLayeredPane.PALETTE_LAYER);
					} else if (fi == 2) {
						AnimationPlayerModule.shakeAnimation(target);
						target.updateHealthBar();
					}
					System.out.println("method played");
				};
				
				Keyframe keyframe = new Keyframe(method);
				keyframes[i + index] = keyframe;
				index += 2;
				System.out.println("made key");
			}
			
			Runnable method = () -> {
				CombatPlayerInteractions.layerOnePane.remove(animationLabel);
			};
			
			keyframes[index + 2] = new Keyframe(method);
			
			
			Animation animation = new Animation(this.getParent().sprite, destinations, framesToTake, "easeOutQuart");
			animation.keyframes = keyframes;
			
			AnimationPlayerModule.addAnimation(animation);
		}
	}
	
	public static class BumpMove extends Move {

		public BumpMove(CombatEntity parent) {
			super("Bump", false, true, parent);

			this.setDamage(8);
		}
		
		@Override
		protected void runAnimation(CombatEntity target) {
			Point targetDestination = new Point((int)(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), target.sprite.getLocation().y);
			
			Point[] destinations = {targetDestination, null, this.getParent().sprite.getLocation()};
			int[] framesToTake = {24, 28, 22};
			Keyframe[] keyframes = new Keyframe[24 + 28 + 22];
			
			Image[] images = AnimationPlayerModule.createIconsFromFolder("Resources/Animations/SlashingAnimation");
			
			
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension(434, 230));

			int index = 24;
			for (int i = 0; i < images.length; i++) {
				final int fi = i;
				Runnable method = () -> {
					Image image;
					if (this.getParent().facingLeft == -1) {
						image = AnimationPlayerModule.createMirror(images[fi]);
					} else {
						image = images[fi];
					}
					
					image = Window.scaleImage(434, 230, image);
					
					animationLabel.setIcon(new ImageIcon(image));
					Point spriteLocation = this.getParent().sprite.getLocation();
					Point location = new Point(spriteLocation.x - 280 * this.getParent().facingLeft, spriteLocation.y - 100);
					animationLabel.setLocation(location);
					if (fi == 0) {
						CombatPlayerInteractions.layerOnePane.add(animationLabel, JLayeredPane.PALETTE_LAYER);
					} else if (fi == 2) {
						AnimationPlayerModule.shakeAnimation(target);
						target.updateHealthBar();
					}
					System.out.println("method played");
				};
				
				Keyframe keyframe = new Keyframe(method);
				keyframes[i + index] = keyframe;
				index += 2;
				System.out.println("made key");
			}
			
			Runnable method = () -> {
				CombatPlayerInteractions.layerOnePane.remove(animationLabel);
			};
			
			keyframes[index + 2] = new Keyframe(method);
			
			
			Animation animation = new Animation(this.getParent().sprite, destinations, framesToTake, "easeOutQuart");
			animation.keyframes = keyframes;
			
			AnimationPlayerModule.addAnimation(animation);
		}
	}
	
	public static class SweepMove extends Move {

		public SweepMove(CombatEntity parent) {
			super("Sweep", false, true, parent);
		
			this.setDamage(13);
		}
		
		@Override
		public void useMove(CombatEntity target) {
			CombatEntity[] enemies = Combat.notCurrentTeam.members;
			
			for (int i = 0; i < enemies.length; i++) {
				enemies[i].recieveDamage(this.getDamage());
			}
			
			runAnimation(target);
		}
		
		@Override
		protected void runAnimation(CombatEntity target) {
			Point targetDestination = new Point(1300, 500);
			targetDestination = Window.scalePoint(targetDestination);
			
			Point[] destinations = {targetDestination, null, this.getParent().sprite.getLocation()};
			int[] framesToTake = {24, 28, 22};
			Keyframe[] keyframes = new Keyframe[24 + 28 + 22];
			
			Image[] images = AnimationPlayerModule.createIconsFromFolder("Resources/Animations/SweepAnimation");
			
			
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension((int)(550 * 1.4), (int)(400 * 1.4)));
			
			final CombatEntity[] enemies = Combat.notCurrentTeam.members;

			int index = 24;
			for (int i = 0; i < images.length; i++) {
				final int fi = i;
				Runnable method = () -> {
					Image image;
					if (this.getParent().facingLeft == 1) {
						image = AnimationPlayerModule.createMirror(images[fi]);
					} else {
						image = images[fi];
					}
					
					image = Window.scaleImage((int)(550 * 1.4), (int)(400 * 1.4), image);

					animationLabel.setIcon(new ImageIcon(image));
					Point spriteLocation = this.getParent().sprite.getLocation();
					Point location = new Point(spriteLocation.x - 200, spriteLocation.y - 250);
					animationLabel.setLocation(location);
					if (fi == 0) {
						CombatPlayerInteractions.layerOnePane.add(animationLabel, JLayeredPane.PALETTE_LAYER);
					} else if (fi == 1) {
						for (int j = 0; j < enemies.length; j++) {
							AnimationPlayerModule.shakeAnimation(enemies[j]);
							enemies[j].updateHealthBar();
						}
					}
					System.out.println("method played");
				};
				
				Keyframe keyframe = new Keyframe(method);
				keyframes[i + index] = keyframe;
				index += 2;
				System.out.println("made key");
			}
			
			Runnable method = () -> {
				CombatPlayerInteractions.layerOnePane.remove(animationLabel);
			};
			
			keyframes[index + 2] = new Keyframe(method);
			
			
			Animation animation = new Animation(this.getParent().sprite, destinations, framesToTake, "easeOutQuart");
			animation.keyframes = keyframes;
			
			AnimationPlayerModule.addAnimation(animation);
		}
	}
}
