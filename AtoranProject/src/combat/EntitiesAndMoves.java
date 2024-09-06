package combat;

import java.awt.Point;

import utilities.Animation;
import utilities.AnimationPlayerModule;

public class EntitiesAndMoves {
	
	public static class AtoranEntity extends CombatEntity {
		public AtoranEntity() {
			super("Atoran", 150, null);
			
			Move[] moveSet = {new SlashMove(this), new SweepMove(this)};
			this.setMoveSet(moveSet);
		}	
	}
	
	public static class SlimeEntity extends CombatEntity {
		public SlimeEntity() {
			super("Slime", 25, null);
			
			Move[] moveSet = {new BumpMove(this)};
			this.setMoveSet(moveSet);
		}
	}

	public static class BanditEntity extends CombatEntity {

		public BanditEntity() {
			super("Bandit", 50, null);
			
			Move[] moveSet = null;
		}
	}
	
	public static class SlashMove extends Move {

		public SlashMove(CombatEntity parent) {
			super("Slash", false, true, parent);
		
			this.setDamage(25);
		}
	}
	
	public static class BumpMove extends Move {

		public BumpMove(CombatEntity parent) {
			super("Bump", false, true, parent);

			this.setDamage(8);
		}
		
	}
	
	public static class SweepMove extends Move {

		public SweepMove(CombatEntity parent) {
			super("Sweep", false, true, parent);
		
			this.setDamage(13);
		}
		
		@Override
		public void useMove(CombatEntity target) {
			CombatEntity[] enemies = Combat.notCurrentTeam.members;
			
			for (int i = 0; i < enemies.length; i++) {
				enemies[i].recieveDamage(this.getDamage());
			}
			
			Point[] destinations = {target.sprite.getLocation(), this.getParent().sprite.getLocation()};
			int[] framesToTake = {16, 20};
			
			Animation animation = new Animation(this.getParent().sprite, destinations, framesToTake);
			
			AnimationPlayerModule.addAnimation(animation);
		}
	}
}
