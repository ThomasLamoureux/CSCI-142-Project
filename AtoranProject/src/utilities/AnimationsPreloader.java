package utilities;

import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import animations.Animation;

public class AnimationsPreloader {
	private static ArrayList<ImageIcon[]> imageList = new ArrayList<ImageIcon[]>();
	
	// Returns the next index
	private static int generateUniqueId() {
		return imageList.size();
	}
	
	// Removes elements from list
	public static void clearImages() {
		imageList = new ArrayList<ImageIcon[]>();
	}
	
	// Gets the icons 
	public static ImageIcon[] getIconArray(int index) {
		return imageList.get(index);
	}
	
	// Preloads the images and saved them in an array, the index to the images is returned by the function
	public static int loadImages(String filePath, Dimension size, boolean flip) {
		int id = generateUniqueId(); // Gets the id

		ImageIcon[] images = AnimationPlayerModule.createIconsFromFolder(filePath, size, flip); // Creates the icons
		
		imageList.add(images); // Adds
		System.out.println("AnimationsPreloader 30: Loaded asset " + id);
		return id; // Returns the index
	}
}
