package combat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import main.Window;

public class Tutorial {
	private int currentStep;
	private JPanel tutorialPanel;
	private JLabel dialogueJLabel;
	private JLabel highlight;
	
	
	public Tutorial() {
		this.currentStep = 0;
		this.tutorialPanel = new JPanel();
		this.dialogueJLabel = new JLabel();
		this.highlight = new JLabel();
		
		tutorialPanel.setSize(Window.getWindow().getSize());
		tutorialPanel.setLocation(new Point(0, 0));
		tutorialPanel.setLayout(null);
		tutorialPanel.setOpaque(false);
		Window.scaleComponent(tutorialPanel);
		
		CombatInterface.layerOnePane.add(tutorialPanel, JLayeredPane.POPUP_LAYER);
		
		dialogueJLabel.setSize(new Dimension(500, 350));
		dialogueJLabel.setLocation(new Point((1920-500)/2, 500));
		dialogueJLabel.setForeground(Color.white);
		dialogueJLabel.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(25)));
		dialogueJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dialogueJLabel.setVerticalAlignment(SwingConstants.TOP);
		dialogueJLabel.setBackground(new Color(0, 0, 0, 200));
		dialogueJLabel.setBorder(new LineBorder(new Color(30, 16, 6), 4));
		dialogueJLabel.setOpaque(true);
		Window.scaleComponent(dialogueJLabel);
		
		tutorialPanel.add(dialogueJLabel);
		
		highlight.setOpaque(false);
		highlight.setBorder(new LineBorder(Color.white, 4));
		highlight.setVisible(false);
		
		tutorialPanel.add(highlight);
		
		tutorialPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				nextStep();
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		
		nextStep();
	}
	
	public void setDialogue(String dialogue) {
		this.dialogueJLabel.setText("<html>" + dialogue + "</html>");
	}
	
	
	public void endTutorial() {
		CombatInterface.layerOnePane.remove(this.tutorialPanel);
		
		Combat.currentCombatInstance.turn();
	}
	
	
	public void nextStep() {
		
		switch(currentStep) {
			case 0:
				setDialogue("Welcome to Atoran.<br/>I am A.J. Scarlato, a.k.a, God.");
				currentStep++;
				break;
			case 1:
				setDialogue("I will teach you the basics of this game.");
				currentStep++;
				break;
			case 2:
				setDialogue("The goal of this game is to delete all your opponents from existence. You can do that by attacking them");
				currentStep++;
				break;
			case 3:
				setDialogue("At the start of every wave, each character on your team will be able to perform one move.");
				currentStep++;
				break;
			case 4:
				setDialogue("You can select which move to use here, on the bottom left part of your screen. Once you select your move, "
						+ "click on the target you'd like to perform it on");
				highlight.setSize(CombatInterface.moveMenu.getSize());
				highlight.setLocation(CombatInterface.moveMenu.getLocation());
				highlight.setVisible(true);
				currentStep++;
				break;
			case 5:
				setDialogue("Once you have completed your turn, your enemies will begin their own turn and attack you. This will "
						+ "Continue until one side has been wiped out.");
				highlight.setVisible(false);
				currentStep++;
				break;
			case 6:
				setDialogue("You can also click the dropdown at the top of your screen to access your inventory, where you can use "
						+ "items to assist you in combat.");
				highlight.setSize(CombatInterface.expandButton.getSize());
				highlight.setLocation(CombatInterface.expandButton.getLocation());
				highlight.setVisible(true);
				currentStep++;
				break;
			case 7:
				setDialogue("But you don't have any items yet, so don't worry about that part right now");
				currentStep++;
				break;
			case 8:
				setDialogue("That's all I have to say, good luck.");
				highlight.setVisible(false);
				currentStep++;
				break;
			case 9:
				endTutorial();
				break;
		}
		
	}
}
