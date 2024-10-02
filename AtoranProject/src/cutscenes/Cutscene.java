package cutscenes;

import main.Window;
import javax.swing.*;

import combat.Combat;
import levels.GameMap;
import levels.Level;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Cutscene {
	
	private List<Dialogue> dialogues;
    private int currentDialogueIndex;
    private JLayeredPane layeredPane;
    private JLabel imageLabel;
    private JTextArea textArea;
    private JLabel namecard;
    private JButton nextButton;
    private Level level;

    public Cutscene(List<Dialogue> dialogues) {
        this.dialogues = dialogues;
        this.currentDialogueIndex = 0;
    }

    public void start(Level level) {
    	this.level = level;
    	
        Window window = Window.getWindow();
        //window.clearFrame();
        layeredPane = new JLayeredPane();
        layeredPane.setSize(window.getSize());
        layeredPane.setLocation(new Point(0, 0));
        //layeredPane.setLayout(new GridLayout());
        
        System.out.println(window.getSize());


        // Create and add cutscene components
        imageLabel = new JLabel();
        imageLabel.setBounds(0, 0, window.getWidth(), window.getHeight() - 200);
        layeredPane.add(imageLabel, JLayeredPane.DRAG_LAYER);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBounds(50, window.getHeight() - 190, window.getWidth() - 100, 150);
        textArea.setSize(new Dimension(400, 500));
        layeredPane.add(textArea, JLayeredPane.DRAG_LAYER);

        nextButton = new JButton("Next");
        nextButton.setLocation(new Point(500, 500));
        nextButton.setSize(new Dimension(100, 30));
        //nextButton.setBounds(window.getWidth() - 150, window.getHeight() - 50, 100, 30);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextDialogue();
            }
        });
        layeredPane.add(nextButton, JLayeredPane.DRAG_LAYER);
        layeredPane.setBackground(Color.black);
        layeredPane.setOpaque(true);
        layeredPane.setVisible(true);
        
        Window.scaleComponent(layeredPane);
        
        
        JLabel testLabel = new JLabel("TESTING");
        testLabel.setSize(new Dimension(200, 200));
        testLabel.setLocation(new Point(200, 200));
        window.add(testLabel);

        GameMap.currentMap.gameMapPane.add(layeredPane, JLayeredPane.DRAG_LAYER);
        window.revalidate();
        window.repaint();
        window.refresh();

        System.out.println("Made it to the end : Cutscene line 73");
        
        showCurrentDialogue();
    }

    private void showCurrentDialogue() {
        if (currentDialogueIndex < dialogues.size()) {
            Dialogue dialogue = dialogues.get(currentDialogueIndex);
            textArea.setText(dialogue.getText());
            dialogue.triggerEvent(this);
        } else {
            end();
        }
    }

    private void showNextDialogue() {
        currentDialogueIndex++;
        showCurrentDialogue();
    }

    public void setImage(ImageIcon image) {
        imageLabel.setIcon(image);
    }

    private void end() {
        Window window = Window.getWindow();
        
        GameMap.currentMap.gameMapPane.remove(layeredPane);

        for (Component component : layeredPane.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER)) {
            window.add(component);
        }
        window.refresh();
        if (this.level != null) {
        	GameMap.currentMap.startLevel(this.level);;
        }
    }

}
