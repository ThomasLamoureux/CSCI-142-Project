package combat;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import animations.Animation;
import animations.Keyframe;
import main.Window;
import utilities.AnimationPlayerModule;

public class EntitiesAndMoves {
	
	public static class AtoranEntity extends CombatEntity {
		public AtoranEntity() {
			super("Atoran", 150, null, null);
			
			Move[] moveSet = {new SlashMove(this), new SweepMove(this)};
			this.setMoveSet(moveSet);
		}	
	}
	
	public static class SlimeEntity extends CombatEntity {
		public SlimeEntity() {
			super("Slime", 25, null, null);
			
			Move[] moveSet = {new BumpMove(this)};
			this.setMoveSet(moveSet);
		}
	}

	public static class BanditEntity extends CombatEntity {

		public BanditEntity() {
			super("Bandit", 50, null, null);
			
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
			Point targetDestination = new Point((int)(target.sprite.getLocation().x * 0.9), target.sprite.getLocation().y);
			
			Point[] destinations = {targetDestination, null, this.getParent().sprite.getLocation()};
			int[] framesToTake = {24, 28, 22};
			Keyframe[] keyframes = new Keyframe[24 + 28 + 22];
			
			Image[] images = AnimationPlayerModule.createIconsFromFolder("Resources\\Animations\\SlashingAnimation");
			
			
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension(434, 230));

			int index = 24;
			for (int i = 0; i < images.length; i++) {
				final int fi = i;
				Runnable method = () -> {
					BufferedImage mirror = AnimationPlayerModule.createMirror(images[fi]);
					
					animationLabel.setIcon(new ImageIcon(mirror));
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
			
			Point[] destinations = {targetDestination, null, this.getParent().sprite.getLocation()};
			int[] framesToTake = {24, 28, 22};
			Keyframe[] keyframes = new Keyframe[24 + 28 + 22];
			
			Image[] images = AnimationPlayerModule.createIconsFromFolder("Resources\\Animations\\SweepAnimation");
			
			
			JLabel animationLabel = new JLabel();
			animationLabel.setSize(new Dimension((int)(550 * 1.4), (int)(400 * 1.4)));
			
			final CombatEntity[] enemies = Combat.notCurrentTeam.members;

			int index = 24;
			for (int i = 0; i < images.length; i++) {
				final int fi = i;
				Runnable method = () -> {
					//BufferedImage mirror = AnimationPlayerModule.createMirror(images[fi]);
					Image image = images[fi];
					image = image.getScaledInstance((int)(550 * 1.4), (int)(400 * 1.4), Image.SCALE_DEFAULT);
					
					//image = AnimationPlayerModule.rotateImage(image, fi);
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
