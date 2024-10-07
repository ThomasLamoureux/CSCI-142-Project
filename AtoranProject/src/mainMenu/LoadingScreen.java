package mainMenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import engine.Engine;
import levels.GameMap;
import main.Window;

public class LoadingScreen {
	// Loading screen
	public static void load() {
	    Window window = Window.getWindow();

	    window.setDefaultCloseOperation(Window.EXIT_ON_CLOSE);
	    window.setLayout(null);
	    window.getContentPane().setBackground(Color.black);


	    JLabel loadingLabel = new JLabel("Loading...", JLabel.CENTER);
	    loadingLabel.setLocation(new Point(0, 600));
	    loadingLabel.setSize(new Dimension(1920, 100));
	    loadingLabel.setFont(new Font("Algerian", Font.ITALIC, Window.scaleInt(100)));
	    loadingLabel.setForeground(Color.white);
	    Window.scaleComponent(loadingLabel);
	    
	    window.add(loadingLabel);
	    
	    window.setVisible(true);
	    
	    new GameMap(); // Loads the game map
	    Menu.loadIntroScreen(); // Loads logo intro
	    Menu.start(); // Runs menu
	    
	    Engine.toggleFps(true); // Turns on FPS system
	}
}
