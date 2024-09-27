package levels;

import combat.Wave;
import cutscenes.Cutscene;
import datastore.Datastore;

public class Level {
	// Поля класса - Class fields
    private int levelNumber;
    private String location;
    private String enemy;
    private Wave[] waves;
    private boolean isUnlocked;
    private boolean isCompleted;
    private Cutscene startingCutscene;
    private Cutscene endingCutscene;
    

    // Constructor for the Level class
    public Level(int levelNumber, String location, String enemy, boolean isUnlocked, Wave[] waves) {
    	// Инициализация полей класса - Initializing class fields
        this.levelNumber = levelNumber;
        this.location = location;
        this.enemy = enemy;
        this.isUnlocked = isUnlocked;
        this.isCompleted = false; // Изначально уровень не пройден - initially level is not completed
        this.waves = waves;
    }

    // Геттеры и сеттеры - Getters and Setters 
    public int getLevelNumber() {
        return levelNumber;
    }
    
    public void playStartingCutscene() {
    	System.out.println("started");
    	if (this.startingCutscene != null) {
    		System.out.println("computed");
    		// this.startingCutscene.start();
    		
    	}
    }
    
    public void playEndingCutscene() {
    	if (this.endingCutscene != null) {
    		// this.endingCutscene.start();
    	}
    }
    
    public void setStartingCutscene(Cutscene cutscene) {
    	this.startingCutscene = cutscene;
    }

    public String getLocation() {
        return location;
    }

    public String getEnemy() {
        return enemy;
    }

    public boolean isUnlocked() {
        return isUnlocked;
       /* Select levels;
	   Datastore.writeData("level2", levels.get(1).isUnlocked() ? "1" : "0");
       Datastore.writeData("level3", levels.get(2).isUnlocked() ? "1" : "0");
       Datastore.writeData("level4", levels.get(3).isUnlocked() ? "1" : "0");  */
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
        
    }

    public void unlock() {
        this.isUnlocked = true;
    }
    
    public Wave[] getWaves() {
    	return this.waves;
    }

    // Метод для прохождения уровня - Method to complete the level
    public void completeLevel() {
        if (isUnlocked) {
            System.out.println("Level " + levelNumber + " completed!");
            this.isCompleted = true;
        } else {
            System.out.println("Level " + levelNumber + " is locked.");
        }
    }
}
