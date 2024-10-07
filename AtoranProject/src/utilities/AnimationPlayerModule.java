package utilities;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import animations.Animation;
import animations.Animation.MovementAnimation;
import animations.Animation.CombinedAnimation;
import combat.CombatEntity;
import main.Window;

public class AnimationPlayerModule {
	private static ArrayList<Animation> animations = new ArrayList<Animation>();

	// Plays the frame of the animation
	public static boolean playFrame(Animation animation) {
		if (animation.playFrame() == true) { // true == animation finished
			return true;
		} else {
			return false;
		}
	}
	
	// Goes through all animations and plays them
	public static void playAnimations() {
		ArrayList<Integer> removals = new ArrayList<Integer>(); // Animations that are complete and must be removed
		
		for (int i = 0; i < animations.size(); i++) {
			if (i >= animations.size()) { // Breaks
				break;
			}
			boolean complete = playFrame(animations.get(i)); // Plays frame, if it returns true then that animation is complete
			
			if (complete == true) { // The completed animation is set to be removed
				removals.add(i);
			}
		}
		
		
		for (int i = 0; i < removals.size(); i++) { // Removes animations
			animations.remove(removals.get(i) - i); // - i is to account for shifting indexes
		}
	}
	
	// Adds an animation to be played
	public static void addAnimation(Animation animation) {
		animations.add(animation);
	}
	
	// Creates image icons from a folder path
	public static ImageIcon[] createIconsFromFolder(String path, Dimension size, boolean flip) {
		
		File folder = new File(path);
		File[] content = folder.listFiles();

		ImageIcon[] icons = new ImageIcon[content.length];
		
		if (content != null) {
			// Loops through content
			for (int i = 0; i < icons.length; i++) {
				File file = content[i];
				
				BufferedImage image = null;
				
				try {
					image = ImageIO.read(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (flip) { // Flips the image
					image = createMirror(image);
				}
				// Resizes based on desired size
				Image reSizedImage = Window.scaleImage(size.width, size.height, image);
				
				icons[i] = new ImageIcon(reSizedImage);
			}
		}
		
		return icons;
	}
	
	// Prebuilt animation for shaking
	public static void shakeAnimation(CombatEntity target) {
		Point targetLocation = target.sprite.getLocation();
		
		
		Point destinationOne = new Point(targetLocation.x + Window.scaleInt(4), targetLocation.y + Window.scaleInt(5));
		Point destinationTwo = new Point(targetLocation.x - Window.scaleInt(4), targetLocation.y - Window.scaleInt(5));
		
		ArrayList<Animation> animationsList = new ArrayList<Animation>();
		
		for (int i = 0; i < 5; i++) {
			if (i % 2 == 1) {
				animationsList.add(new MovementAnimation(target.sprite, 4, "easeInOutSine", destinationTwo, null));
			} else {
				animationsList.add(new MovementAnimation(target.sprite, 4, "easeInOutSine", destinationOne, null));
			}
		}
		
		animationsList.add(new MovementAnimation(target.sprite, 4, "easeInOutSine", targetLocation, null));
		Animation CombinedAnimation = new CombinedAnimation(4 * 6, animationsList, new int[] {0, 4, 8, 12, 16, 20});
		
		addAnimation(CombinedAnimation);
	}
	
	// Creates a mirror image
    public static BufferedImage createMirror(Image image) {
		BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D draw = newImage.createGraphics();
		draw.translate(newImage.getWidth(), 0);
		draw.scale(-1, 1);
		draw.drawImage(image, 0, 0, null);
		
		draw.dispose();
		
		return newImage;
    }
}
