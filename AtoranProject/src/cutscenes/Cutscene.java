package cutscenes;

import main.Window;
import javax.swing.*;
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
    private JButton nextButton;

    public Cutscene(List<Dialogue> dialogues) {
        this.dialogues = dialogues;
        this.currentDialogueIndex = 0;
    }

    public void start() {
        Window window = Window.getWindow();
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(window.getSize());
        
        System.out.println(window.getSize());

        // Add existing components to the bottom layer
        /*Component[] components = window.getContentPane().getComponents();
        for (Component component : components) {
            layeredPane.add(component, JLayeredPane.DEFAULT_LAYER);
            component.setBounds(0, 0, window.getWidth(), window.getHeight());
        }*/

        // Create and add cutscene components
        imageLabel = new JLabel();
        imageLabel.setBounds(0, 0, window.getWidth(), window.getHeight() - 200);
        layeredPane.add(imageLabel, JLayeredPane.PALETTE_LAYER);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBounds(50, window.getHeight() - 190, window.getWidth() - 100, 150);
        layeredPane.add(textArea, JLayeredPane.PALETTE_LAYER);

        nextButton = new JButton("Next");
        nextButton.setBounds(window.getWidth() - 150, window.getHeight() - 50, 100, 30);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextDialogue();
            }
        });
        layeredPane.add(nextButton, JLayeredPane.PALETTE_LAYER);
        
        Window.scaleComponent(layeredPane);

        //window.setContentPane(layeredPane);
        window.add(layeredPane);
        window.revalidate();
        window.repaint();

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
        window.setContentPane(new JPanel());
        for (Component component : layeredPane.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER)) {
            window.add(component);
        }
        window.revalidate();
        window.repaint();
    }

}
