package mainMenu;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import levels.GameMap;
import animations.Animation;
import animations.Animation.ResizeAnimation;
import animations.Animation.CombinedAnimation;
import animations.Animation.FadeAnimation;
import animations.Animation.MovementAnimation;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Dimension;

import main.Window;
import utilities.AnimationPlayerModule;

public class Menu {
	
	public static Animation test(JLabel titleLabel) {
		Animation testAni = new ResizeAnimation(titleLabel, 120, "linear", new Dimension(599, 599), "middle", 125);
		Animation testAni2 = new MovementAnimation(titleLabel, 21, "linear", new Point(599, 599));
		Animation testAni3 = new FadeAnimation(titleLabel, 20, "linear", 0, 255);
		
		ArrayList<Animation> animationsTest = new ArrayList<Animation>();
		//animationsTest.add(testAni3);
		animationsTest.add(testAni2);
		animationsTest.add(testAni);
		
		Animation testAni4 = new CombinedAnimation(122, animationsTest, new int[] {0, 0, 0} );
		
		return testAni4;
	}

    public static void main(String[] args) {
        // Creating a window
        Window window = Window.getWindow();

        // Throws an error if window cannot open
        if (window == null) {
            System.out.println("Error: The window could not be created.");
        } else {
            
            // Properties for the window
            window.setDefaultCloseOperation(Window.EXIT_ON_CLOSE);
            //window.setSize(400, 300);

            // Use a GridLayout with 3 rows and 1 column
            JPanel panel = new JPanel(new GridLayout(4, 1));

            // Creating a title for the Main Menu
            JLabel titleLabel = new JLabel("ATORAN", JLabel.CENTER);
            
            panel.setLayout(null);
            
            titleLabel.setLocation(new Point(200, 500));
            titleLabel.setSize(new Dimension(50, 50));
            titleLabel.setBackground(Color.red);
            titleLabel.setOpaque(true);
            // Gives color and font for the Menu
            //titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
            //titleLabel.setForeground(Color.BLUE);  // Customize the color
            //titleLabel.setHorizontalAlignment(JLabel.CENTER);
    		titleLabel.setFont(new Font("Calibri", Font.PLAIN, Window.scaleInt(20)));
    		panel.add(titleLabel);
           

            /*titleLabel.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(125)));
            panel.add(titleLabel);*/


            // Adding space between the labels and title
           // panel.add(new JLabel());

            // Create and add the "Play" button
            JButton playButton = new JButton("Play");
            playButton.setFont(new Font("Calibri", Font.PLAIN, 30));  
            
            playButton.setLocation(new Point(500, 500));
            playButton.setSize(new Dimension(50, 50));
            
            
            playButton.addActionListener(new ActionListener() {
    			@Override
                public void actionPerformed(ActionEvent event) {
    				window.clearFrame();
    				window.refresh();
    				new GameMap();
    				
    	            
    				/*Animation t = test(titleLabel);
    				
    	            AnimationPlayerModule.addAnimation(t);
    	            
    	            engine.Engine.toggleFps(true);*/
                }
            });
            
     
            
            panel.add(playButton);
            

            // Add the panel to the window and set the window visible
            window.add(panel);
            window.setVisible(true);
        }
    }
}

