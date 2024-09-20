package levels;
import javax.swing.*;

import main.Window;

import combat.Combat;
import combat.CombatEntity;
import combat.Wave;
import datastore.Datastore;
import combat.EntitiesAndMoves.SlimeEntity;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
	public static GameMap currentMap;
    private List<Level> levels;

    public GameMap() {
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
        
        // Loop through all levels and create a button for each
        for (Level level : levels) {
            JButton levelButton = new JButton("Level " + level.getLevelNumber());
            // Enable the button if the level is unlocked
            levelButton.setEnabled(level.isUnlocked());
            // Add an action listener to the button that will start the level
            levelButton.addActionListener(new LevelButtonActionListener(level));
            window.add(levelButton);
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
                // Логика перехода на уровень
                startLevel(level);

                // Если уровень завершен, разблокировать следующий
                /*if (level.isCompleted() && level.getLevelNumber() < levels.size()) {
                    Level nextLevel = levels.get(level.getLevelNumber());
                    nextLevel.unlock();
                    updateMap();
                }*/
                // I commented this out because whenever you tried to replay a level it would break and this was the cause
            } else {
                JOptionPane.showMessageDialog(null, "Этот уровень заблокирован!");
            }
        }
    }

    private void startLevel(Level level) {
        // Вызов класса с логикой боя
    	
        System.out.println("Starting Level " + level.getLevelNumber() + " in location: " + level.getLocation() + " against " + level.getEnemy());
        // Здесь добавить вызов класса combat
    	new Combat(level);
        // После завершения боя установить уровень как пройденный
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
