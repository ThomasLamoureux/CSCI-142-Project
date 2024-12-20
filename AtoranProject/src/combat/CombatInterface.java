package combat;

import main.Window;
import utilities.AnimationPlayerModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
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
import javax.swing.border.EmptyBorder;

import animations.Animation;
import animations.Animation.MovementAnimation;
import inventory.Item;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

public class CombatInterface {
	public static JPanel moveMenu; // Contains move options
	private static JPanel selectTargetButtonsPanel; // Panel contains the targets for selecting who to use a move on
	private static Move selectedMove;
	private static CombatEntity selectedTarget;
	public static JLayeredPane layerOnePane; 
	public static JPanel inventoryPanel; // Contains the items
	public static JLabel announcementText; // Level beat, wave complete, etc.
	public static JPanel infoPanel; // Inventory panel
	public static JLabel infoLabel;
	public static JButton expandButton; // Button for expanding inventory pull down
	public static JPanel moveInfoDisplay;
	public static boolean infoPanelExpanded = false;

	// Displays the combat screen
	public static void openCombatScreen() {
		Window window = Window.getWindow(); // New JFrame
		window.clearFrame();
		window.getContentPane().setBackground(new Color(0xFFFFFF));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		
		layerOnePane = new JLayeredPane();
		layerOnePane.setLocation(new Point(0, 0));
		layerOnePane.setSize(new Dimension(1920, 1080));
		
		Window.scaleComponent(layerOnePane);
		
		window.add(layerOnePane);
		
		// Move menu
		moveMenu = new JPanel();
		moveMenu.setLocation(new Point(20, 1080 - 200));
		moveMenu.setSize(new Dimension(300, 200));
		moveMenu.setPreferredSize(new Dimension(300, 200));
		moveMenu.setBackground(new Color(50, 50, 50, 20));
		moveMenu.setVisible(false);
		moveMenu.setLayout(new FlowLayout());
		
		Window.scaleComponent(moveMenu);
		layerOnePane.add(moveMenu, JLayeredPane.PALETTE_LAYER);
		
		// Creates inventory panel
		createInfoPanel();
		
		announcementText = new JLabel("nil", SwingConstants.CENTER);
		announcementText.setLocation(0, 100);
		announcementText.setSize(new Dimension(1920, 200));
		announcementText.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(125)));
		announcementText.setVisible(false);
		announcementText.setBackground(new Color(0x7e4f2f));
		announcementText.setForeground(Color.WHITE);
		announcementText.setOpaque(false);
		
		announcementText.setText("WAVE COMPLETE");
		
		Window.scaleComponent(announcementText);
		
		layerOnePane.add(announcementText, JLayeredPane.MODAL_LAYER); 
		
		
		loadEntityImagesOfTeam(Combat.currentCombatInstance.teams[0], true);
		
		
		JLabel background = new JLabel();
		background.setLocation(0, 0);
		background.setSize(new Dimension(1920, 1080));
		background.setVisible(true);
		
		// Sets the background of the level
		File backgroundFile = new File(Combat.currentCombatInstance.currentLevel.getBackground());
		Image image = null;
		try {
			image = ImageIO.read(backgroundFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		image = Window.scaleImage(1920, 1080, image);
		
		ImageIcon backgroundIcon = new ImageIcon(image);
		
		background.setIcon(backgroundIcon);
		
		Window.scaleComponent(background);
		
		layerOnePane.add(background, JLayeredPane.DEFAULT_LAYER); 
		
		moveInfoDisplay = new JPanel();
		moveInfoDisplay.setVisible(false);
		moveInfoDisplay.setSize(new Dimension(375, 250));
		moveInfoDisplay.setLocation(new Point(80, 600));
		moveInfoDisplay.setLayout(new FlowLayout());
		moveInfoDisplay.setBackground(new Color(0x1e1006));
		
		Window.scaleComponent(moveInfoDisplay);
		
		layerOnePane.add(moveInfoDisplay, JLayeredPane.MODAL_LAYER);
		
		loadInventory();
				
		window.setVisible(true);
	}
	
	// Loads the inventory and displays the items
	public static void loadInventory() {
		if (Combat.currentCombatInstance.inventory == null) {
			return;
		}
		inventoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, Window.scaleInt(18), Window.scaleInt(18)));
		inventoryPanel.setLocation(76 - 18, 36 - 18 - 453);
		inventoryPanel.setOpaque(false);
		inventoryPanel.setSize(new Dimension(800, 500));
		inventoryPanel.setVisible(true);
		
		Window.scaleComponent(inventoryPanel);
		
		layerOnePane.add(inventoryPanel, JLayeredPane.MODAL_LAYER);
		
		for (Item item : Combat.currentCombatInstance.inventory.getInventory()) {
			JButton itemLabel = new JButton();
			itemLabel.setSize(new Dimension(104, 104));
			itemLabel.setPreferredSize(new Dimension(Window.scaleInt(104), Window.scaleInt(104)));
			itemLabel.setBackground(Color.black);
			itemLabel.setOpaque(false);
			itemLabel.setBorderPainted(false);
			itemLabel.addActionListener(new ActionListener() {
				@Override
	            public void actionPerformed(ActionEvent event) {
					if (item.useItem() == true) { // Will return false if the item cannot be used at the moment
						itemLabel.setEnabled(false); // Prevents item from being used again
					};
	            }
	        });
			Window.scaleComponent(itemLabel);
			
			File infoFile = new File(item.imagePath);
			
			Image image = null;
			try {
				image = ImageIO.read(infoFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			image = Window.scaleImage(104, 104, image);
			
			ImageIcon icon = new ImageIcon(image);
			itemLabel.setIcon(icon);
			
			inventoryPanel.add(itemLabel);
		}
	}
	
	// Brings the inventory panel down
	public static void expandInfoPanel() {		
		Point destination;
		Point destinationTwo;
		
		if (infoPanelExpanded == true) { // Unexpands
			destination = Window.scalePoint(new Point(0, -453));
			expandButton.setLocation(Window.scalePoint(new Point(875, 0)));
			infoPanelExpanded = false;
			
			destinationTwo = Window.scalePoint(new Point(76 - 18, 36 - 18 - 453));
		} else { // Expands
			destination = Window.scalePoint(new Point(0, 0));
			expandButton.setLocation(Window.scalePoint(new Point(875, 450)));
			infoPanelExpanded = true;
			
			destinationTwo = Window.scalePoint(new Point(76 - 18, 36 - 18));
		}
		
		// Animation
		MovementAnimation animation = new MovementAnimation(infoLabel, 15, "easeInOutSine", destination, null);
		MovementAnimation animationTwo = new MovementAnimation(inventoryPanel, 15, "easeInOutSine", destinationTwo, null);
		
		AnimationPlayerModule.addAnimation(animation);
		AnimationPlayerModule.addAnimation(animationTwo);
	}
	
	// Creates the inventory panel
	public static void createInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setLocation(0, 0);
		infoPanel.setSize(new Dimension(1920, 500));
		infoPanel.setPreferredSize(new Dimension(1920, 500));
		infoPanel.setBackground(new Color(200, 200, 200, 230));
		infoPanel.setVisible(true);
		infoPanel.setOpaque(false);

		
		infoLabel = new JLabel();
		infoLabel.setLocation(0, -453);
		infoLabel.setSize(new Dimension(1920, 500));
		infoLabel.setPreferredSize(new Dimension(1920, 500));
		infoLabel.setBackground(new Color(200, 200, 200, 230));
		infoLabel.setVisible(true);
		
		File infoFile = new File("Resources/Images/AtoranInventory.png"); // Background image
		
		Image infoImage = null;
		try {
			infoImage = ImageIO.read(infoFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		infoImage = Window.scaleImage(1920, 500, infoImage);
		
		ImageIcon infoIcon = new ImageIcon(infoImage);
		
		infoLabel.setIcon(infoIcon);
		
		Window.scaleComponent(infoLabel);
			
		Window.scaleComponent(infoPanel);
		
		layerOnePane.add(infoPanel, JLayeredPane.PALETTE_LAYER);
		
		layerOnePane.add(infoLabel, JLayeredPane.PALETTE_LAYER);
		
		
		inventoryPanel = new JPanel();
		inventoryPanel.setSize(new Dimension(500, 200));
		inventoryPanel.setPreferredSize(new Dimension(500, 200));
		inventoryPanel.setVisible(false);
		inventoryPanel.setBackground(new Color(0x7e4f2f));
		
		Window.scaleComponent(inventoryPanel);
		
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
	
	
	// Creates a health bar
	public static JLabel createHealthBar(int size) {
		JLabel healthBar = new JLabel("" + size + "/" + size);
		
		healthBar.setSize(new Dimension(200, 15));
		healthBar.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(15)));
		healthBar.setForeground(Color.white);
		healthBar.setBackground(new Color(150, 0, 0));
		healthBar.setOpaque(true);
		
		Window.scaleComponent(healthBar);
		
		return healthBar;
	}
	
	// Loads the images of each entity in the team.
	public static void loadEntityImagesOfTeam(Team team, boolean leftSide) {
		Point[] positionsArray;
		
		if (leftSide == true) {
			positionsArray = new Point[]{new Point(500, 900), new Point(350, 950), new Point(250, 850)}; // Positions for enemies
		} else {
			positionsArray = new Point[]{new Point(1200, 900), new Point(1350, 950), new Point(1450, 850)}; // Positions for team
		}
		
		// Loops through team members and creates their sprites
		for (int i = 0; i < team.members.length; i++) {
			CombatEntity member = team.members[i];
			
			// Sets the direction they are facing
			if (leftSide == true) {
				member.facingLeft = -1;
			} else {
				member.facingLeft = 1;
			}
			
			Point position = positionsArray[i];
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

			if (i == 1) {
				layerOnePane.add(sprite, Integer.valueOf(130));
			} else if (i == 2) {
				layerOnePane.add(sprite, Integer.valueOf(110));
			} else {
				layerOnePane.add(sprite, Integer.valueOf(120));
			}
			
			layerOnePane.add(healthBar, JLayeredPane.PALETTE_LAYER);
		}	
	}
	
	// Performs turn for selected move and target
	private static void confirmTurn() {
		Combat.currentCombatInstance.currentEntityTurn.performTurn(selectedMove, selectedTarget);
		
		// Delay
		Runnable fpsMethod = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
				Combat.currentCombatInstance.turn();
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		
		Thread fpsThread = new Thread(fpsMethod);
		fpsThread.start();
	}
	
	// Selects the target
	private static void selectTarget(CombatEntity target) {
		selectedTarget = target;
		
		selectTargetButtonsPanel.removeAll();
		moveMenu.removeAll();
		moveMenu.setVisible(false);
		
		confirmTurn();
	}
	
	// Creates a JLabel with target icon positioned at the targets location
	private static void createTargetButton(CombatEntity target) {
		JButton targetButton = new JButton();
		

		targetButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent event) {
				selectTarget(target);
            }
        });

		targetButton.setSize(new Dimension(150, 150));
		targetButton.setBackground(null);
		targetButton.setOpaque(false);
		targetButton.setContentAreaFilled(false);
		targetButton.setBorderPainted(false);
		Window.scaleComponent(targetButton);
		
		File targetFile = new File("Resources/Images/target.png"); // Target icon
		Image image = null;
		try {
			image = ImageIO.read(targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		image = Window.scaleImage(150, 150, image);
		
		ImageIcon targetIcon = new ImageIcon(image);
		targetButton.setIcon(targetIcon);
			
		
		selectTargetButtonsPanel.add(targetButton);
		
		int offset = (targetButton.getWidth() - target.sprite.getWidth())/2;
		Point location = new Point(
				target.sprite.getX() - offset,
				target.sprite.getY() - offset
				);

		targetButton.setLocation(location);
	}
	
	// Creates JLabels with target icons and hovers them above the enemies
	private static void createTargetSelection(Move move) {
		Team[] teams = Combat.currentCombatInstance.teams;
		if (selectTargetButtonsPanel != null) { // This is for win you select a different move after already selecting one
			selectTargetButtonsPanel.removeAll();
		}
		
		selectTargetButtonsPanel = new JPanel();
		selectTargetButtonsPanel.setPreferredSize(new Dimension(1920, 1080));
		selectTargetButtonsPanel.setSize(new Dimension(1920, 1080));
		selectTargetButtonsPanel.setLayout(null);
		selectTargetButtonsPanel.setLocation(new Point(0, 0));
		selectTargetButtonsPanel.setOpaque(false);
		
		// Checks if it should place targets over enemies
		if (move.checkIfTargetIsValid("enemies")) {
			for (int i = 0; i < teams[1].members.length; i++) {
				if (teams[1].members[i].dead == false) { 
					createTargetButton(teams[1].members[i]);
				}
			}
		}
		
		// Checks if it should place targets over allies
		if (move.checkIfTargetIsValid("team")) {
			for (int i = 0; i < teams[0].members.length; i++) {
				if (teams[1].members[i].dead == false) { 
					createTargetButton(teams[1].members[i]);
				}
			}
		}
		
		// Checks if it should only place a target on the current entity performing their turn
		if (move.checkIfTargetIsValid("self")) {
			createTargetButton(move.getParent());
		}
		
		layerOnePane.add(selectTargetButtonsPanel, JLayeredPane.MODAL_LAYER);
	}
	
	// Select the move
	private static void selectMove(Move move) {
		selectedMove = move;
		
		createTargetSelection(move);
	}
	
	// Adds the entity's moves to the moveMenu
	private static void addMovesToMoveMenu(Move[] moveSet) {
		moveMenu.removeAll();
		
		moveInfoDisplay.removeAll();
		JLabel descriptionDisplay = new JLabel("", SwingConstants.CENTER);
		descriptionDisplay.setSize(new Dimension(375, 150));
		descriptionDisplay.setPreferredSize(new Dimension(375,150));
		descriptionDisplay.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(18)));
		descriptionDisplay.setForeground(Color.WHITE);
		Window.scaleComponent(descriptionDisplay);
		moveInfoDisplay.add(descriptionDisplay);
		
		JLabel damageDisplay = new JLabel("", SwingConstants.CENTER);
		damageDisplay.setSize(new Dimension(120, 100));
		damageDisplay.setPreferredSize(new Dimension(120, 100));
		damageDisplay.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(15)));
		damageDisplay.setForeground(Color.WHITE);
		Window.scaleComponent(damageDisplay);
		moveInfoDisplay.add(damageDisplay);
		
		JLabel critIncreaseDisplay = new JLabel("", SwingConstants.CENTER);
		critIncreaseDisplay.setSize(new Dimension(120, 100));
		critIncreaseDisplay.setPreferredSize(new Dimension(120, 100));
		critIncreaseDisplay.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(15)));
		Window.scaleComponent(critIncreaseDisplay);
		System.out.println(Window.scaleInt(15) + " " +  critIncreaseDisplay.getWidth());
		critIncreaseDisplay.setForeground(Color.WHITE);
		moveInfoDisplay.add(critIncreaseDisplay);
		
		JLabel critChanceDisplay = new JLabel("", SwingConstants.CENTER);
		critChanceDisplay.setSize(new Dimension(120, 100));
		critChanceDisplay.setPreferredSize(new Dimension(120, 100));
		critChanceDisplay.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(15)));
		critChanceDisplay.setForeground(Color.WHITE);
		Window.scaleComponent(critChanceDisplay);
		moveInfoDisplay.add(critChanceDisplay);
		
		// Loops through moves and creates buttons
		for (int i = 0; i < moveSet.length; i++) {
			Move move = moveSet[i];
			
			JButton moveButton = new JButton(move.name);
			moveButton.addActionListener(new ActionListener() {
				@Override
	            public void actionPerformed(ActionEvent event) {
					selectMove(move);
	            }
	        });
			
			moveButton.setPreferredSize(new Dimension(Window.scaleInt(135), Window.scaleInt(50)));
			moveButton.setSize(new Dimension(Window.scaleInt(135), Window.scaleInt(50)));
			moveButton.setFont(new Font("Algerian", Font.PLAIN, Window.scaleInt(20)));
			moveButton.setBackground(new Color(0x1e1006));
			moveButton.setForeground(Color.WHITE);
			
			if (move.disabled == true) {
				moveButton.setEnabled(false);
			}
			
			// For some reason this requires that all methods are overridden
			moveButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {
					// Sets move info display to visible and sets info
					moveInfoDisplay.setVisible(true);
					descriptionDisplay.setText("<html>" + move.getDescription() + "</html");
					damageDisplay.setText("<html>Damage:<br/>" + move.getDamage() + "</html");
					critIncreaseDisplay.setText("<html>Crit Increase:<br/>" + 1.0 + "</html");
					critChanceDisplay.setText("<html>Crit Chance:<br/>" + 0 + "%</html");
				}
				@Override
				public void mouseExited(MouseEvent e) {
					// Removes info display
					moveInfoDisplay.setVisible(false);
				}
			});
			
			moveMenu.add(moveButton);
			// ??
			moveMenu.repaint();
			moveMenu.revalidate();
		}
	}
	
	// Creates the moveMenu for the turn
	private static void moveMenuPopup(Move[] moveSet) {		
		addMovesToMoveMenu(moveSet);
		
		moveMenu.setVisible(true);
	}
	
	// Method called by combat class to get the players turn
	public static void getTurn(Move[] moveSet) {
		moveMenuPopup(moveSet);
	}
}
