package levels;
import javax.swing.*;

import combat.Combat;
import combat.CombatEntity;
import combat.Wave;
import combat.EntitiesAndMoves.SlimeEntity;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameMap extends JFrame {
	public static GameMap currentMap;
    private List<Level> levels;

    public GameMap() {
		CombatEntity[] enemies = {new SlimeEntity(), new SlimeEntity()};
		CombatEntity[] enemiesTwo = {new SlimeEntity(), new SlimeEntity(), new SlimeEntity()};
		Wave wave = new Wave(enemies);
		Wave waveTwo = new Wave(enemiesTwo);
		Wave[] waves = {wave, waveTwo};
		
        levels = new ArrayList<>();
        levels.add(new Level(1, "Village", "Slime", true, waves));  // Первый уровень разблокирован по умолчанию
        levels.add(new Level(2, "Forest", "Bear", false, waves));
        levels.add(new Level(3, "Cave", "Dragon", false, waves));
        levels.add(new Level(4, "Mountains", "Wizard", false, waves));

        setTitle("Game Map");
        setSize(400, 400);
        setLayout(new GridLayout(4, 1)); // Сетка для кнопок уровней
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (Level level : levels) {
            JButton levelButton = new JButton("Level " + level.getLevelNumber());
            levelButton.setEnabled(level.isUnlocked());
            levelButton.addActionListener(new LevelButtonActionListener(level));
            add(levelButton);
        }

        setVisible(true);
        
        currentMap = this;
    }

    private class LevelButtonActionListener implements ActionListener {
        private Level level;

        public LevelButtonActionListener(Level level) {
            this.level = level;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (level.isUnlocked()) {
                // Логика перехода на уровень
                startLevel(level);

                // Если уровень завершен, разблокировать следующий
                if (level.isCompleted() && level.getLevelNumber() < levels.size()) {
                    Level nextLevel = levels.get(level.getLevelNumber());
                    nextLevel.unlock();
                    updateMap();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Этот уровень заблокирован!");
            }
        }
    }

    private void startLevel(Level level) {
        // Вызов класса с логикой боя
    	
        System.out.println("Starting Level " + level.getLevelNumber() + " in location: " + level.getLocation() + " against " + level.getEnemy());
        // Здесь добавить вызов класса combat
    	Combat.createLevelFromInfo(level);
        // После завершения боя установить уровень как пройденный
    }

    public void updateMap() {
        getContentPane().removeAll(); // Удаляем все компоненты
        for (Level level : levels) {
            JButton levelButton = new JButton("Level " + level.getLevelNumber());
            levelButton.setEnabled(level.isUnlocked());
            levelButton.addActionListener(new LevelButtonActionListener(level));
            add(levelButton);
        }
        revalidate();
        repaint();
    }
    
    public static List<Level> getLevels() {
    	return currentMap.levels;
    }

    public static void main(String[] args) {
        new GameMap();
    }
}
