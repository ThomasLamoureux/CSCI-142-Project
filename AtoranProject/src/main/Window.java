package main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import combat.CombatInterface;


public class Window extends JFrame {
	private static final long serialVersionUID = 1L; // Auto generated, idk what this is.
	private static double windowScale = 1.0;
	private final static int xWindowDefaultSize = 1920;
	private final static int yWindowDefaultSize = 1080;
	
	private static Window MainWindow;
	
	// Child of JFrame
	public Window() {
		
	}
	
	// Returns the scale of the window compared to the default scale
	public static double getWindowScale() {
		return windowScale;
	}
	// Returns an integer scalled in reference to the Window size
	public static int scaleInt(int number) {
		return (int)((double)number * windowScale);
	}
	// Scales an image in reference to window size
	public static Image scaleImage(int x, int y, Image image) {
		return image.getScaledInstance((int)(x * windowScale), (int)(y * windowScale), Image.SCALE_SMOOTH);
	}
	// Scales without reference to window size
	public static Image scaleImage(int x, int y, Image image, boolean dontScale) {
		return image.getScaledInstance(x, y, Image.SCALE_SMOOTH);
	}
	// Scales a point with reference to window size
	public static Point scalePoint(Point point) {
		return new Point((int)(point.x * windowScale), (int)(point.y * windowScale));
	}
	// Scales a component in reference to window sizes
	public static Component scaleComponent(Component component) {
		component.setSize((int)((double)component.getSize().width * windowScale), (int)((double)component.getSize().height * windowScale));
		component.setLocation((int)((double)component.getLocation().x * windowScale), (int)((double)component.getLocation().y * windowScale));
		
		return component;
	}

	// Creates the window
	private static Window createWindow() {
		MainWindow = new Window();
		double screenScale = (double)java.awt.Toolkit.getDefaultToolkit().getScreenResolution() / 96.0; // Gets the screen res

		int xWidth = (int)((double)GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth() / screenScale); // X size is used for scaling, y is ignored

		if (xWindowDefaultSize != xWidth) {
			windowScale = ((double)xWidth / (double)xWindowDefaultSize);
			
		}
		
		MainWindow.setSize((int)(windowScale * (double)xWindowDefaultSize), (int)(windowScale * (double)yWindowDefaultSize));
		MainWindow.setSize(1920, 1080);
		MainWindow.setTitle("Atoran");
		MainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
		MainWindow.setUndecorated(true); // No bar
		return MainWindow;
	}
	// Gets the window or creates it if null
	public static Window getWindow() {
		if (MainWindow == null) {
			createWindow();
		}
		return MainWindow;
	}
	// Removes all components from the frame
	public void clearFrame() {
		this.getContentPane().removeAll();
	}
	// Refreshes the frame
	public void refresh() {
		this.repaint();
		this.validate();
	}
 
}
