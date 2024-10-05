package mainMenu;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import levels.GameMap;
import animations.Animation;
import animations.Keyframe;
import combat.Combat;
import animations.Animation.ResizeAnimation;
import cutscenes.CreatedCutscenes;
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
	private static JButton creditsButton;
	private static int introIndex;
	private static boolean creditsOpen = false;
	private static JLabel creditsLabel;
	
	public static void loadIntroScreen() {
		introIndex = AnimationsPreloader.loadImages("Resources/Animations/SigmaStudiosIntro", new Dimension(1920, 1080), false);
	}
	
	public static void introScreen() {
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
		
		Combat.combatMusic = new GameSound("Resources/Sounds/MenuSoundtrack.wav");
		//menuMusic.setLooped(true);
		Runnable removeLogoAnimation = () -> {
			animationLabel.setVisible(false);
		};
		
		Runnable playMenuMusic = () -> {
			Combat.combatMusic.playLooped();
		};
		
		Runnable setPlayButtonVisible = () -> {
			playButton.setVisible(true);
			creditsButton.setVisible(true);
		};
		
		ArrayList<Animation> animationsList = new ArrayList<>();
		animationsList.add(logoAnimation);
		animationsList.add(titleResize);
		animationsList.add(titleMove);
		
		CombinedAnimation finalAnimation = new CombinedAnimation(880, animationsList, new int[] {120, 740, 740});
		finalAnimation.keyframes[560] = new Keyframe(removeLogoAnimation);
		finalAnimation.keyframes[530] = new Keyframe(playMenuMusic);
		finalAnimation.keyframes[879] = new Keyframe(setPlayButtonVisible);

		AnimationPlayerModule.addAnimation(finalAnimation);
	}
	
	public static void openMenu() {
        // Creating a window
        Window window = Window.getWindow();
        window.clearFrame();
        
		pane = new JLayeredPane();
		pane.setSize(new Dimension(1920, 1080));
		pane.setLayout(null);
		pane.setLocation(new Point(0, 0));
		Window.scaleComponent(pane);

        // Creating a title for the Main Menu
        titleLabel = new JLabel("ATORAN", JLabel.CENTER);
        titleLabel.setLocation(new Point(0, 450));
        titleLabel.setSize(new Dimension(1920, 350));
		titleLabel.setFont(new Font("Algerian", Font.ROMAN_BASELINE, Window.scaleInt(350)));
		titleLabel.setForeground(Color.white);

		Window.scaleComponent(titleLabel);
		
		pane.add(titleLabel, JLayeredPane.PALETTE_LAYER);
		
		window.add(pane);

        // Create and add the "Play" button
        playButton = new JButton("Play");
        playButton.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(80)));  
        playButton.setForeground(Color.white);
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
				GameMap.currentMap.openGameMap();
				if (GameMap.getLevels().get(0).isCompleted() == false) {
					CreatedCutscenes.introCutscene().start(null);
				}
            }
        });
        
        creditsButton = new JButton("Credits");
        creditsButton.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(80))); 
        creditsButton.setForeground(Color.white);
        creditsButton.setLocation(new Point(760, 750));
        creditsButton.setSize(new Dimension(400, 100));
        creditsButton.setBorderPainted(false); 
        creditsButton.setContentAreaFilled(false); 
        creditsButton.setFocusPainted(false); 
        creditsButton.setOpaque(false);
        creditsButton.setVisible(false);
        
        creditsLabel = new JLabel("<html>Bridget Wexler<br></br>Luke Alley<br></br>Temirlan Stamakunov<br></br>Thomas Lamoureux</html>", JLabel.CENTER);
        creditsLabel.setLocation(new Point(500, 300));
        creditsLabel.setSize(new Dimension(920, 500));
        creditsLabel.setFont(new Font("Algerian", Font.ROMAN_BASELINE, Window.scaleInt(60)));
        creditsLabel.setForeground(Color.white);
        creditsLabel.setVisible(false);
        Window.scaleComponent(creditsLabel);
 
        creditsButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent event) {
				if (creditsOpen == false) {
					creditsOpen = true;
					
					creditsLabel.setVisible(true);
					playButton.setVisible(false);
					creditsButton.setText("Menu");
					titleLabel.setText("SIGMA STUDIOS");
				} else {
					titleLabel.setText("ATORAN");
					creditsLabel.setVisible(false);
					playButton.setVisible(true);
					creditsButton.setText("Credits");
					creditsOpen = false;
				}
            }
        });
        
        Window.scaleComponent(creditsButton);
        
        pane.add(creditsLabel, JLayeredPane.PALETTE_LAYER);
        pane.add(creditsButton, JLayeredPane.PALETTE_LAYER);
        pane.add(playButton, JLayeredPane.PALETTE_LAYER);
    }

    public static void start() {
        // Creating a window
    	openMenu();
    	introScreen();
    	Engine.toggleFps(true);
    }
}

