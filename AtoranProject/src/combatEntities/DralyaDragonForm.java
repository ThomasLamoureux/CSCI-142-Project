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

public class DralyaDragonForm extends CombatEntity {
	
	public static JLabel getDralyaDragonSprite() {
		JLabel sprite = new JLabel("Atoran");
		sprite.setPreferredSize(new Dimension(300, 300));
		sprite.setSize(new Dimension(300, 300));
		sprite.setBackground(new Color(20, 0, 255));
		sprite.setOpaque(false);
		
		return sprite;
	}
	
	public DralyaDragonForm() {
		super("Dralya", 250, null, getDralyaDragonSprite());
		
		File targetFile = new File("Resources/Images/DralyaDragonForm.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		Move[] moveSet = {new DragonSlash(this)};
		this.setMoveSet(moveSet);
	}	
	
	
	
	public static class DragonSlash extends Move {

		public DragonSlash(CombatEntity parent) {
			super("Dragon Slash", false, true, parent);
		
			this.setDamage(100);
			this.setDescription("Targets a single enemy with a slashing attack");
		}
		
		@Override
		protected void runAnimation(CombatEntity target) {
			boolean flipImage = false;
			if (this.getParent().facingLeft == 1) {
				flipImage = true;
			}
			
			JLabel animationLabelOne = new JLabel();
			animationLabelOne.setSize(new Dimension((int)(500), (int)(500)));
			Window.scaleComponent(animationLabelOne);
			
			int offset = (animationLabelOne.getWidth() - target.sprite.getWidth())/2;
			Point animationLocation = new Point(
					target.sprite.getX() - offset,
					target.sprite.getY() - offset
					);

			
			animationLabelOne.setLocation(animationLocation);
			
			
			Point targetDestination = new Point((int)
					(target.sprite.getLocation().x + Window.scaleInt(250) * this.getParent().facingLeft), 
					target.sprite.getLocation().y + target.sprite.getHeight() - this.getParent().sprite.getHeight());
			
			//animationLabel.setLocation(new Point(targetDestination.x - 100 * this.getParent().facingLeft, targetDestination.y - this.getParent().sprite.getHeight()/2));

			String slashPath = "Resources/Animations/DragonSlash";
			
			Animation slashGraphics = new GraphicAnimation(animationLabelOne, 11, slashPath, 0, 1, flipImage);
			
			Runnable removeLabel = () -> {
				CombatInterface.layerOnePane.remove(animationLabelOne);
			};
			
			Runnable addLabel = () -> {
				CombatInterface.layerOnePane.add(animationLabelOne, JLayeredPane.MODAL_LAYER);
			};
			slashGraphics.keyframes[0] = new Keyframe(addLabel);
			
			Runnable shakeAnimation = () -> {
				AnimationPlayerModule.shakeAnimation(target);
				target.updateHealthBar();
			};
			slashGraphics.keyframes[1] = new Keyframe(shakeAnimation);
			
			
			
			
			
			JLabel animationLabelTwo = new JLabel();
			animationLabelTwo.setSize(new Dimension((int)(300), (int)(300)));
			Window.scaleComponent(animationLabelTwo);
			
			int offsetTwo = (animationLabelTwo.getWidth() - this.getParent().sprite.getWidth())/2;
			Point animationLocationTwo = new Point(
					this.getParent().sprite.getX() - offsetTwo,
					this.getParent().sprite.getY() - offsetTwo
					);

			
			animationLabelTwo.setLocation(animationLocationTwo);
			
			
			String TeleportOutPath = "Resources/Animations/DragonTeleportOut";
			
			
			Animation teleportOutAnimation = new GraphicAnimation(animationLabelTwo, 6, TeleportOutPath, 0, 1, flipImage);
			
			Runnable removeLabelTwo = () -> {
				CombatInterface.layerOnePane.remove(animationLabelTwo);
			};
			
			Runnable addLabelTwo = () -> {
				CombatInterface.layerOnePane.add(animationLabelTwo, JLayeredPane.MODAL_LAYER);
			};
			
			
			Runnable setInvisibleAndMove = () -> {
				this.getParent().sprite.setVisible(false);;
				this.getParent().sprite.setLocation(targetDestination);
			};
			
			Runnable setVisible = () -> {
				this.getParent().sprite.setVisible(true);;
			};
			
			teleportOutAnimation.keyframes[0] = new Keyframe(addLabelTwo);
			teleportOutAnimation.keyframes[2] = new Keyframe(setInvisibleAndMove);
			
			
			
			ArrayList<Animation> animationsList = new ArrayList<>();
			animationsList.add(teleportOutAnimation);
			animationsList.add(slashGraphics);
			
			Animation finalAnimation = new CombinedAnimation(82, animationsList, new int[]{0, 20});
			finalAnimation.keyframes[10] = new Keyframe(setVisible);
			finalAnimation.keyframes[6] = new Keyframe(removeLabelTwo);
			finalAnimation.keyframes[32] = new Keyframe(removeLabel);
			
			AnimationPlayerModule.addAnimation(finalAnimation);
		}
	}
}
