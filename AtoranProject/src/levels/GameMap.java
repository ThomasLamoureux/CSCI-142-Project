package levels;
import javax.swing.*;

import combat.Combat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameMap extends JFrame {
    private List<Level> levels;

    public GameMap() {
        levels = new ArrayList<>();
        levels.add(new Level(1, "Village", "Slime", true));  // Первый уровень разблокирован по умолчанию
        levels.add(new Level(2, "Forest", "Bear", false));
        levels.add(new Level(3, "Cave", "Dragon", false));
        levels.add(new Level(4, "Mountains", "Wizard", false));

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
        // После завершения боя установить уровень как пройденный
        level.setCompleted(true);
    }

    private void updateMap() {
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

    public static void main(String[] args) {
        new GameMap();
    }
}
