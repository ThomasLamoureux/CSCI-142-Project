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
import utilities.AnimationsPreloader;
import utilities.SoundPlayerModule.GameSound;

public class Menu {
	private static JLabel titleLabel;
	private static JButton playButton;
	private static JLayeredPane pane;
	private static GameSound menuMusic;
	private static int introIndex;
	
	public static void loadIntroScreen() {
		introIndex = AnimationsPreloader.loadImages("Resources/Animations/SigmaStudiosIntro", new Dimension(1920, 1080), false);
	}
	
	public static void introScreen() {
		Window window = Window.getWindow();
		
		titleLabel.setSize(new Dimension(1920, 350));
		
		JLabel animationLabel = new JLabel();
		animationLabel.setSize(new Dimension(1920, 1080));
		animationLabel.setLocation(new Point(0, 0));
		animationLabel.setOpaque(true);
		animationLabel.setBackground(Color.black);
		Window.scaleComponent(animationLabel);
		
		pane.add(animationLabel, JLayeredPane.MODAL_LAYER);
		
		Dimension targetSize = new Dimension(titleLabel.getWidth(), Window.scaleInt(150));
		
		ResizeAnimation titleResize = new ResizeAnimation(titleLabel, 50, "linear", targetSize, "middle", Window.scaleInt(-200));
		MovementAnimation titleMove = new MovementAnimation(titleLabel, 50, "linear", new Point(0, 200), null);
		
		
		GraphicAnimation logoAnimation = new GraphicAnimation(animationLabel, 85 * 4, introIndex, 0, 4);
		
		menuMusic = new GameSound("Resources/Sounds/MenuSoundtrack.wav");
		//menuMusic.setLooped(true);
		Runnable removeLogoAnimation = () -> {
			animationLabel.setVisible(false);
		};
		
		Runnable playMenuMusic = () -> {
			menuMusic.playLooped();
		};
		
		Runnable setPlayButtonVisible = () -> {
			playButton.setVisible(true);
		};
		
		ArrayList<Animation> animationsList = new ArrayList<>();
		animationsList.add(logoAnimation);
		animationsList.add(titleResize);
		animationsList.add(titleMove);
		
		CombinedAnimation finalAnimation = new CombinedAnimation(880, animationsList, new int[] {120, 740, 740});
		finalAnimation.keyframes[560] = new Keyframe(removeLogoAnimation);
		finalAnimation.keyframes[540] = new Keyframe(playMenuMusic);
		finalAnimation.keyframes[879] = new Keyframe(setPlayButtonVisible);

		AnimationPlayerModule.addAnimation(finalAnimation);
	}
	
	public static void openMenu() {
        // Creating a window
        Window window = Window.getWindow();
        window.clearFrame();
        
		pane = new JLayeredPane();
		pane.setSize(window.getSize());
		pane.setLayout(null);
		pane.setLocation(new Point(0, 0));

        // Creating a title for the Main Menu
        titleLabel = new JLabel("ATORAN", JLabel.CENTER);
        titleLabel.setLocation(new Point(0, 450));
        titleLabel.setSize(new Dimension(1920, 350));
		titleLabel.setFont(new Font("Algerian", Font.ROMAN_BASELINE, Window.scaleInt(350)));

		Window.scaleComponent(titleLabel);
		
		pane.add(titleLabel, JLayeredPane.PALETTE_LAYER);
		
		window.add(pane);

        // Create and add the "Play" button
        playButton = new JButton("Play");
        playButton.setFont(new Font("Algerian", Font.PLAIN, 80));  
        playButton.setLocation(new Point(810, 600));
        playButton.setSize(new Dimension(300, 100));
        playButton.setBorderPainted(false); 
        playButton.setContentAreaFilled(false); 
        playButton.setFocusPainted(false); 
        playButton.setOpaque(false);
        playButton.setVisible(false);
        
        Window.scaleComponent(playButton);
        
        playButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent event) {
				window.clearFrame();
				menuMusic.stop();
				GameMap.currentMap.openGameMap();
            }
        });
        
        pane.add(playButton, JLayeredPane.PALETTE_LAYER);
    }

    public static void start() {
        // Creating a window
    	openMenu();
    	introScreen();
    	Engine.toggleFps(true);
    }
}

