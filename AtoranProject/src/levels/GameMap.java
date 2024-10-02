package levels;
import javax.imageio.ImageIO;
import javax.swing.*;

import main.Window;
import utilities.AnimationPlayerModule;
import combat.Combat;
import combat.CombatEntity;
import combat.Wave;
import datastore.Datastore;
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
    
    public static void loadLevels() {
    	
    }

    public GameMap() {
    	
		Wave levelOneWaveOne = new Wave(new CombatEntity[] {new Slime(true)});
		Wave levelOneWaveTwo = new Wave(new CombatEntity[] {new Slime(true), new Slime(true)});
		Wave[] wavesOne = {levelOneWaveOne, levelOneWaveTwo};
		
		Wave levelTwoWaveOne = new Wave(new CombatEntity[] {new RedSlime(true), new Slime(true)});
		Wave levelTwoWaveTwo = new Wave(new CombatEntity[] {new RedSlime(true), new Slime(true), new Slime(true)});
		Wave[] wavesTwo = {levelTwoWaveOne, levelTwoWaveTwo};
		
		Wave levelThreeWaveOne = new Wave(new CombatEntity[] {new RedSlime(true), new RedSlime(true)});
		Wave levelThreeWaveTwo = new Wave(new CombatEntity[] {new RedSlime(true), new RedSlime(true), new Slime(true)});
		Wave levelThreeWaveThree = new Wave(new CombatEntity[] {new Bear(true), new RedSlime(true), new Slime(true)});
		Wave[] wavesThree = {levelThreeWaveOne, levelThreeWaveTwo, levelThreeWaveThree};
		
		Wave levelFourWaveOne = new Wave(new CombatEntity[] {new Spider(true), new Slime(true)}); 
		Wave levelFourWaveTwo = new Wave(new CombatEntity[] {new Spider(true), new Spider(true), new RedSlime(true)}); 
		Wave levelFourWaveThree = new Wave(new CombatEntity[] {new Spider(true), new Spider(true), new Spider(true)}); 
		Wave[] wavesFour = {levelFourWaveOne, levelFourWaveTwo, levelFourWaveThree};
		
		Wave levelFiveWaveOne = new Wave(new CombatEntity[] {new Spider(true), new Spider(true)}); 
		Wave levelFiveWaveTwo = new Wave(new CombatEntity[] {new Spider(true), new Spider(true), new Spider(true)}); 
		Wave levelFiveWaveThree = new Wave(new CombatEntity[] {new DralyaDragonForm(true)}); 
		Wave[] wavesFive = {levelFiveWaveOne, levelFiveWaveTwo, levelFiveWaveThree};
		
		Wave levelSixWaveOne = new Wave(new CombatEntity[] {new RedSlime(true), new RedSlime(true)}); 
		Wave levelSixWaveTwo = new Wave(new CombatEntity[] {new Knight(true), new Knight(true)}); 
		Wave levelSixWaveThree = new Wave(new CombatEntity[] {new Knight(true), new Knight(true), new Knight(true)}); 
		Wave[] wavesSix = {levelSixWaveOne, levelSixWaveTwo, levelSixWaveThree};
		
		Wave levelSevenWaveOne = new Wave(new CombatEntity[] {new Knight(true), new Knight(true)}); 
		Wave levelSevenMaveTwo = new Wave(new CombatEntity[] {new Knight(true), new Knight(true), new Knight(true)}); 
		Wave levelSevenWaveThree = new Wave(new CombatEntity[] {new Samoht(true)}); 
		Wave[] wavesSeven = {levelSevenWaveOne, levelSevenMaveTwo, levelSevenWaveThree};
		
		// Defining levels
        levels = new ArrayList<>();
        levels.add(new Level(1, "Village", "Slime", true, wavesOne));  // First level is unlocked by default
        levels.add(new Level(2, "Forest", "Bear", false, wavesTwo)); // Second level is locked
        levels.add(new Level(3, "Cave", "Dragon", false, wavesThree)); // Third level is locked
        levels.add(new Level(4, "Mountains", "Wizard", false, wavesFour)); // Fourth level is locked
        levels.add(new Level(5, "Mountains", "Wizard", false, wavesFive));
        levels.add(new Level(6, "Mountains", "Wizard", false, wavesSix));
        levels.add(new Level(7, "Mountains", "Wizard", false, wavesSeven));
        

        Level levelOne = levels.get(0);
        
        List<Dialogue> dialogues = new ArrayList<>();
        
        dialogues.add(new Dialogue("Hiiii"));
        dialogues.add(new Dialogue("Bye"));
        
        Cutscene levelOneCutscene = new Cutscene(dialogues);
        
        levelOne.setStartingCutscene(levelOneCutscene);
        
        if (Datastore.readData("level2") == "1") {
        	levels.get(2).unlock();
        }
        if (Datastore.readData("level3") == "1") {
        	levels.get(3).unlock();
        }
        if (Datastore.readData("level4") == "1") {
        	levels.get(4).unlock();
        }

        // Setting the current map to this instance
        currentMap = this;
    }
    
    // Method to display the game map UI with level buttons
    public void openGameMap() {
    	// Fetch the game's window instance
        Window window = Window.getWindow();
    	
    	gameMapPane = new JLayeredPane();
    	gameMapPane.setSize(window.getSize());
    	gameMapPane.setLocation(new Point(0, 0));
        
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
        	new Point(140, 540),
        	new Point(550, 575),
        	new Point(840, 615),
        	new Point(1000, 375),
        	new Point(1340, 290),
        	new Point(1540, 605),
        	new Point(1640, 885)
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
            
            System.out.println(level.isUnlocked());
            
            gameMapPane.add(levelButton, JLayeredPane.PALETTE_LAYER);
        }

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
        Combat combat = new Combat(level);
        
        // After combat is finished, mark the level as completed and unlock the next level
        if (combat.currentLevel.isCompleted()) {  // Assuming Combat class has a method to check if it's completed
            level.setCompleted(true);  // Assuming Level class has a method to set completion status
            int nextLevelIndex = level.getLevelNumber();
            if (nextLevelIndex < levels.size()) {
                Level nextLevel = levels.get(nextLevelIndex);
                nextLevel.unlock();
            }
            updateMap();  // Update the game map UI
        }
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
