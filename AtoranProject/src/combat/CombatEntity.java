package combat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.Window;
import utilities.AnimationPlayerModule;

public class CombatEntity implements Cloneable {
	private Move[] moveSet = {};
	private double damageResistence = 0.0;
	public int health = 0;
	public boolean dead = false;
	private double damageMultiplier = 1.0;
	private Point fieldPosition;
	private String name;
	public JLabel sprite;
	public JLabel healthBar;
	public int maxHealth;
	public int facingLeft;
	public boolean flipIfFacingLeft;
	private File imageFile;
	public boolean flipImages;
	
	public CombatEntity(String name, int health, Move[] moveSet, JLabel sprite, boolean flipImages) {
		this.health = health;
		this.moveSet = moveSet;
		this.name = name; // Temporarys
		this.maxHealth = health;
		this.flipImages = flipImages;
		
		if (sprite == null) {
			JLabel defaultSprite = new JLabel(this.getName());
			defaultSprite.setPreferredSize(new Dimension(70, 70));
			defaultSprite.setSize(new Dimension(70, 70));
			defaultSprite.setBackground(new Color(20, 0, 255));
			defaultSprite.setOpaque(true);
			
			this.sprite = defaultSprite;
		} else {
			this.sprite = sprite;
		}
	}
	
	
	public void setImageFile(File image) {
		this.imageFile = image;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	
	public void loadSpriteIcon() {
		Image image = null;
		try {
			image = ImageIO.read(this.imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Image targetImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		//image = image.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
		image = Window.scaleImage(this.sprite.getWidth(), this.sprite.getHeight(), image);
		
		if (facingLeft == 1 && flipIfFacingLeft == true) {
			image = AnimationPlayerModule.createMirror(image);
		} else if (facingLeft == -1 && flipIfFacingLeft == false) {
			image = AnimationPlayerModule.createMirror(image);
		}
		
		ImageIcon targetIcon = new ImageIcon(image);
		this.sprite.setIcon(targetIcon);
	}

	
	public void setMoveSet(Move[] moveSet) {
		this.moveSet = moveSet;
	}
	
	
	public void performTurn(Move move, CombatEntity target) {
		move.useMove(target);
	}
	
	
	public void updateHealthBar() {
		int healthBarPercent = (int)((double)this.health/(double)this.maxHealth * this.maxHealth);
		this.healthBar.setPreferredSize(new Dimension(healthBarPercent, Window.scaleInt(15) ));
		this.healthBar.setSize(new Dimension(healthBarPercent, Window.scaleInt(15) ));
	}
	
	
	public void recieveDamage(int damage) {
		int totalDamage = (int)(damage - damage * this.damageResistence);
		
		this.health = this.health - totalDamage;
		
		if (this.health <= 0 ) {
			this.death();
		}
	}
	
	
	public void death() {
		this.dead = true;
		
		Runnable removeSprite = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
				this.sprite.setVisible(false);
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		
		Thread removeSpriteThread = new Thread(removeSprite);
		removeSpriteThread.start();
	}
	
	
	public void setFieldPosition(Point position) {
		fieldPosition = position;
	}
	
	public Point getFieldPosition() {
		return fieldPosition;
	}
	
	public void automatedTurn2(CombatEntity[] team, CombatEntity[] enemyTeam) {
		
	}
	
	public void automatedTurn(CombatEntity[] team, CombatEntity[] enemyTeam) {
		Random randomGenerator = new Random();
		
		int randomMove;
		
		if (this.moveSet.length == 1) {
			randomMove = 0;
		} else {
			randomMove = randomGenerator.nextInt(0, this.moveSet.length);
			System.out.println(randomMove);
		}
		
		Move move = moveSet[randomMove];
		
		CombatEntity target; // Set to null to stop error
		
		ArrayList<CombatEntity> potentialTargets = new ArrayList<CombatEntity>();
		
			
		if (move.checkIfTargetIsValid("enemies") == true) {
			for (int i = 0; i < enemyTeam.length; i++) {
				CombatEntity entity = enemyTeam[i];
				
				if (entity.dead == false) {
					potentialTargets.add(entity);
				}
			}
		} else if (move.checkIfTargetIsValid("team") == true) {
			for (int i = 0; i < team.length; i++) {
				CombatEntity entity = team[i];
				
				if (entity.dead == false) {
					potentialTargets.add(entity);
				}
			}
		}
		
		int arrayListSize = potentialTargets.size();
		if (arrayListSize == 0) {
			Combat.currentCombatInstance.turn();
			return;
		} else if (arrayListSize == 1){
			target = potentialTargets.get(0);
		} else {
			int randomTarget = randomGenerator.nextInt(0, arrayListSize);
			target = potentialTargets.get(randomTarget);
		}
		performTurn(move, target);
		Runnable fpsMethod = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(1500);
				Combat.currentCombatInstance.turn();
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		
		Thread fpsThread = new Thread(fpsMethod);
		fpsThread.start();
	}
	
	
	public void myTurn(boolean automatic, CombatEntity[] team, CombatEntity[] enemyTeam) {
		if (automatic == true) {
			automatedTurn(team, enemyTeam);
			
		} else {
			CombatInterface.getTurn(moveSet);
		}
	}
	
	 @Override
	 public Object clone() throws CloneNotSupportedException {
		 return super.clone();
	 }
}
