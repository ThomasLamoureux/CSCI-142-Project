package combat;

import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import combatEntities.Atoran;
import combatEntities.DralyaHumanForm;
import combatEntities.Knight;
import combatEntities.Samoht;
import engine.Engine;
import inventory.Inventory;
import inventory.Item.DefensePotion;
import inventory.Item.HealthPotion;
import levels.GameMap;
import levels.Level;
import main.Window;
import utilities.AnimationPlayerModule;

public class Combat {
	public byte teamTurn = 0; // 0 = player team, 1 = enemy team
	public Team[] teams = new Team[2];
	public boolean fighting; // Loop variable for fighting until all enemies or player is dead
	public int currentWave = 0; // Tracks waves
	public Wave[] waves;
	public CombatEntity currentEntityTurn;
	public int currentEntityTurnIndex = 0; // The index of the entity currently performing their turn in their team class
	public Team currentTeam;
	public Team notCurrentTeam;
	public Level currentLevel;
	public Inventory inventory;
	public static Combat currentCombatInstance;
	public static Atoran atoran = new Atoran(false);
	public static DralyaHumanForm dralya = new DralyaHumanForm(false);
	
	
	public Combat(Level level) {
		currentCombatInstance = this;
		
		createLevelFromInfo(level);
	}

	
	// Creates a teams, waves, etc from the information passed through the Level object
	public void createLevelFromInfo(Level level) {
		Window.getWindow().clearFrame();
		
		currentLevel = level;
		
		teamTurn = 0;
		teams = new Team[2];
		fighting = true;
		currentWave = 0;
		currentEntityTurn = null;

		teams[1] = new Team();
		teams[1].automatic = true;
		
		teams[0] = new Team();
		
		// Level 6 and 7 will have Dralya on the player's team
		if (level.getLevelNumber() > 0) {
			atoran.reset();
			dralya.reset();
			atoran.loadAnimations();
			dralya.loadAnimations();
			teams[0].members = new CombatEntity[] {atoran, dralya};
		} else {
			atoran.loadAnimations();
			teams[0].members = new CombatEntity[] {atoran};
		}
		
		if (level.getLevelNumber() > 0) {
			this.inventory = new Inventory(2);
			this.inventory.addItem(new HealthPotion("Health Potion", "Heals all allies on the field by 50 hp", 1));
			this.inventory.addItem(new DefensePotion("Defense Poition", "Grants 30% damage reduction to all allies on the field for on attack", 1));
		} else {
			this.inventory = null;
		}
		currentTeam = teams[0];
		notCurrentTeam = teams[1];

		waves = level.getWaves();
		
		initializeCombat(); // Starts the combat
	}
	
	
	public void initializeCombat() {
		CombatInterface.openCombatScreen(); // Opens the GUI interface for the player
		
		loadWave(); // Loads the first wave
		
		Engine.toggleFps(true);
		fighting = true;
		
		// Checks if the current level is the first one and it has not been completed
		if (this.currentLevel.getLevelNumber() == 1 && this.currentLevel.isCompleted() == false) {
			new Tutorial(); // Level 1 tutorial
		} else {
			turn();
		}
	}
	
	// Sets the next wave as the enemy teams members and loads the GUI
	public void loadWave() {
		teams[1].members = waves[currentWave].enemies;
		
		// Resets health and other things of the enemy
		for (CombatEntity entity : teams[1].members) {
			entity.reset();
		}
		
		CombatInterface.loadEntityImagesOfTeam(teams[1], false); // Loads enemy GUI
	}
	
	// Checks if a team is dead
	private boolean checkIfTeamIsDead(Team team) {
		boolean foundAlive = false;
		
		// Loops to check for any living members
		for (int i = 0; i < team.members.length; i++) {
			CombatEntity entity = team.members[i];
			
			if (entity.dead == false) {
				foundAlive = true;
				break;
			}
		}
		
		if (foundAlive == true) {
			return false; // The team is still alive
		} else {
			return true; // The team is dead
		}
	}
	
	// Method to be called when the level is completed
	public void levelComplete() {
		CombatInterface.announcementText.setText("LEVEL BEAT");
		CombatInterface.announcementText.setVisible(true);
		
		
		currentLevel.completeLevel();
		
		List<Level> levels = GameMap.getLevels();
        Level nextLevel = levels.get(currentLevel.getLevelNumber()); /* Gets the next level, (levels are numbered 1-7, 
        										indexes are 0-6 so the current level number will return the next level*/
        nextLevel.unlock(); // Unlocks the next level
        
        // Adds a delay before returning to gamemap
		Runnable wait = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(3000);
				Window.getWindow().clearFrame();
				GameMap.currentMap.openGameMap();
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		Thread waitThread = new Thread(wait);
		waitThread.start();
	}
	
	// Method to be called when the level is lost
	public void levelLost() {
		CombatInterface.announcementText.setText("LEVEL LOST");
		CombatInterface.announcementText.setVisible(true);
		
        // Adds a delay before returning to gamemap
		Runnable wait = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(3000);
				Window.getWindow().clearFrame();
				GameMap.currentMap.openGameMap();
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		Thread waitThread = new Thread(wait);
		waitThread.start();
	}
	
	// Method to be called when the current wave has been defeated
	public void waveComplete() {
		int waveCount = waves.length;

		CombatInterface.announcementText.setText("WAVE COMPLETE");
		CombatInterface.announcementText.setVisible(true);
		
		if (currentWave == waveCount - 1) { // Checks if it was the last wave
			// Level is complete
			fighting = false;
			levelComplete();
		} else { // Starts the second wave
			currentWave++;
			// Resets
			currentEntityTurnIndex = 0;
			teamTurn = 0;
			currentTeam = teams[0];
			notCurrentTeam = teams[1];
			
			// Adds a delay before starting the next wave
			Runnable wait = () -> {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					CombatInterface.announcementText.setVisible(false);
					loadWave();
					turn();
				} catch (InterruptedException err) {
					err.printStackTrace();
				}
			};
			
			Thread waitThread = new Thread(wait);
			waitThread.start();
		}
	}
	
	// Gets the turn of the entities
	public void turn() {
		// Runs check to see if the enemy team is dead
		if (checkIfTeamIsDead(teams[1]) == true) {
			// Completed the wave
			waveComplete();
			return;
		}
		// Runs check to see if the player team is dead
		if (checkIfTeamIsDead(teams[0]) == true) {
			// Lost the level
			levelLost();
			return;
		}
		
		// Checks to see if every member of the current team has performed the turn
		if (currentEntityTurnIndex == currentTeam.members.length) { 
			// Switches team 
			if (teamTurn == 0) {
				teamTurn = 1;
				notCurrentTeam = teams[0];
			} else {
				teamTurn = 0;
				notCurrentTeam = teams[1];
			}
	
			currentEntityTurnIndex = 0;
			currentTeam = teams[teamTurn];
		}
		
		CombatEntity entity = currentTeam.members[currentEntityTurnIndex]; // Gets entity
		currentEntityTurn = entity;
		currentEntityTurnIndex++;
		
		// Entities are not removed when they die, instead they are set as dead and skipped
		if (entity.dead == true) {
			turn();
			return;
		}
		
		// This wait exists to give time for animations to play
		// Using a runnable and thread is necessary to prevent the player screen from freezing during the wait
		Runnable turnWait = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(1200);
				entity.myTurn(currentTeam.automatic, currentTeam.members, notCurrentTeam.members);
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		
		Thread turnWaitThread = new Thread(turnWait);
		turnWaitThread.start();
	}
}