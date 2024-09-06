package combat;

import main.Window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CombatPlayerInteractions {
	private static JPanel moveMenu = new JPanel();
	private static JPanel selectTargetButtonsPanel = new JPanel();
	private static Move selectedMove;
	private static CombatEntity selectedTarget;
	private static JLayeredPane layerOnePane = new JLayeredPane(); 
	
	public static void openCombatScreen() {
		Window window = main.Window.createWindow(); // New JFrame
		window.clearFrame();
		// Sets window attributes
		window.getContentPane().setBackground(new Color(0xFFFFFF));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1920, 1080);
		window.setLayout(null);
		
		layerOnePane.setLocation(new Point(0, 0));
		layerOnePane.setSize(new Dimension(1920, 1080));
		
		window.add(layerOnePane);
		
		// Move menu
		//moveMenu.setLayout(null);
		//moveMenu.setBounds(200, 700, 0, 0);
		moveMenu.setLocation(new Point(100, 700));
		moveMenu.setSize(new Dimension(320, 280));
		moveMenu.setPreferredSize(new Dimension(320, 280));
		moveMenu.setBackground(new Color(50, 50, 50, 20));
		moveMenu.setVisible(false);
		//moveMenu.setVisible(false);
		layerOnePane.add(moveMenu, 0);
		
		
		loadTeamEntitiesImages();
		
				
		window.setVisible(true);
	}
	
	
	public static void loadEnemyEntitiesImages() {
		final Point[] positionsArrayRight = {new Point(1600, 500), new Point(1650, 600), new Point(1660, 400)};
		
		Team teamTwo = Combat.teams[1];
		
		for (int i = 0; i < teamTwo.members.length; i++) {
			System.out.println("ran once");
			CombatEntity member = teamTwo.members[i];
			
			member.setFieldPosition(positionsArrayRight[i]);
			
			JLabel temporaryLabel = new JLabel(member.name);
			temporaryLabel.setPreferredSize(new Dimension(70, 70));
			temporaryLabel.setSize(new Dimension(70, 70));
			temporaryLabel.setBackground(new Color(255, 0, 0));
			temporaryLabel.setOpaque(true);
			
			JLabel healthBar = new JLabel();
			healthBar.setPreferredSize(new Dimension(80, 20));
			healthBar.setSize(new Dimension(80, 20));
			healthBar.setBackground(new Color(0, 255, 0));
			healthBar.setOpaque(true);
			
			healthBar.setLocation(new Point(member.getFieldPosition().x, member.getFieldPosition().y - 90));
			
			
			temporaryLabel.setLocation(member.getFieldPosition());
			
			member.sprite = temporaryLabel;
			member.healthBar = healthBar;
			
			//Window.getWindow().add(temporaryLabel);
			
			layerOnePane.add(temporaryLabel, JLayeredPane.DEFAULT_LAYER);
			layerOnePane.add(healthBar, JLayeredPane.DEFAULT_LAYER);
		}
		
	}
	
	
	public static void loadTeamEntitiesImages() {
		final Point[] positionsArrayLeft = {new Point(100, 500), new Point(50, 600), new Point(40, 400)};
		
		System.out.print("test");
		
		Team teamOne = Combat.teams[0];
		
		for (int i = 0; i < teamOne.members.length; i++) {
			CombatEntity member = teamOne.members[i];
			
			member.setFieldPosition(positionsArrayLeft[i]);
			
			JLabel temporaryLabel = new JLabel(member.name);
			temporaryLabel.setPreferredSize(new Dimension(70, 70));
			temporaryLabel.setSize(new Dimension(70, 70));
			temporaryLabel.setBackground(new Color(20, 0, 255));
			temporaryLabel.setOpaque(true);
			
			temporaryLabel.setLocation(member.getFieldPosition());
			
			JLabel healthBar = new JLabel();
			healthBar.setPreferredSize(new Dimension(80, 20));
			healthBar.setSize(new Dimension(80, 20));
			healthBar.setBackground(new Color(0, 255, 0));
			healthBar.setOpaque(true);
			
			healthBar.setLocation(new Point(member.getFieldPosition().x, member.getFieldPosition().y - 90));

			
			member.sprite = temporaryLabel;
			member.healthBar = healthBar;
			
			
			layerOnePane.add(temporaryLabel, JLayeredPane.DEFAULT_LAYER);
			layerOnePane.add(healthBar, JLayeredPane.DEFAULT_LAYER);
		}	
	}
	
	// Animation
	public static void teamOneEntrance() {
		
	}
	
	// Animation 
	public static void teamTwoEntrance() {
		
	}
	
	
	private static void confirmTurn() {
		Combat.currentEntityTurn.performTurn(selectedMove, selectedTarget);
		
		Runnable fpsMethod = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
				Combat.turn();
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		
		Thread fpsThread = new Thread(fpsMethod);
		fpsThread.start();
	}
	
	
	private static void selectTarget(CombatEntity target) {
		selectedTarget = target;
		
		selectTargetButtonsPanel.removeAll();
		
		confirmTurn();
	}
	
	
	private static void createTargetButton(CombatEntity target) {
		JButton targetButton = new JButton();
		
		targetButton.setText("test");
		targetButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent event) {
				selectTarget(target);
            }
        });
		//targetButton.setPreferredSize(new Dimension(100, 100));
		targetButton.setSize(new Dimension(target.sprite.getSize()));
		targetButton.setOpaque(true);
		targetButton.setBackground(new Color(0x005572));
		
		
		selectTargetButtonsPanel.add(targetButton);
		selectTargetButtonsPanel.setPreferredSize(new Dimension(1920, 1080));
		selectTargetButtonsPanel.setSize(new Dimension(1920, 1080));
		selectTargetButtonsPanel.setLayout(null);
		selectTargetButtonsPanel.setLocation(new Point(0, 0));
		selectTargetButtonsPanel.setOpaque(false);
		
		targetButton.setLocation(target.getFieldPosition());
		
		layerOnePane.add(selectTargetButtonsPanel, JLayeredPane.PALETTE_LAYER);
		//Window.getWindow().refresh();
	}
	
	
	private static void createTargetSelection(Move move) {
		Team[] teams = Combat.teams;
		
		if (move.checkIfTargetIsValid("enemies")) {
			System.out.println("ene");
			for (int i = 0; i < teams[1].members.length; i++) {
				if (teams[1].members[i].dead == false) { 
					createTargetButton(teams[1].members[i]);
				}
			}
		}
	}
	
	
	private static void selectMove(Move move) {
		selectedMove = move;
		moveMenu.setVisible(false);
		
		createTargetSelection(move);
	}
	
	
	private static void addMovesToMoveMenu(Move[] moveSet) {
		moveMenu.removeAll();
		
		for (int i = 0; i < moveSet.length; i++) {
			Move move = moveSet[i];
			
			JButton moveButton = new JButton(move.name);
			moveButton.addActionListener(new ActionListener() {
				@Override
	            public void actionPerformed(ActionEvent event) {
					selectMove(move);
	            }
	        });
			
			moveMenu.add(moveButton);
			moveMenu.repaint();
			moveMenu.revalidate();
		}
	}
	
	
	private static void moveMenuPopup(Move[] moveSet) {		
		addMovesToMoveMenu(moveSet);
		
		moveMenu.setVisible(true);
		Window window = Window.getWindow();
		//window.refresh();
	}
	
	
	public static void getTurn(Move[] moveSet) {
		moveMenuPopup(moveSet);
	}
}
