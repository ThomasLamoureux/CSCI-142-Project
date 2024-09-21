package combat;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import main.Window;

public class Tutorial {
	public int currentStep;
	public JPanel tutorialPanel;
	public JLabel dialogueJLabel;
	public JLabel highlight;
	
	
	public Tutorial() {
		this.currentStep = 0;
		this.tutorialPanel = new JPanel();
		this.dialogueJLabel = new JLabel();
		this.highlight = new JLabel();
		
		tutorialPanel.setSize(Window.getWindow().getSize());
		tutorialPanel.setLocation(new Point(0, 0));
		tutorialPanel.setLayout(null);
		tutorialPanel.setOpaque(false);
		tutorialPanel.setBackground(new Color(0, 0, 0, 125));
		
		CombatInterface.layerOnePane.add(tutorialPanel, JLayeredPane.POPUP_LAYER);
	}
	
	public void nextStep() {
		
		switch(currentStep) {
			case 0:
				
		}
		
	}
}
