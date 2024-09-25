package levels;
import javax.swing.*;

import main.Window;

import combat.Combat;
import combat.CombatEntity;
import combat.Wave;
import datastore.Datastore;
import combat.EntitiesAndMoves.SlimeEntity;
import cutscenes.Cutscene;
import cutscenes.Dialogue;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
	public static GameMap currentMap;
    private List<Level> levels;
    public JLayeredPane gameMapPane;

    public GameMap() {
    	Window window = Window.getWindow();
    	
    	gameMapPane = new JLayeredPane();
    	gameMapPane.setSize(window.getSize());
    	gameMapPane.setLocation(new Point(0, 0));
    	
    	// Defining enemies for the waves using CombatEntity classes
		CombatEntity[] enemies = {new SlimeEntity(), new SlimeEntity()};
		CombatEntity[] enemiesTwo = {new SlimeEntity(), new SlimeEntity(), new SlimeEntity()};
		CombatEntity[] enemiesThree = {new SlimeEntity()};
		
		// Defining waves
		Wave wave = new Wave(enemies);
		Wave waveTwo = new Wave(enemiesTwo);
		Wave[] waves = {wave, waveTwo}; // First level has two waves
		
		Wave waveThree = new Wave(enemiesThree);
		Wave[] wavesTwo = {waveThree}; // Second level has one wave
		
		// Defining levels
        levels = new ArrayList<>();
        levels.add(new Level(1, "Village", "Slime", true, waves));  // First level is unlocked by default
        levels.add(new Level(2, "Forest", "Bear", false, wavesTwo)); // Second level is locked
        levels.add(new Level(3, "Cave", "Dragon", false, waves)); // Third level is locked
        levels.add(new Level(4, "Mountains", "Wizard", false, waves)); // Fourth level is locked

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
        
        // Opening the game map UI
        this.openGameMap();
    }
    
    // Method to display the game map UI with level buttons
    public void openGameMap() {
    	// Fetch the game's window instance
        Window window = Window.getWindow();
        // 4 columns, 1 row
        window.setLayout(new GridLayout(4, 1)); // Сетка для кнопок уровней
        window.setLayout(null);
        
        window.add(gameMapPane);
        
        // Loop through all levels and create a button for each
        int i = 0;
        
        for (Level level : levels) {
            JButton levelButton = new JButton("Level " + level.getLevelNumber());
            // Enable the button if the level is unlocked
            levelButton.setEnabled(level.isUnlocked());
            // Add an action listener to the button that will start the level
            levelButton.addActionListener(new LevelButtonActionListener(level));
            levelButton.setLocation(new Point(0, i));
            levelButton.setSize(new Dimension(400, 200));
            i += 200;
            
            gameMapPane.add(levelButton, JLayeredPane.DEFAULT_LAYER);
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
                level.playStartingCutscene();
                //startLevel(level);  // Uncomment this line
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
