package levels;
import javax.imageio.ImageIO;
import javax.swing.*;

import animations.Animation;
import animations.Keyframe;
import main.Window;
import utilities.AnimationPlayerModule;
import utilities.AnimationsPreloader;
import utilities.SoundPlayerModule.GameSound;
import combat.Combat;
import combat.CombatEntity;
import combat.Wave;
import datastore.Datastore;
import cutscenes.CreatedCutscenes;
import cutscenes.Cutscene;
import cutscenes.Dialogue;
import combatEntities.Bear;
import combatEntities.DralyaDragonForm;
import combatEntities.Knight;
import combatEntities.RedSlime;
import combatEntities.Samoht;
import combatEntities.Slime;
import combatEntities.Spider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
	public static GameMap currentMap;
    private List<Level> levels;
    public JLayeredPane gameMapPane;
    
    public void loadLevels() {
		Wave levelOneWaveOne = new Wave(new CombatEntity[] {new Slime(true)});
		Wave levelOneWaveTwo = new Wave(new CombatEntity[] {new Slime(true), new Slime(true)});
		Wave[] wavesOne = {levelOneWaveOne, levelOneWaveTwo};
		
		Wave levelTwoWaveOne = new Wave(new CombatEntity[] {new RedSlime(true), new Slime(true)});
		Wave levelTwoWaveTwo = new Wave(new CombatEntity[] {new RedSlime(true), new Slime(true), new Slime(true)});
		Wave[] wavesTwo = {levelTwoWaveOne, levelTwoWaveTwo};
		
		Wave levelThreeWaveOne = new Wave(new CombatEntity[] {new RedSlime(true), new RedSlime(true)});
		Wave levelThreeWaveTwo = new Wave(new CombatEntity[] {new RedSlime(true), new RedSlime(true), new Slime(true)});
		Wave levelThreeWaveThree = new Wave(new CombatEntity[] {new Bear(true), new RedSlime(true), new Slime(true)}, CreatedCutscenes.bearCutscene());
		Wave[] wavesThree = {levelThreeWaveOne, levelThreeWaveTwo, levelThreeWaveThree};
		
		Wave levelFourWaveOne = new Wave(new CombatEntity[] {new Spider(true), new Slime(true)}); 
		Wave levelFourWaveTwo = new Wave(new CombatEntity[] {new Spider(true), new Spider(true), new RedSlime(true)}); 
		Wave levelFourWaveThree = new Wave(new CombatEntity[] {new Spider(true), new Spider(true), new Spider(true)}); 
		Wave[] wavesFour = {levelFourWaveOne, levelFourWaveTwo, levelFourWaveThree};
		
		Wave levelFiveWaveOne = new Wave(new CombatEntity[] {new Spider(true), new Spider(true)}); 
		Wave levelFiveWaveTwo = new Wave(new CombatEntity[] {new Spider(true), new Spider(true)}); 
		Wave levelFiveWaveThree = new Wave(new CombatEntity[] {new DralyaDragonForm(true)}, CreatedCutscenes.dragonIntroCutscene()); 
		Wave[] wavesFive = {levelFiveWaveOne, levelFiveWaveTwo, levelFiveWaveThree};
		
		Wave levelSixWaveOne = new Wave(new CombatEntity[] {new RedSlime(true), new RedSlime(true)}); 
		Wave levelSixWaveTwo = new Wave(new CombatEntity[] {new Knight(true), new Knight(true)}); 
		Wave levelSixWaveThree = new Wave(new CombatEntity[] {new Knight(true), new Knight(true), new Knight(true)}); 
		Wave[] wavesSix = {levelSixWaveOne, levelSixWaveTwo, levelSixWaveThree};
		
		Wave levelSevenWaveOne = new Wave(new CombatEntity[] {new Knight(true), new Knight(true)}); 
		Wave levelSevenWaveTwo = new Wave(new CombatEntity[] {new Knight(true), new Knight(true), new Knight(true)}); 
		Wave levelSevenWaveThree = new Wave(new CombatEntity[] {new Samoht(true)}, CreatedCutscenes.samohtIntro()); 
		Wave[] wavesSeven = {levelSevenWaveOne, levelSevenWaveTwo, levelSevenWaveThree};
		
		// Defining levels
        levels = new ArrayList<>();
        levels.add(new Level(1, "Resources/Images/VillageBackground.png", "Resources/Sounds/ForestMusicBackground.wav", true, wavesOne));  // First level is unlocked by default
        levels.get(0).setEndingCutscene(CreatedCutscenes.levelOneOutro());
        levels.get(0).setStartingCutscene(CreatedCutscenes.levelOneIntro());
        levels.add(new Level(2, "Resources/Images/TemporaryForestBackgroundCropped.png", "Resources/Sounds/ForestMusicBackground.wav", false, wavesTwo)); // Second level is locked
        levels.add(new Level(3, "Resources/Images/TemporaryForestBackgroundCropped.png", "Resources/Sounds/ForestMusicBackground.wav", false, wavesThree)); // Third level is locked
        levels.add(new Level(4, "Resources/Images/CaveBackground.png", "Resources/Sounds/CaveMusic.wav", false, wavesFour)); // Fourth level is locked
        levels.add(new Level(5, "Resources/Images/CaveBackground.png", "Resources/Sounds/CaveMusic.wav", false, wavesFive));
        levels.get(4).setEndingCutscene(CreatedCutscenes.dragonOutroCutscene());
        levels.add(new Level(6, "Resources/Images/MountainBackground.png", "Resources/Sounds/MountainMusicLower.wav", false, wavesSix));
        levels.add(new Level(7, "Resources/Images/MountainBackground.png", "Resources/Sounds/FinalBossMusic.wav", false, wavesSeven));
        levels.get(6).setEndingCutscene(CreatedCutscenes.ending());
    }

    public GameMap() {
        loadLevels();

        Level levelOne = levels.get(0);
        levelOne.setStartingCutscene(CreatedCutscenes.levelOneIntro());
        
        if (Datastore.readData("level1").equals("1")) {
        	System.out.println("Level 2 unlocked");
        	levels.get(0).setCompleted(true);
        	levels.get(1).unlock();
        }
        if (Datastore.readData("level2").equals("1")) {
        	levels.get(1).setCompleted(true);
        	levels.get(2).unlock();
        }
        if (Datastore.readData("level3").equals("1")) {
        	levels.get(2).setCompleted(true);
        	levels.get(3).unlock();
        }
        if (Datastore.readData("level4").equals("1")) {
        	levels.get(3).setCompleted(true);
        	levels.get(4).unlock();
        }
        if (Datastore.readData("level5").equals("1")) {
        	levels.get(4).setCompleted(true);
        	levels.get(5).unlock();
        }
        if (Datastore.readData("level6").equals("1")) {
        	levels.get(5).setCompleted(true);
        	levels.get(6).unlock();
        }
        if (Datastore.readData("level6").equals("7")) {
        	levels.get(6).setCompleted(true);
        }

        // Setting the current map to this instance
        currentMap = this;
    }
    
    // Method to display the game map UI with level buttons
    public void openGameMap() {
    	// Fetch the game's window instance
        Window window = Window.getWindow();
    	
    	gameMapPane = new JLayeredPane();
    	gameMapPane.setSize(new Dimension(1920, 1080));
    	gameMapPane.setLocation(new Point(0, 0));
    	Window.scaleComponent(gameMapPane);
        
        window.add(gameMapPane);
        
        // Loop through all levels and create a button for each
        
        JLabel background = new JLabel();
        background.setSize(new Dimension(1920, 1080));
        background.setLocation(new Point(0, 0));
        
        Window.scaleComponent(background);
        
		File targetFile = new File("Resources/Images/MapBackground.png");
		Image image = null;
		try {
			image = ImageIO.read(targetFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		image = Window.scaleImage(1920, 1080, image);
		
		ImageIcon icon = new ImageIcon(image);
		background.setIcon(icon);
		
		gameMapPane.add(background, JLayeredPane.DEFAULT_LAYER);
        
        
        Point[] buttonLocations = {
        	new Point(155, 593),
        	new Point(565, 680),
        	new Point(814, 553),
        	new Point(1112, 551),
        	new Point(1275, 604),
        	new Point(1446, 507),
        	new Point(1654, 388)
        };

        for (int i = 0; i < levels.size(); i++) {
        	Level level = levels.get(i);
        	
            JButton levelButton = new JButton("Level " + level.getLevelNumber());
            // Enable the button if the level is unlocked
            levelButton.setEnabled(level.isUnlocked());
            // Add an action listener to the button that will start the level
            levelButton.addActionListener(new LevelButtonActionListener(level));
            levelButton.setLocation(buttonLocations[i]);
            levelButton.setSize(new Dimension(100, 50));
            Window.scaleComponent(levelButton);
            
            System.out.println(level.isUnlocked());
            
            gameMapPane.add(levelButton, JLayeredPane.PALETTE_LAYER);
        }
        
        JButton easterEgg = new JButton();
        easterEgg.setLocation(new Point(0, 0));
        easterEgg.setSize(new Dimension(5, 5));
        easterEgg.setOpaque(false);
        easterEgg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.out.println("THE ONE PIECE IS REAL");
            	GameSound sound = new GameSound("Resources/Sounds/ONEPIECE.wav");
            	
            	JLabel label = new JLabel("BIG BACK BIG BACK BIG BACK");
            	label.setSize(new Dimension(1920, 1080));
            	label.setLocation(new Point(0, 0));
            	label.setOpaque(true);
            	label.setBackground(Color.black);
            	Window.scaleComponent(label);
            	
            	File targetFile = new File("Resources/Images/THEONEPIECEISREAL.png");
        		Image image = null;
        		try {
        			image = ImageIO.read(targetFile);
        		} catch (IOException err) {
        			// TODO Auto-generated catch block
        			err.printStackTrace();
        		}
        		
        		image = Window.scaleImage(1920, 1080, image);
        		
        		ImageIcon icon = new ImageIcon(image);
        		label.setIcon(icon);
            	
            	Animation easterAnimation = new Animation(701, "linear");
            	Runnable addEasteregg = () -> {
            		System.out.println("HHHH");
            		gameMapPane.add(label, JLayeredPane.DRAG_LAYER);
            		sound.play();
            	};
            	
            	Runnable removeEasteregg = () -> {
            		gameMapPane.remove(label);
            	};
            	
            	easterAnimation.keyframes[0] = new Keyframe(addEasteregg);
            	easterAnimation.keyframes[700] = new Keyframe(removeEasteregg);
            	
            	AnimationPlayerModule.addAnimation(easterAnimation);
            }
        });
        Window.scaleComponent(easterEgg);
        gameMapPane.add(easterEgg, JLayeredPane.PALETTE_LAYER);

        // Refresh the window
        window.setVisible(true);
    }

    // Inner class for handling what happens when a level button is clicked
    private class LevelButtonActionListener implements ActionListener {
        private Level level;

        // Constructor that takes in a level object
        public LevelButtonActionListener(Level level) {
            this.level = level;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (level.isUnlocked()) {
                level.play();
            } else {
                JOptionPane.showMessageDialog(null, "This level is locked!");
            }
        }
    }

    public void startLevel(Level level) {
        System.out.println("Starting Level " + level.getLevelNumber() + " in location: " + level.getLocation() + " against " + level.getEnemy());
        AnimationsPreloader.clearImages();
        
        for (Wave wave : level.getWaves()) {
        	for (CombatEntity entity : wave.enemies) {
            	entity.loadAnimations();
            }
        }
        
        new Combat(level);
    }

    // Method to update the game map UI when a level is completed
    public void updateMap() {
        Window.getWindow().clearFrame();; // Удаляем все компоненты
        for (Level level : levels) {
            JButton levelButton = new JButton("Level " + level.getLevelNumber());
            levelButton.setEnabled(level.isUnlocked());
            levelButton.addActionListener(new LevelButtonActionListener(level));
            Window.getWindow().add(levelButton);
        }
        Window.getWindow().refresh();
    }
    
    // Method to get the levels of the current map 
    public static List<Level> getLevels() {
    	return currentMap.levels;
    }
    
}
