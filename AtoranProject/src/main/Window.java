package main;

import javax.swing.JFrame;


public class Window extends JFrame {
	private static final long serialVersionUID = 1L; // Auto generated, idk what this is.
	
	private static Window MainWindow = new Window();

	
	public static Window createWindow() {
		MainWindow = new Window();

		return MainWindow;
	}
	
	public static Window getWindow() {
		return MainWindow;
	}
	
	public Window() {
		
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
