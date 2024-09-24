package utilities;

import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import animations.Animation;

public class AnimationsPreloader {
	private static ArrayList<ImageIcon[]> imageList = new ArrayList<ImageIcon[]>();
	
	private static int generateUniqueId() {
		return imageList.size();
	}
	
	
	public static ImageIcon[] getIconArray(int index) {
		return imageList.get(index);
	}
	
	
	public static int loadImages(String filePath, Dimension size, boolean flip) {
		int id = generateUniqueId();

		ImageIcon[] images = AnimationPlayerModule.createIconsFromFolder(filePath, size, flip);
		
		imageList.add(images);
		
		return id;
	}
}
