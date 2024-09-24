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

	public static boolean playFrame(Animation animation) {
		if (animation.playFrame() == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void playAnimations() {
		ArrayList<Integer> removals = new ArrayList<Integer>();
		
		for (int i = 0; i < animations.size(); i++) {
			if (i >= animations.size()) {
				break;
			}
			boolean complete = playFrame(animations.get(i));
			
			if (complete == true) {
				removals.add(i);
			}
		}
		
		
		for (int i = 0; i < removals.size(); i++) {
			animations.remove(removals.get(i) - i);
		}
	}
	
	public static void addAnimation(Animation animation) {
		animations.add(animation);
	}
	
	public static ImageIcon[] createIconsFromFolder(String path, Dimension size, boolean flip) {
		
		File folder = new File(path);
		File[] content = folder.listFiles();
		
		Image[] images = new Image[content.length];
		ImageIcon[] icons = new ImageIcon[content.length];
		
		if (content != null) {
			for (int i = 0; i < icons.length; i++) {
				File file = content[i];
				
				BufferedImage image = null;
				
				try {
					image = ImageIO.read(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (flip) {
					image = createMirror(image);
				}
				
				Window.scaleImage(size.width, size.height, image);
				
				
				icons[i] = new ImageIcon(image);
			}
		}
		
		return icons;
	}
	
	
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
	
	
    public static BufferedImage createMirror(Image image) {
		BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D draw = newImage.createGraphics();
		draw.translate(newImage.getWidth(), 0);
		draw.scale(-1, 1);
		draw.drawImage(image, 0, 0, null);
		
		draw.dispose();
		
		return newImage;
    }
    
    public static BufferedImage rotateImage(Image image, int degrees) {
		BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D draw = newImage.createGraphics();
		draw.rotate(Math.PI / degrees, newImage.getWidth() / 2, newImage.getHeight() / 2);
		draw.drawImage(image, 0, 0, null);
		
		draw.dispose();
		
		return newImage;
    }
	
	/*public static void main(String[] args) {
		createIconsFromFolder("Resources\\Animations\\SlashingAnimation");
	}*/
}
