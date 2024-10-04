package levels;

import combat.Combat;
import combat.Wave;
import cutscenes.Cutscene;
import datastore.Datastore;
import main.Window;

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
    private String musicPath;
    private String imagePath;
    

    // Constructor for the Level class
    public Level(int levelNumber, String background, String music, boolean isUnlocked, Wave[] waves) {
    	// Инициализация полей класса - Initializing class fields
        this.levelNumber = levelNumber;
        this.imagePath = background;
        this.musicPath = music;
        this.isUnlocked = isUnlocked;
        this.isCompleted = false; // Изначально уровень не пройден - initially level is not completed
        this.waves = waves;
    }

    // Геттеры и сеттеры - Getters and Setters 
    public int getLevelNumber() {
        return levelNumber;
    }
    
    
    public String getBackground() {
    	return imagePath;
    }
    
    
    public String getMusic() {
    	return musicPath;
    }
    
    
    public void play() {
    	if (this.startingCutscene == null) {
    		GameMap.currentMap.startLevel(this);
    	} else {
    		if (this.isCompleted == true) {
    			GameMap.currentMap.startLevel(this);
    		} else {
    			this.startingCutscene.start(this);
    		}
    	}
    }
    
    public void playStartingCutscene() {
    	System.out.println("started");
    	if (this.startingCutscene != null) {
    		System.out.println("computed");
    		this.startingCutscene.start(this);
    		
    	}
    }
    
    public boolean playEndingCutscene() {
    	if (this.endingCutscene != null) {
    		this.endingCutscene.start(null);
    		return true;
    	}
    	return false;
    }
    
    public void setStartingCutscene(Cutscene cutscene) {
    	this.startingCutscene = cutscene;
    }
    
    public void setEndingCutscene(Cutscene cutscene) {
    	this.endingCutscene = cutscene;
    }

    public String getLocation() {
        return location;
    }

    public String getEnemy() {
        return enemy;
    }

    public boolean isUnlocked() {
        return isUnlocked;
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
            if (isCompleted == false) {
            	setCompleted(true);
                Datastore.updateDataString("level" + this.levelNumber, "1");
            }
        } else {
            System.out.println("Level " + levelNumber + " is locked.");
        }
    }
}
