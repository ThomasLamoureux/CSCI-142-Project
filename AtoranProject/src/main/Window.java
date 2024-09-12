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

import combat.CombatPlayerInteractions;


public class Window extends JFrame {
	private static final long serialVersionUID = 1L; // Auto generated, idk what this is.
	private static double windowScale = 1.0;
	private final static int xWindowDefaultSize = 1920;
	private final static int yWindowDefaultSize = 1080;
	
	private static Window MainWindow;
	
	
	public Window() {
		
	}
	
	
	public static double getWindowScale() {
		return windowScale;
	}
	
	public static int scaleInt(int number) {
		return (int)((double)number * windowScale);
	}
	
	public static Image scaleImage(int x, int y, Image image) {
		return image.getScaledInstance((int)(x * windowScale), (int)(y * windowScale), Image.SCALE_DEFAULT);
	}
	
	public static Point scalePoint(Point point) {
		return new Point((int)(point.x * windowScale), (int)(point.y * windowScale));
	}
	
	public static Component scaleComponent(Component component) {
		
		component.setSize((int)((double)component.getSize().width * windowScale), (int)((double)component.getSize().height * windowScale));
		component.setLocation((int)((double)component.getLocation().x * windowScale), (int)((double)component.getLocation().y * windowScale));
		
		return component;
	}

	
	private static Window createWindow() {
		MainWindow = new Window();
		double screenScale = (double)java.awt.Toolkit.getDefaultToolkit().getScreenResolution() / 96.0;
		System.out.println(screenScale);
		int xWidth = (int)((double)GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth() / screenScale);
		if (xWindowDefaultSize != xWidth) {
			windowScale = ((double)xWidth / (double)xWindowDefaultSize);
			
			System.out.println(windowScale);
		}
		
		MainWindow.setSize((int)(windowScale * (double)xWindowDefaultSize), (int)(windowScale * (double)yWindowDefaultSize));
		MainWindow.setSize(1920, 1080);
		MainWindow.setTitle("Atoran");
		MainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//MainWindow.setUndecorated(true);
		return MainWindow;
	}
	
	public static Window getWindow() {
		if (MainWindow == null) {
			createWindow();
		}
		return MainWindow;
	}
	
	public void clearFrame() {
		this.getContentPane().removeAll();
		this.getContentPane().repaint();
	}
	
	public void refresh() {
		this.repaint();
		this.validate();
	}
 
}
