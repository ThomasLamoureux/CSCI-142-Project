package combatEntities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import animations.Animation;
import animations.Keyframe;
import animations.Animation.CombinedAnimation;
import animations.Animation.GraphicAnimation;
import animations.Animation.MovementAnimation;
import combat.Combat;
import combat.CombatEntity;
import combat.CombatInterface;
import combat.Move;
import main.Window;
import utilities.AnimationPlayerModule;
import utilities.AnimationsPreloader;

public class Samoht extends CombatEntity {
	private boolean chargingDarkSoul = false;
	private boolean usedDarkSoul;
	public GraphicAnimation darkSoulCharge;
	public JLabel darkSoulLabel;
	
	public static JLabel getSamohtSprite() {
		JLabel sprite = new JLabel("Dralya");
		sprite.setPreferredSize(new Dimension(250, 250));
		sprite.setSize(new Dimension(250, 250));
		sprite.setOpaque(false);
		
		return sprite;
	}
	
	public Samoht(boolean flipImages) {
		super("Samoht", 300, null, getSamohtSprite(), flipImages);
		
		File targetFile = new File("Resources/Images/DralyaDragonForm.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		Move[] moveSet = {new MagicBullet(this), new TrapSpell(this), new Multihit(this), new ChargeDarkSoul(this), new DarkSoul(this)};
		this.setMoveSet(moveSet);
	}	
	
	
	@Override
	protected Move chooseMove() {
		Random randomGenerator = new Random();
		
		int randomMove;
		Move move;
		
		if (this.chargingDarkSoul == true) {
			move = this.moveSet[4];
		} else if (this.health <= this.maxHealth && this.usedDarkSoul == false) {
			move = this.moveSet[3];
		} else {
			if (this.moveSet.length == 1) {
				randomMove = 0;
			} else {
				randomMove = randomGenerator.nextInt(0, this.moveSet.length - 2);
			}
			
			move = moveSet[randomMove];
		}

		
		return move;
	}
	
	
	public static class MagicBullet extends Move {

		public MagicBullet(CombatEntity parent) {
			super("Dragon Slash", new boolean[]{true, false, false}, parent);
		
			this.setDamage(60);
			this.setDescription("Fires a magic bullet at the enemy");
			
			this.preLoadAnimations();
		}
		
		@Override
		protected void preLoadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/MagicBullet", new Dimension(300, 300), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/SideWaysPortal", new Dimension(300, 300), !this.getParent().flipImages),
					};
		}
		

		@Override
		protected void runAnimation(CombatEntity target) {			
			JLabel portalLabel = new JLabel();
			portalLabel.setSize(new Dimension((int)(300), (int)(300)));
			Window.scaleComponent(portalLabel);
			
			int portalOffset = (portalLabel.getWidth() - target.sprite.getWidth())/2;
			Point portalLocation = new Point(
					target.sprite.getX() - portalOffset + Window.scaleInt(400) * this.getParent().facingLeft,
					target.sprite.getY() - portalOffset
					);
			
			portalLabel.setLocation(portalLocation);		
			
			GraphicAnimation portalGraphics = new GraphicAnimation(portalLabel, 25, this.uniqueIndex[1], 0, 5);
			portalGraphics.setLooped(true);
			portalGraphics.setLoopStartIndex(5);
			
			Runnable removePortalLabel = () -> {
				CombatInterface.layerOnePane.remove(portalLabel);
			};
			
			Runnable addPortalLabel = () -> {
				CombatInterface.layerOnePane.add(portalLabel, JLayeredPane.MODAL_LAYER);
			};
			portalGraphics.keyframes[0] = new Keyframe(addPortalLabel);
			
			
			
			JLabel bulletLabel = new JLabel();
			bulletLabel.setSize(new Dimension((int)(300), (int)(300)));
			Window.scaleComponent(bulletLabel);
			

			Point bulletStartingLocation = portalLabel.getLocation();
			Point bulletTargetDestination = new Point(target.sprite.getLocation().x, bulletStartingLocation.y);
			
			bulletLabel.setLocation(bulletStartingLocation);			
			
			GraphicAnimation bulletGraphics = new GraphicAnimation(bulletLabel, 4, this.uniqueIndex[0], 0, 1);
			MovementAnimation bulletMovement = new MovementAnimation(bulletLabel, 12, "easeInQuad", bulletTargetDestination, bulletStartingLocation);
			
			
			Runnable removeBulletLabel = () -> {
				CombatInterface.layerOnePane.remove(bulletLabel);
			};
			
			Runnable addBulletLabel = () -> {
				CombatInterface.layerOnePane.add(bulletLabel, JLayeredPane.MODAL_LAYER);
			};
			bulletGraphics.keyframes[0] = new Keyframe(addBulletLabel);
			
			
			
			Runnable shakeAnimation = () -> {
				AnimationPlayerModule.shakeAnimation(target);
				target.updateHealthBar();
			};
			bulletMovement.keyframes[11] = new Keyframe(shakeAnimation);
			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(portalGraphics);
			animationsList.add(bulletGraphics);
			animationsList.add(bulletMovement);
			
			
			Animation finalAnimation = new CombinedAnimation(110, animationsList, new int[]{0, 55, 57});
			finalAnimation.keyframes[109] = new Keyframe(removePortalLabel);
			finalAnimation.keyframes[68] = new Keyframe(removeBulletLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
	
	public static class Multihit extends Move {

		public Multihit(CombatEntity parent) {
			super("Death from Above", new boolean[]{true, false, false}, parent);
		
			this.setDamage(30);
			this.setDescription("Targets all enemies on the field with a sweeping attack");
			
			this.preLoadAnimations();
		}
		
		@Override
		public void useMove(CombatEntity target) {
			CombatEntity[] enemies = Combat.currentCombatInstance.notCurrentTeam.members;
			
			for (int i = 0; i < enemies.length; i++) {
				enemies[i].recieveDamage(this.getDamage());
			}
			
			runAnimation(target);
		}
		
		@Override
		protected void preLoadAnimations() {
			this.uniqueIndex = new int[] {AnimationsPreloader.loadImages("Resources/Animations/Portal", new Dimension(400, 400), !this.getParent().flipImages),
					AnimationsPreloader.loadImages("Resources/Animations/SamohtMultiHit", new Dimension(400, 400), false),
					};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity blank) {
			boolean flipImage = false;
			
			for (CombatEntity target : Combat.currentCombatInstance.notCurrentTeam.members) {			
				if (target.dead == true) {
					continue;
				}
				JLabel targetSprite = target.sprite;
				
				JLabel portalLabel = new JLabel();
				portalLabel.setSize(new Dimension((int)(400), (int)(400)));
				Window.scaleComponent(portalLabel);
				
				int portalOffset = (portalLabel.getWidth() - targetSprite.getWidth())/2;
				Point portalLocation = new Point(
						targetSprite.getX() - portalOffset,
						targetSprite.getY() - Window.scaleInt(350)
						);
				portalLabel.setLocation(portalLocation);
				
				
				GraphicAnimation portalOpenGraphic = new GraphicAnimation(portalLabel, 25, this.uniqueIndex[0], 0, 5);
				portalOpenGraphic.setLooped(true);
				portalOpenGraphic.setLoopStartIndex(1);
				
				
				Runnable addPortalLabel = () -> {
					CombatInterface.layerOnePane.add(portalLabel, JLayeredPane.MODAL_LAYER);
				};
				portalOpenGraphic.keyframes[0] = new Keyframe(addPortalLabel);
				
				
				Runnable removePortalLabel = () -> {
					CombatInterface.layerOnePane.remove(portalLabel);
				};
				
				
				
				JLabel attackLabel = new JLabel();
				attackLabel.setSize(new Dimension((int)(400), (int)(400)));
				Window.scaleComponent(attackLabel);
				
				Point attackLocation = new Point(
						targetSprite.getX() - portalOffset,
						targetSprite.getY() - Window.scaleInt(150)
						);
				attackLabel.setLocation(attackLocation);
				
				GraphicAnimation attackGraphic = new GraphicAnimation(attackLabel, 30, this.uniqueIndex[1], 0, 2);
				
				
				Runnable addAttackLabel = () -> {
					CombatInterface.layerOnePane.add(attackLabel, JLayeredPane.MODAL_LAYER);
				};
				attackGraphic.keyframes[0] = new Keyframe(addAttackLabel);
				
				
				Runnable removeAttackLabel = () -> {
					CombatInterface.layerOnePane.remove(attackLabel);
				};
				
				Runnable shakeAnimation = () -> {
					AnimationPlayerModule.shakeAnimation(target);
					target.updateHealthBar();
				};
				attackGraphic.keyframes[5] = new Keyframe(shakeAnimation); 
				
				
				ArrayList<Animation> animationsList = new ArrayList<>();
				animationsList.add(portalOpenGraphic);
				animationsList.add(attackGraphic);
				
				
				Animation finalAnimation = new CombinedAnimation(120, animationsList, new int[]{0, 50});
				finalAnimation.keyframes[80] = new Keyframe(removeAttackLabel);
				finalAnimation.keyframes[119] = new Keyframe(removePortalLabel);
				
				AnimationPlayerModule.addAnimation(finalAnimation);
			}
		}
	}
	
	
	public static class TrapSpell extends Move {

		public TrapSpell(CombatEntity parent) {
			super("Death from Above", new boolean[]{true, false, false}, parent);
		
			this.setDamage(0);
			this.setDescription("Targets all enemies, increasing damage taken by 50% and decresing damage dealt by 50% until spell is cleansed");
			
			this.preLoadAnimations();
		}
		
		@Override
		public void useMove(CombatEntity target) {
			runAnimation(target);
		}
		
		
		@Override
		protected void preLoadAnimations() {
			this.uniqueIndex = new int[]{AnimationsPreloader.loadImages("Resources/Animations/TrapSpell", new Dimension(300, 300), false)};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity blank) {
			for (CombatEntity target : Combat.currentCombatInstance.notCurrentTeam.members) {				
				JLabel targetSprite = target.sprite;
				
				JLabel attackLabel = new JLabel();
				attackLabel.setSize(new Dimension((int)(300), (int)(300)));
				Window.scaleComponent(attackLabel);
				
				int attackOffset = (attackLabel.getWidth() - targetSprite.getWidth())/2;
				Point attackLocation = new Point(
						targetSprite.getX() - attackOffset,
						targetSprite.getY() - Window.scaleInt(15)
						);
				attackLabel.setLocation(attackLocation);
				
				GraphicAnimation attackGraphic = new GraphicAnimation(attackLabel, 50, this.uniqueIndex[0], 0, 2);
				attackGraphic.setLooped(true);
				attackGraphic.setLoopStartIndex(46);
				
				Runnable addAttackLabel = () -> {
					CombatInterface.layerOnePane.add(attackLabel, JLayeredPane.MODAL_LAYER);
				};
				attackGraphic.keyframes[0] = new Keyframe(addAttackLabel);
				
				
				Runnable removeAttackLabel = () -> {
					CombatInterface.layerOnePane.remove(attackLabel);
				};
				
				AnimationPlayerModule.addAnimation(attackGraphic);
			}
		}
	}
	
	
	public static class ChargeDarkSoul extends Move {

		public ChargeDarkSoul(CombatEntity parent) {
			super("Dark Soul", new boolean[]{true, false, false}, parent);
		
			this.setDamage(0);
			this.setDescription("Charges the Dark Soul attack");
			
			this.preLoadAnimations();
		}
		
		@Override
		public void useMove(CombatEntity target) {
			Samoht samoht = (Samoht) this.getParent();
			samoht.chargingDarkSoul = true;
			
			runAnimation(target);
		}
		
		@Override
		protected void preLoadAnimations() {
			this.uniqueIndex = new int[] {AnimationsPreloader.loadImages("Resources/Animations/DarkSoulChargeAttack", new Dimension(600, 600), !this.getParent().flipImages)};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity blank) {	
			JLabel attackLabel = new JLabel();
			attackLabel.setSize(new Dimension(600, 600));
			Window.scaleComponent(attackLabel);
			
			int offset = (attackLabel.getWidth() - this.getParent().sprite.getWidth())/2;
			Point labelLocation = new Point(
					this.getParent().sprite.getX() - offset,
					this.getParent().sprite.getY() - Window.scaleInt(500)
					);
			attackLabel.setLocation(labelLocation);
			
			
			GraphicAnimation attackGraphics = new GraphicAnimation(attackLabel, 45, this.uniqueIndex[0], 0, 5);
			attackGraphics.setLooped(true);
			attackGraphics.setLoopStartIndex(25);
			
			
			Runnable addPortalLabel = () -> {
				CombatInterface.layerOnePane.add(attackLabel, JLayeredPane.MODAL_LAYER);
			};
			attackGraphics.keyframes[0] = new Keyframe(addPortalLabel);
			
			AnimationPlayerModule.addAnimation(attackGraphics);
			
			Samoht samoht = (Samoht) this.getParent();
			samoht.darkSoulLabel = attackLabel;
			samoht.darkSoulCharge = attackGraphics;
		}
	}
	
	
	public static class DarkSoul extends Move {

		public DarkSoul(CombatEntity parent) {
			super("Dark Soul", new boolean[]{true, false, false}, parent);
		
			this.setDamage(800);
			this.setDescription("Kills everybody basically");
			
			this.preLoadAnimations();
		}
		
		@Override
		public void useMove(CombatEntity target) {
			CombatEntity[] enemies = Combat.currentCombatInstance.notCurrentTeam.members;
			
			Samoht samoht = (Samoht) this.getParent();
			samoht.chargingDarkSoul = false;
			samoht.usedDarkSoul = true;
			
			for (int i = 0; i < enemies.length; i++) {
				enemies[i].recieveDamage(this.getDamage());
			}
			
			runAnimation(target);
		}
		
		@Override
		protected void preLoadAnimations() {
			//this.uniqueIndex = new int[] {AnimationsPreloader.loadImages(};
		}
		
		
		@Override
		protected void runAnimation(CombatEntity blank) {
			Samoht samoht = (Samoht) this.getParent();
			Point targetLocation = new Point(400, 500);
			targetLocation = Window.scalePoint(targetLocation);
			
			MovementAnimation movement = new MovementAnimation(samoht.darkSoulLabel, 24, "easeOutQuart", targetLocation, null);
			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(movement);
			
			int[] startTimes = new int[1 + Combat.currentCombatInstance.notCurrentTeam.members.length];
			startTimes[0] = 0;
			int index = 1;
			
			for (CombatEntity target : Combat.currentCombatInstance.notCurrentTeam.members) {	
				if (target.dead == true) {
					continue;
				}
				JLabel targetSprite = target.sprite;
				
				JLabel portalLabel = new JLabel();
				portalLabel.setSize(new Dimension((int)(400), (int)(400)));
				Window.scaleComponent(portalLabel);
				
				int portalOffset = (portalLabel.getWidth() - targetSprite.getWidth())/2;
				Point portalLocation = new Point(
						targetSprite.getX() - portalOffset,
						targetSprite.getY() - Window.scaleInt(350)
						);
				portalLabel.setLocation(portalLocation);
				
				
				Runnable shakeAnimation = () -> {
					AnimationPlayerModule.shakeAnimation(target);
					target.updateHealthBar();
				};				
				
				Animation shake = new Animation(2, "linear");
				shake.keyframes[0] = new Keyframe(shakeAnimation);
				
				animationsList.add(shake);
				startTimes[index] = 20;
				index++;
			}
			
			Animation finalAnimation = new CombinedAnimation(24, animationsList, startTimes);
			
			Runnable removeDarkSoul = () -> {
				samoht.darkSoulCharge.setLooped(false);
				samoht.darkSoulCharge.stop();
			};
			
			Runnable removeDarkSoulLabel = () -> {
				CombatInterface.layerOnePane.remove(samoht.darkSoulLabel);
			};
			
			finalAnimation.keyframes[22] = new Keyframe(removeDarkSoul);
			finalAnimation.keyframes[23] = new Keyframe(removeDarkSoulLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
