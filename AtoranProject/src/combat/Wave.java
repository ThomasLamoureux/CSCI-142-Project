package combat;

import cutscenes.Cutscene;

public class Wave {
	public CombatEntity[] enemies;
	public Cutscene cutscene;
	
	public Wave(CombatEntity[] enemies) {
		this.enemies = enemies;
	}
	
	public Wave(CombatEntity[] enemies, Cutscene cutscene) {
		this.enemies = enemies;
		this.cutscene = cutscene;
	}
}
