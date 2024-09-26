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
	public static void main(String[] args) {
	    // Creating a window
	    Window window = Window.getWindow();

	    // Properties for the window
	    window.setDefaultCloseOperation(Window.EXIT_ON_CLOSE);
	    window.setLayout(null);
	    window.getContentPane().setBackground(Color.black);

	    // Creating a title for the Main Menu
	    JLabel loadingLabel = new JLabel("Loading...", JLabel.CENTER);
	    loadingLabel.setLocation(new Point(0, 600));
	    loadingLabel.setSize(new Dimension(1920, 100));
	    loadingLabel.setFont(new Font("Algerian", Font.ITALIC, Window.scaleInt(100)));
	    loadingLabel.setForeground(Color.white);
	    
	    window.add(loadingLabel);
	    
	    window.setVisible(true);
	    
	    new GameMap();
	    Menu.start();
	    
	    Engine.toggleFps(true);
	}
}
