package cutscenes;

import main.Window;
import javax.swing.*;
import javax.swing.border.LineBorder;

import combat.Combat;
import combat.CombatInterface;
import levels.GameMap;
import levels.Level;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private boolean removeBlackBackground = false;

    public Cutscene(List<Dialogue> dialogues) {
        this.dialogues = dialogues;
        this.currentDialogueIndex = 0;
    }
    
    public Cutscene(List<Dialogue> dialogues, boolean removeBlackBackground) {
        this.dialogues = dialogues;
        this.currentDialogueIndex = 0;
        this.removeBlackBackground = removeBlackBackground;
    }
    
    private static class TransparentLabel extends JLabel {
		private static final long serialVersionUID = 1L;

		public TransparentLabel() {
            super();
            this.setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Convert the Graphics object to Graphics2D
            Graphics2D g2d = (Graphics2D) g.create();

            // Set the transparency (alpha value between 0.0f and 1.0f)
            float alpha = 0.5f;  // 50% transparency
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Set the background color (you can change this color as needed)
            g2d.setColor(Color.black);

            // Fill a rectangle with the semi-transparent color
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Dispose the Graphics2D object to clean up resources
            g2d.dispose();

            // Call the superclass to ensure the text and other content is painted
            super.paintComponent(g);
        }
    }

    public void start(Level level) {
    	this.level = level;
    	
        Window window = Window.getWindow();
        //window.clearFrame();
        layeredPane = new JLayeredPane();
        layeredPane.setSize(new Dimension(1920, 1080));
        layeredPane.setLocation(new Point(0, 0));
        //layeredPane.setLayout(new GridLayout());
        Window.scaleComponent(layeredPane);

        // Create and add cutscene components
        imageLabel = new JLabel();
        imageLabel.setBounds(0, 0, window.getWidth(), window.getHeight() - 200);
        layeredPane.add(imageLabel, JLayeredPane.DRAG_LAYER);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setLocation(new Point(660, 830));
        textArea.setSize(new Dimension(600, 250));
        textArea.setForeground(Color.white);
        textArea.setBackground(Color.black);
        textArea.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(18)));
        textArea.setBorder(new LineBorder(new Color(50, 50, 50), 4));
        Window.scaleComponent(textArea);
        
        namecard = new JLabel("Atoran");
        namecard.setSize(new Dimension(600, 35));
        namecard.setLocation(new Point(680, 795));
        namecard.setForeground(Color.white);
        namecard.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(32)));
        Window.scaleComponent(namecard);
        
        layeredPane.add(textArea, JLayeredPane.DRAG_LAYER);
        layeredPane.add(namecard, JLayeredPane.DRAG_LAYER);
        

        nextButton = new JButton("Next");
        nextButton.setLocation(new Point(1160, 795));
        nextButton.setForeground(Color.white);
        nextButton.setBackground(Color.black);
        nextButton.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(23)));
        nextButton.setSize(new Dimension(100, 35));
        //nextButton.setBounds(window.getWidth() - 150, window.getHeight() - 50, 100, 30);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextDialogue();
            }
        });
        Window.scaleComponent(nextButton);
        layeredPane.add(nextButton, JLayeredPane.DRAG_LAYER);
        
        if (this.removeBlackBackground == true) {
        	JLabel background = new TransparentLabel();
        	background.setSize(layeredPane.getSize());
        	background.setLocation(new Point(0, 0));
        	layeredPane.add(background, JLayeredPane.POPUP_LAYER);
        } else {
        	layeredPane.setBackground(Color.black);
        	layeredPane.setOpaque(true);
        }

        layeredPane.setVisible(true);
        
        

        if (Combat.currentCombatInstance != null) {
        	CombatInterface.layerOnePane.add(layeredPane, JLayeredPane.DRAG_LAYER);
        	CombatInterface.expandButton.setEnabled(false);
        	CombatInterface.moveMenu.setVisible(false);
        } else {
        	GameMap.currentMap.gameMapPane.add(layeredPane, JLayeredPane.DRAG_LAYER);
        }

        showCurrentDialogue();
    }

    private void showCurrentDialogue() {
        if (currentDialogueIndex < dialogues.size()) {
            Dialogue dialogue = dialogues.get(currentDialogueIndex);
            textArea.setText(dialogue.getText());
            namecard.setText(dialogue.getName());
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
        
        if (Combat.currentCombatInstance != null) {
        	CombatInterface.layerOnePane.remove(layeredPane);
        	CombatInterface.expandButton.setEnabled(true);
        	CombatInterface.moveMenu.setVisible(true);
        	this.currentDialogueIndex = 0;
        	
        	if (Combat.currentCombatInstance.fighting == false) {
    			Window.getWindow().clearFrame();
    			GameMap.currentMap.openGameMap();
    			Combat.currentCombatInstance = null;
        	}
        } else {
        	GameMap.currentMap.gameMapPane.remove(layeredPane);
        }


        /*for (Component component : layeredPane.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER)) {
            window.add(component);
        }*/
        window.refresh();
        if (this.level != null) {
        	GameMap.currentMap.startLevel(this.level);;
        }
    }

}
