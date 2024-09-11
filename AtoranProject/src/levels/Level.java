package levels;

import java.util.List;

import combat.Wave;

public class Level {
    private int levelNumber;
    private String location;
    private String enemy;
    private Wave[] waves;
    private boolean isUnlocked;
    private boolean isCompleted;

    public Level(int levelNumber, String location, String enemy, boolean isUnlocked, Wave[] waves) {
        this.levelNumber = levelNumber;
        this.location = location;
        this.enemy = enemy;
        this.isUnlocked = isUnlocked;
        this.isCompleted = false; // Изначально уровень не пройден
        this.waves = waves;
    }

    // Геттеры и сеттеры
    public int getLevelNumber() {
        return levelNumber;
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

    // Метод для прохождения уровня
    public void completeLevel() {
        if (isUnlocked) {
            System.out.println("Level " + levelNumber + " completed!");
            this.isCompleted = true;
        } else {
            System.out.println("Level " + levelNumber + " is locked.");
        }
    }
}
