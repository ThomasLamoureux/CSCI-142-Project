package combat;

import cutscenes.Cutscene;

// Contains wave info
public class Wave {
	public CombatEntity[] enemies;
	public Cutscene cutscene; // Cutscene that plays at the start of the wave
	
	public Wave(CombatEntity[] enemies) {
		this.enemies = enemies;
	}
	
	public Wave(CombatEntity[] enemies, Cutscene cutscene) {
		this.enemies = enemies;
		this.cutscene = cutscene;
	}
}
