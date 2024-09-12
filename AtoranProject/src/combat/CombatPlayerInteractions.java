package combat;

import main.Window;
import utilities.AnimationPlayerModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import animations.Animation;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

public class CombatPlayerInteractions {
	private static JPanel moveMenu = new JPanel();
	private static JPanel selectTargetButtonsPanel = new JPanel();
	private static Move selectedMove;
	private static CombatEntity selectedTarget;
	public static JLayeredPane layerOnePane = new JLayeredPane(); 
	public static JPanel inventoryPanel = new JPanel();
	public static JLabel announcementText = new JLabel("nil", SwingConstants.CENTER);
	public static JPanel infoPanel;
	public static JLabel infoLabel;
	public static JButton expandButton;
	public static JPanel moveInfoDisplay;
	public static boolean infoPanelExpanded = false;
	
	public static void openCombatScreen() {
		Window window = Window.getWindow(); // New JFrame
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
		moveMenu.setLocation(new Point(20, 1080 - 200));
		moveMenu.setSize(new Dimension(300, 200));
		moveMenu.setPreferredSize(new Dimension(300, 200));
		moveMenu.setBackground(new Color(50, 50, 50, 20));
		moveMenu.setVisible(false);
		moveMenu.setLayout(new FlowLayout());
		
		Window.scaleComponent(moveMenu);
		//moveMenu.setVisible(false);
		layerOnePane.add(moveMenu, JLayeredPane.PALETTE_LAYER);
		
		
		createInfoPanel();
		
		
		announcementText.setLocation(0, 100);
		announcementText.setSize(new Dimension(1920, 200));
		announcementText.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(125)));
		announcementText.setVisible(false);
		announcementText.setBackground(new Color(0x7e4f2f));
		announcementText.setOpaque(false);
		
		announcementText.setText("WAVE COMPLETE");
		
		Window.scaleComponent(announcementText);
		
		layerOnePane.add(announcementText, JLayeredPane.PALETTE_LAYER); 
		
		
		loadEntityImagesOfTeam(Combat.teams[0], true);
		
		
		JLabel background = new JLabel();
		background.setLocation(0, 0);
		background.setSize(new Dimension(1920, 1080));
		background.setVisible(true);
		
		File backgroundFile = new File("Resources/Images/TemporaryForestBackgroundCropped.png");
		
		Image image = null;
		try {
			image = ImageIO.read(backgroundFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Image targetImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		//image = image.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
		image = Window.scaleImage(1920, 1080, image);
		
		ImageIcon backgroundIcon = new ImageIcon(image);
		
		background.setIcon(backgroundIcon);
		
		
		layerOnePane.add(background, JLayeredPane.DEFAULT_LAYER); 
		
		moveInfoDisplay = new JPanel();
		moveInfoDisplay.setVisible(false);
		moveInfoDisplay.setSize(new Dimension(375, 250));
		moveInfoDisplay.setLocation(new Point(20, 300));
		moveInfoDisplay.setLayout(new FlowLayout());
		
		Window.scaleComponent(moveInfoDisplay);
		
		layerOnePane.add(moveInfoDisplay, JLayeredPane.MODAL_LAYER);
		
				
		window.setVisible(true);
	}
	
	
	public static void expandInfoPanel() {		
		Point[] destination = new Point[1];
		
		if (infoPanelExpanded == true) {
			destination[0] = Window.scalePoint(new Point(0, -453));
			expandButton.setLocation(Window.scalePoint(new Point(875, 0)));
			infoPanelExpanded = false;
		} else {
			destination[0] = Window.scalePoint(new Point(0, 0));
			expandButton.setLocation(Window.scalePoint(new Point(875, 450)));
			infoPanelExpanded = true;
		}
		
		Animation animation = new Animation(infoLabel, destination, new int[] {15}, "easeInOutSine");
		
		AnimationPlayerModule.addAnimation(animation);
	}
	
	
	public static void createInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setLocation(0, 0);
		infoPanel.setSize(new Dimension(1920, 500));
		infoPanel.setPreferredSize(new Dimension(1920, 500));
		infoPanel.setBackground(new Color(200, 200, 200, 230));
		infoPanel.setVisible(true);
		infoPanel.setOpaque(false);
		//infoPanel.setLayout(null);
		
		infoLabel = new JLabel();
		infoLabel.setLocation(0, -453);
		infoLabel.setSize(new Dimension(1920, 500));
		infoLabel.setPreferredSize(new Dimension(1920, 500));
		infoLabel.setBackground(new Color(200, 200, 200, 230));
		infoLabel.setVisible(true);
		
		File infoFile = new File("Resources/Images/AtoranInventory.png");
		
		Image infoImage = null;
		try {
			infoImage = ImageIO.read(infoFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Image targetImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		//image = image.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
		infoImage = Window.scaleImage(1920, 500, infoImage);
		
		ImageIcon infoIcon = new ImageIcon(infoImage);
		
		infoLabel.setIcon(infoIcon);
		
		Window.scaleComponent(infoLabel);
		
		Window.scaleComponent(inventoryPanel);
		
		Window.scaleComponent(infoPanel);
		
		layerOnePane.add(infoPanel, JLayeredPane.PALETTE_LAYER);
		
		layerOnePane.add(infoLabel, JLayeredPane.PALETTE_LAYER);
		
		
		//inventoryPanel.setLocation(0, 880);
		inventoryPanel.setSize(new Dimension(500, 200));
		inventoryPanel.setPreferredSize(new Dimension(500, 200));
		inventoryPanel.setVisible(false);
		inventoryPanel.setBackground(new Color(0x7e4f2f));
		
		infoPanel.add(inventoryPanel);
		
		infoPanel.setLayout(null);
		
		expandButton = new JButton();
		expandButton.setSize(new Dimension(170, 45));
		expandButton.setPreferredSize(new Dimension(170, 45));
		expandButton.setVisible(true);
		expandButton.setLocation(875, 0);
		expandButton.setOpaque(false);
		expandButton.setContentAreaFilled(false);
		expandButton.setBorderPainted(false);
		
		expandButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent event) {
				expandInfoPanel();
            }
        });
		
		Window.scaleComponent(expandButton);
		
		layerOnePane.add(expandButton, JLayeredPane.PALETTE_LAYER);
	}
	
	
	
	public static JLabel createHealthBar(int Size) {
		JLabel healthBar = new JLabel();
		
		healthBar.setPreferredSize(new Dimension(Size, 15));
		healthBar.setSize(new Dimension(Size, 15));
		healthBar.setBackground(new Color(150, 0, 0));
		healthBar.setOpaque(true);
		
		Window.scaleComponent(healthBar);
		
		return healthBar;
	}
	
	
	public static void loadEntityImagesOfTeam(Team team, boolean leftSide) {
		Point[] positionsArray;
		
		if (leftSide == true) {
			positionsArray = new Point[]{new Point(500, 900), new Point(350, 950), new Point(250, 850)};
		} else {
			positionsArray = new Point[]{new Point(1200, 900), new Point(1350, 950), new Point(1450, 850)};
		}
		
		for (int i = 0; i < team.members.length; i++) {
			CombatEntity member = team.members[i];
			
			if (leftSide == true) {
				member.facingLeft = -1;
			} else {
				member.facingLeft = 1;
			}
			
			Point position = positionsArray[i];
			position = Window.scalePoint(position);
			position = new Point(position.x, position.y - member.sprite.getHeight());
			
			member.loadSpriteIcon();
			member.setFieldPosition(position);
			
			JLabel sprite = member.sprite;	
			sprite.setLocation(member.getFieldPosition());
			
			JLabel healthBar = createHealthBar(member.health);
			
			member.healthBar = healthBar;
			
			Window.scaleComponent(member.sprite);
			
			healthBar.setLocation(new Point(
					member.getFieldPosition().x + sprite.getWidth() / 2 - healthBar.getWidth() / 2, 
					member.getFieldPosition().y + sprite.getWidth()));
			
			Window.scaleComponent(healthBar);
			
			
			layerOnePane.add(sprite, JLayeredPane.PALETTE_LAYER);
			layerOnePane.add(healthBar, JLayeredPane.PALETTE_LAYER);
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
		
		image = Window.scaleImage(100, 100, image);
		
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
		
		layerOnePane.add(selectTargetButtonsPanel, JLayeredPane.MODAL_LAYER);
	}
	
	
	private static void selectMove(Move move) {
		selectedMove = move;
		moveMenu.setVisible(false);
		
		createTargetSelection(move);
	}
	
	
	private static void addMovesToMoveMenu(Move[] moveSet) {
		moveMenu.removeAll();
		
		moveInfoDisplay.removeAll();
		JLabel descriptionDisplay = new JLabel("", SwingConstants.CENTER);
		descriptionDisplay.setSize(new Dimension(375, 150));
		descriptionDisplay.setPreferredSize(new Dimension(375,150));
		descriptionDisplay.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(15)));
		moveInfoDisplay.add(descriptionDisplay);
		
		JLabel damageDisplay = new JLabel("", SwingConstants.CENTER);
		damageDisplay.setSize(new Dimension(120, 100));
		damageDisplay.setPreferredSize(new Dimension(120, 100));
		damageDisplay.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(15)));
		moveInfoDisplay.add(damageDisplay);
		
		JLabel critIncreaseDisplay = new JLabel("", SwingConstants.CENTER);
		critIncreaseDisplay.setSize(new Dimension(120, 100));
		critIncreaseDisplay.setPreferredSize(new Dimension(120, 100));
		critIncreaseDisplay.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(15)));
		moveInfoDisplay.add(critIncreaseDisplay);
		
		JLabel critChanceDisplay = new JLabel("", SwingConstants.CENTER);
		critChanceDisplay.setSize(new Dimension(120, 100));
		critChanceDisplay.setPreferredSize(new Dimension(120, 100));
		critChanceDisplay.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(15)));
		moveInfoDisplay.add(critChanceDisplay);
		
		for (int i = 0; i < moveSet.length; i++) {
			Move move = moveSet[i];
			
			JButton moveButton = new JButton(move.name);
			moveButton.addActionListener(new ActionListener() {
				@Override
	            public void actionPerformed(ActionEvent event) {
					selectMove(move);
	            }
	        });
			
			moveButton.setPreferredSize(new Dimension(140, 50));
			moveButton.setSize(new Dimension(140, 50));
			moveButton.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(20)));
			moveButton.setBackground(new Color(0x1e1006));
			moveButton.setForeground(Color.WHITE);
			
			// For some reason this requires that all methods are overridden
			moveButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					moveInfoDisplay.setVisible(true);
					descriptionDisplay.setText(move.getDescription());
					damageDisplay.setText("<html>Damage:<br/>" + move.getDamage() + "</html");
					critIncreaseDisplay.setText("<html>Crit Increase:<br/>" + move.getCritDamage() + "</html");
					critChanceDisplay.setText("<html>Crit Chance:<br/>" + move.getCritChance() + "%</html");
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					moveInfoDisplay.setVisible(false);
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
	}
	
	
	public static void getTurn(Move[] moveSet) {
		moveMenuPopup(moveSet);
	}
}
