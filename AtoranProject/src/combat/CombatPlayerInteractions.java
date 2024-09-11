package combat;

import main.Window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CombatPlayerInteractions {
	private static JPanel moveMenu = new JPanel();
	private static JPanel selectTargetButtonsPanel = new JPanel();
	private static Move selectedMove;
	private static CombatEntity selectedTarget;
	public static JLayeredPane layerOnePane = new JLayeredPane(); 
	public static JPanel inventoryPanel = new JPanel();
	public static JLabel announcementText = new JLabel("nil", SwingConstants.CENTER);
	
	public static void openCombatScreen() {
		Window window = main.Window.createWindow(); // New JFrame
		window.clearFrame();
		// Sets window attributes
		window.getContentPane().setBackground(new Color(0xFFFFFF));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window.setSize(1920, 1080);
		window.setLayout(null);
		
		layerOnePane.setLocation(new Point(0, 0));
		layerOnePane.setSize(new Dimension(1920, 1080));
		
		Window.scaleComponent(layerOnePane);
		
		window.add(layerOnePane);
		
		// Move menu
		//moveMenu.setLayout(null);
		//moveMenu.setBounds(200, 700, 0, 0);
		moveMenu.setLocation(new Point(100, 700));
		moveMenu.setSize(new Dimension(320, 280));
		moveMenu.setPreferredSize(new Dimension(320, 280));
		moveMenu.setBackground(new Color(50, 50, 50, 20));
		moveMenu.setVisible(false);
		
		Window.scaleComponent(moveMenu);
		//moveMenu.setVisible(false);
		layerOnePane.add(moveMenu, JLayeredPane.DEFAULT_LAYER);
		
		inventoryPanel.setLocation(0, 880);
		inventoryPanel.setSize(new Dimension(500, 200));
		inventoryPanel.setPreferredSize(new Dimension(500, 200));
		inventoryPanel.setVisible(true);
		inventoryPanel.setBackground(new Color(0x7e4f2f));
		
		Window.scaleComponent(inventoryPanel);
		
		layerOnePane.add(inventoryPanel, JLayeredPane.DEFAULT_LAYER);
		
		
		announcementText.setLocation(0, 100);
		announcementText.setSize(new Dimension(1920, 200));
		announcementText.setFont(new Font("Algerian", Font.PLAIN, 125));
		announcementText.setVisible(false);
		announcementText.setBackground(new Color(0x7e4f2f));
		announcementText.setOpaque(false);
		
		announcementText.setText("WAVE COMPLETE");
		
		Window.scaleComponent(announcementText);
		
		layerOnePane.add(announcementText, JLayeredPane.MODAL_LAYER); 
		
		
		loadEntityImagesOfTeam(Combat.teams[0], true);
		
				
		window.setVisible(true);
	}
	
	
	public static JLabel createHealthBar() {
		JLabel healthBar = new JLabel();
		
		healthBar.setPreferredSize(new Dimension(80, 20));
		healthBar.setSize(new Dimension(80, 20));
		healthBar.setBackground(new Color(0, 255, 0));
		healthBar.setOpaque(true);
		
		return healthBar;
	}
	
	
	public static void loadEntityImagesOfTeam(Team team, boolean leftSide) {
		Point[] positionsArray;
		
		if (leftSide == true) {
			positionsArray = new Point[]{new Point(100, 500), new Point(50, 600), new Point(40, 400)};
		} else {
			positionsArray = new Point[]{new Point(1600, 500), new Point(1650, 600), new Point(1660, 400)};
		}
		
		for (int i = 0; i < team.members.length; i++) {
			CombatEntity member = team.members[i];
			
			if (leftSide == true) {
				member.facingLeft = -1;
			} else {
				member.facingLeft = 1;
			}
			
			member.loadSpriteIcon();
			member.setFieldPosition(positionsArray[i]);
			
			JLabel sprite = member.sprite;	
			sprite.setLocation(member.getFieldPosition());
			
			JLabel healthBar = createHealthBar();
			healthBar.setLocation(new Point(member.getFieldPosition().x, member.getFieldPosition().y + 75));
			
			member.healthBar = healthBar;
			
			Window.scaleComponent(healthBar);
			
			Window.scaleComponent(member.sprite);
			
			
			layerOnePane.add(sprite, JLayeredPane.DEFAULT_LAYER);
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
		

		targetButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent event) {
				selectTarget(target);
            }
        });
		//targetButton.setPreferredSize(new Dimension(100, 100));
		targetButton.setSize(new Dimension(100, 100));
		targetButton.setBackground(null);
		targetButton.setOpaque(false);
		targetButton.setContentAreaFilled(false);
		targetButton.setBorderPainted(false);
		
		File targetFile = new File("Resources/Images/target.png");
		Image image = null;
		try {
			image = ImageIO.read(targetFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Image targetImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		image = image.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
		ImageIcon targetIcon = new ImageIcon(image);
		targetButton.setIcon(targetIcon);
			
		
		selectTargetButtonsPanel.add(targetButton);
		
		targetButton.setLocation(target.sprite.getLocation().x - 15, target.sprite.getLocation().y - 15);
		//Window.getWindow().refresh();
	}
	
	
	private static void createTargetSelection(Move move) {
		Team[] teams = Combat.teams;
		
		selectTargetButtonsPanel.setPreferredSize(new Dimension(1920, 1080));
		selectTargetButtonsPanel.setSize(new Dimension(1920, 1080));
		selectTargetButtonsPanel.setLayout(null);
		selectTargetButtonsPanel.setLocation(new Point(0, 0));
		selectTargetButtonsPanel.setOpaque(false);
		
		if (move.checkIfTargetIsValid("enemies")) {
			System.out.println("ene");
			for (int i = 0; i < teams[1].members.length; i++) {
				if (teams[1].members[i].dead == false) { 
					createTargetButton(teams[1].members[i]);
				}
			}
		}
		
		layerOnePane.add(selectTargetButtonsPanel, JLayeredPane.PALETTE_LAYER);
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
