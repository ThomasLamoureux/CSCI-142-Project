package combat;

import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import combat.EntitiesAndMoves.AtoranEntity;
import combat.EntitiesAndMoves.SlimeEntity;
import engine.Engine;
import levels.GameMap;
import levels.Level;
import main.Window;
import utilities.AnimationPlayerModule;

public class Combat {
	public static byte teamTurn = 0; // 0 = player team, 1 = enemy team
	public static Team[] teams = new Team[2];
	public static boolean fighting; // Loop variable for fighting until all enemies or player is dead
	public static int currentWave = 0;
	public static Wave[] waves;
	public static CombatEntity currentEntityTurn;
	public static int currentEntityTurnIndex = 0;
	public static Team currentTeam;
	public static Team notCurrentTeam;
	public static Level currentLevel;

	
	
	public static void createLevelFromInfo(Level level) {
		Window.getWindow().clearFrame();
		
		currentLevel = level;
		
		teamTurn = 0;
		teams = new Team[2];
		boolean fighting = true;
		currentWave = 0;
		currentEntityTurn = null;

		teams[1] = new Team();
		teams[1].automatic = true;
		
		teams[0] = new Team();
		
		AtoranEntity atoran = new AtoranEntity();
		AtoranEntity atoran2 = new AtoranEntity();
		AtoranEntity atoran3 = new AtoranEntity();
		
		teams[0].members = new CombatEntity[] {atoran, atoran2, atoran3};
		
		currentTeam = teams[0];
		notCurrentTeam = teams[1];

		
		waves = level.getWaves();
		System.out.println(waves);
		
		initializeCombat();
	}
	
	
	public static void initializeCombat() {
		CombatPlayerInteractions.openCombatScreen();
		loadWave();
		
		Engine.toggleFps(true);
		
		fighting = true;
		
		turn();
	}
	
	
	public static void loadWave() {
		teams[1].members = waves[currentWave].enemies;
		
		CombatPlayerInteractions.loadEntityImagesOfTeam(teams[1], false);
	}
	
	
	private static boolean checkIfTeamIsDead(Team team) {
		boolean foundAlive = false;
		
		for (int i = 0; i < team.members.length; i++) {
			CombatEntity entity = team.members[i];
			
			if (entity.dead == false) {
				foundAlive = true;
				break;
			}
		}
		
		if (foundAlive == true) {
			return false;
		} else {
			return true;
		}
	}
	
	
	public static void levelComplete() {
		CombatPlayerInteractions.announcementText.setText("LEVEL BEAT");
		CombatPlayerInteractions.announcementText.setVisible(true);
		
		currentLevel.setCompleted(true);
		
		
		
		List<Level> levels = GameMap.getLevels();
        Level nextLevel = levels.get(currentLevel.getLevelNumber());
        nextLevel.unlock();
        GameMap.currentMap.updateMap();
	}
	

	public static void levelLost() {
		CombatPlayerInteractions.announcementText.setText("LEVEL LOST");
		CombatPlayerInteractions.announcementText.setVisible(true);
	}
	
	
	public static void waveComplete() {
		int waveCount = waves.length;
		System.out.println("WAVE COMPLETED");
		CombatPlayerInteractions.announcementText.setText("WAVE COMPLETE");
		CombatPlayerInteractions.announcementText.setVisible(true);
		
		if (currentWave == waveCount - 1) {
			System.out.println("LEVEL COMPLETED");
			fighting = false;
			
			levelComplete();
		} else {
			currentWave++;
			
			currentEntityTurnIndex = 0;
			teamTurn = 0;
			currentTeam = teams[0];
			notCurrentTeam = teams[1];
			
			Runnable turnWait = () -> {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					CombatPlayerInteractions.announcementText.setVisible(false);
					loadWave();
					turn();
				} catch (InterruptedException err) {
					err.printStackTrace();
				}
			};
			
			Thread turnWaitThread = new Thread(turnWait);
			turnWaitThread.start();
		}
	}
	
	
	public static void turn() {
		
		if (checkIfTeamIsDead(teams[1]) == true) {
			waveComplete();
			return;
		}
		
		if (checkIfTeamIsDead(teams[0]) == true) {
			levelLost();
			return;
		}
		
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
		
		CombatEntity entity = currentTeam.members[currentEntityTurnIndex];
		currentEntityTurn = entity;
		currentEntityTurnIndex++;
		System.out.println("else");
		
		if (entity.dead == true) {
			//Window.resizeWindow(new Dimension(1, 1));
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