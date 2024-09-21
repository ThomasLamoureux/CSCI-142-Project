package mainMenu;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import levels.GameMap;
import animations.Animation;
import animations.Keyframe;
import animations.Animation.ResizeAnimation;
import engine.Engine;
import animations.Animation.CombinedAnimation;
import animations.Animation.FadeAnimation;
import animations.Animation.GraphicAnimation;
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
	private static JLabel titleLabel;
	private static JButton playButton;
	private static JLayeredPane pane;
	
	public static void introScreen() {
		Window window = Window.getWindow();
		window.setDefaultCloseOperation(Window.EXIT_ON_CLOSE);
		
		JLabel fadeLabel = new JLabel();
		fadeLabel.setSize(window.getSize());
		fadeLabel.setBackground(Color.black);
		fadeLabel.setLocation(new Point(0, 0));
		fadeLabel.setOpaque(true);
		
		JLabel animationLabel = new JLabel();
		animationLabel.setSize(window.getSize());
		animationLabel.setLocation(new Point(0, 0));
		animationLabel.setOpaque(true);
		
		pane.add(animationLabel, JLayeredPane.MODAL_LAYER);
		pane.add(fadeLabel, JLayeredPane.POPUP_LAYER);
		
		//Dimension targetSize = new Dimension((int)((double)titleLabel.getWidth()/2.0), (int)((double)titleLabel.getHeight()/2.0));
		
		ResizeAnimation titleResize = new ResizeAnimation(titleLabel, 40, "linear", titleLabel.getSize(), "middle", Window.scaleInt(-150));
		MovementAnimation titleMove = new MovementAnimation(titleLabel, 40, "linear", new Point(0, 100), null);		
		GraphicAnimation logoAnimation = new GraphicAnimation(animationLabel, 85, "Resources/Animations/SigmaStudiosIntro", 0, 0, false);
		FadeAnimation fadingAnimation = new FadeAnimation(fadeLabel, 120, "linear", 255, 0);
		FadeAnimation fadingLogoAnimation = new FadeAnimation(titleLabel, 120, "easeOutQuart", 0, 255);
		
		Runnable removeLogoAnimation = () -> {
			animationLabel.setVisible(false);
			System.out.println("hi");
		};
		logoAnimation.keyframes[84] = new Keyframe(removeLogoAnimation);
		
		Runnable removeFade = () -> {
			fadeLabel.setVisible(false);
			System.out.println("hi");
		};
		fadingAnimation.keyframes[119] = new Keyframe(removeFade);
		
		ArrayList<Animation> animationsList = new ArrayList<>();
		animationsList.add(logoAnimation);
		animationsList.add(fadingAnimation);
		animationsList.add(fadingLogoAnimation);
		animationsList.add(titleResize);
		animationsList.add(titleMove);
		
		CombinedAnimation finalAnimation = new CombinedAnimation(340, animationsList, new int[] {5, 120, 120, 300, 300});
		
		window.setLayout(null);
		
		window.setVisible(true);
		
		Engine.toggleFps(true);
		AnimationPlayerModule.addAnimation(finalAnimation);
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
            window.setLayout(null);
            //window.setSize(400, 300);
    		
    		pane = new JLayeredPane();
    		pane.setSize(window.getSize());
    		pane.setLayout(null);
    		pane.setLocation(new Point(0, 0));
    		

            // Use a GridLayout with 3 rows and 1 column
            JPanel panel = new JPanel(new GridLayout(4, 1));

            // Creating a title for the Main Menu
            titleLabel = new JLabel("ATORAN", JLabel.CENTER);
            
            panel.setLayout(null);
            
            titleLabel.setLocation(new Point(0, 450));
            titleLabel.setSize(new Dimension(1920, 300));
    		titleLabel.setFont(new Font("Algerian", Font.ROMAN_BASELINE, Window.scaleInt(300)));
    		//titleLabel.setForeground(new Color(0, 0, 0, 0));
    		//titleLabel.setOpaque(true);
    		//titleLabel.setBackground(Color.blue);
    		
    		Window.scaleComponent(titleLabel);
    		
    		pane.add(titleLabel, JLayeredPane.PALETTE_LAYER);
    		
    		window.add(pane);
           

            /*titleLabel.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(125)));
            panel.add(titleLabel);*/


            // Adding space between the labels and title
           // panel.add(new JLabel());

            // Create and add the "Play" button
            playButton = new JButton("Play");
            playButton.setFont(new Font("Calibri", Font.PLAIN, 30));  
            
            playButton.setLocation(new Point(500, 500));
            playButton.setSize(new Dimension(50, 50));
            
            
            playButton.addActionListener(new ActionListener() {
    			@Override
                public void actionPerformed(ActionEvent event) {
    				window.clearFrame();
    				window.refresh();
    				new GameMap();
                }
            });
            
            introScreen();
            
            window.setVisible(true);
            
            Engine.toggleFps(true);
        }
    }
}

