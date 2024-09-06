package combat;

import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import combat.EntitiesAndMoves.AtoranEntity;
import combat.EntitiesAndMoves.SlimeEntity;
import engine.Engine;
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

	
	
	public static void createLevelFromInfo() {
		teamTurn = 0;
		teams = new Team[2];
		boolean fighting = true;
		currentWave = 0;
		currentEntityTurn = null;

		teams[1] = new Team();
		teams[1].automatic = true;
		
		teams[0] = new Team();
		
		AtoranEntity atoran = new AtoranEntity();
		
		teams[0].members = new CombatEntity[1];
		teams[0].members[0] = atoran;
		
		currentTeam = teams[0];
		notCurrentTeam = teams[1];

		
		CombatEntity[] enemies = {new SlimeEntity(), new SlimeEntity()};
		CombatEntity[] enemiesTwo = {new SlimeEntity(), new SlimeEntity(), new SlimeEntity()};
		Wave wave = new Wave(enemies);
		Wave waveTwo = new Wave(enemiesTwo);
		Wave[] wavesThing = {wave, waveTwo};
		waves = wavesThing;
		System.out.println(waves);
	}
	
	public static void main(String[] args) {
		createLevelFromInfo();
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
		
		CombatPlayerInteractions.loadEnemyEntitiesImages();
		
		//Window.getWindow().refresh();
	}
	
	private static boolean checkWaveCompletion() {
		boolean foundAlive = false;
		
		for (int i = 0; i < teams[1].members.length; i++) {
			CombatEntity entity = teams[1].members[i];
			
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
	
	private static boolean checkIfPlayerAlive() {
		boolean foundAlive = false;
		
		for (int i = 0; i < teams[0].members.length; i++) {
			CombatEntity entity = teams[0].members[i];
			
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
	

	public static void levelLost() {
		
	}
	
	
	public static void turn() {
		
		if (checkWaveCompletion() == true) {
			
			int waveCount = waves.length;
			System.out.println("WAVE COMPLETED");
			
			if (currentWave == waveCount - 1) {
				System.out.println("LEVEL COMPLETED");
				fighting = false;
			} else {
				currentWave++;
				
				loadWave();
			}
			
			currentEntityTurnIndex = 0;
			teamTurn = 0;
			currentTeam = teams[0];
			notCurrentTeam = teams[1];
		}
		
		if (checkIfPlayerAlive() == false) {
			levelLost();
		}
		
		CombatEntity entity;
		
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
			
			entity = currentTeam.members[currentEntityTurnIndex];
			currentEntityTurn = entity;
			currentEntityTurnIndex++;
			System.out.println("If");
			if (entity.dead == true) {
				turn();
				return;
			}
			
		} else {
			entity = currentTeam.members[currentEntityTurnIndex];
			currentEntityTurn = entity;
			currentEntityTurnIndex++;
			System.out.println("else");
			
			if (entity.dead == true) {
				turn();
				return;
			}
		}

		Runnable fpsMethod = () -> {
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
				entity.myTurn(currentTeam.automatic, currentTeam.members, notCurrentTeam.members);
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		};
		
		Thread fpsThread = new Thread(fpsMethod);
		fpsThread.start();
	}
}