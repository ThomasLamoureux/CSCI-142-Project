package utilities;

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
		for (int i = 0; i < animations.size(); i++) {
			if (i >= animations.size()) {
				break;
			}
			boolean complete = playFrame(animations.get(i));
			
			if (complete == true) {
				animations.remove(i);
				i -= 1;
			}
		}
	}
	
	public static void addAnimation(Animation animation) {
		animations.add(animation);
	}
	
	public static Image[] createIconsFromFolder(String path) {
		
		File folder = new File(path);
		File[] content = folder.listFiles();
		
		Image[] icons = new Image[content.length];
		
		if (content != null) {
			System.out.println("test");
			for (int i = 0; i < icons.length; i++) {
				File file = content[i];
				System.out.println(file.getName());
				
				BufferedImage image = null;
				try {
					image = ImageIO.read(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				icons[i] = image;
			}
		}
		
		return icons;
	}
	
	
	public static void shakeAnimation(CombatEntity target) {
		Point targetLocation = target.sprite.getLocation();
		
		
		Point destinationOne = new Point(targetLocation.x + Window.scaleInt(4), targetLocation.y + Window.scaleInt(5));
		Point destinationTwo = new Point(targetLocation.x - Window.scaleInt(4), targetLocation.y - Window.scaleInt(5));
		
		Point[] destinations = new Point[6];
		int[] framesToTake = new int[6];
		
		for (int i = 0; i < 5; i++) {
			if (i % 2 == 1) {
				destinations[i] = destinationTwo;
			} else {
				destinations[i] = destinationOne;
			}
			
			framesToTake[i] = 3;
		}
		
		destinations[5] = targetLocation;
		framesToTake[5] = 4;
		
		Animation animation = new Animation(target.sprite, destinations, framesToTake, "easeInOutSine");
		
		addAnimation(animation);
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
		System.out.println("ran");
		createIconsFromFolder("Resources\\Animations\\SlashingAnimation");
	}*/
}
