package combatEntities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import animations.Animation;
import animations.Keyframe;
import animations.Animation.CombinedAnimation;
import animations.Animation.GraphicAnimation;
import animations.Animation.MovementAnimation;
import combat.CombatEntity;
import combat.CombatInterface;
import combat.Move;
import main.Window;
import utilities.AnimationPlayerModule;

public class Samoht extends CombatEntity {
	public static JLabel getSamohtSprite() {
		JLabel sprite = new JLabel("Dralya");
		sprite.setPreferredSize(new Dimension(250, 250));
		sprite.setSize(new Dimension(250, 250));
		sprite.setOpaque(false);
		
		return sprite;
	}
	
	public Samoht() {
		super("Samoht", 250, null, getSamohtSprite());
		
		File targetFile = new File("Resources/Images/DralyaDragonForm.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		Move[] moveSet = {new MagicBullet(this)};
		this.setMoveSet(moveSet);
	}	
	
	
	public static class MagicBullet extends Move {

		public MagicBullet(CombatEntity parent) {
			super("Dragon Slash", false, true, parent);
		
			this.setDamage(60);
			this.setDescription("Fires a magic bullet at the enemy");
		}
		
		

		@Override
		protected void runAnimation(CombatEntity target) {
			boolean flipImage = false;
			if (this.getParent().facingLeft == -1) {
				flipImage = true;
			}
			
			JLabel sprite = this.getParent().sprite;
			
			
			JLabel portalLabel = new JLabel();
			portalLabel.setSize(new Dimension((int)(300), (int)(300)));
			Window.scaleComponent(portalLabel);
			
			int portalOffset = (portalLabel.getWidth() - target.sprite.getWidth())/2;
			Point portalLocation = new Point(
					target.sprite.getX() - portalOffset + Window.scaleInt(400) * this.getParent().facingLeft,
					target.sprite.getY() - portalOffset
					);
			
			portalLabel.setLocation(portalLocation);		
	
			String portalPath = "Resources/Animations/SideWaysPortal";
			
			GraphicAnimation portalGraphics = new GraphicAnimation(portalLabel, 25, portalPath, 0, 5, flipImage);
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
			
			String bulletPath = "Resources/Animations/MagicBullet";
			
			System.out.println(bulletStartingLocation.y);
			System.out.println(bulletTargetDestination.y);
			
			
			GraphicAnimation bulletGraphics = new GraphicAnimation(bulletLabel, 4, bulletPath, 0, 1, flipImage);
			MovementAnimation bulletMovement = new MovementAnimation(bulletLabel, 12, "easeInQuad", bulletTargetDestination, bulletStartingLocation);
			//bulletGraphics.setLooped(true);
			
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
			//finalAnimation.keyframes[68] = new Keyframe(removeBulletLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
