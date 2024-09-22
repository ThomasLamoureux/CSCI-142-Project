package combatEntities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import animations.Keyframe;
import animations.Animation.GraphicAnimation;
import combat.CombatEntity;
import combat.CombatInterface;
import combat.Move;
import main.Window;

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
		
		Move[] moveSet = {};
		this.setMoveSet(moveSet);
	}	
	
	
	public static class MagicBullet extends Move {

		public MagicBullet(CombatEntity parent) {
			super("Dragon Slash", false, true, parent);
		
			this.setDamage(60);
			this.setDescription("Targets a single enemy with a slashing attack");
		}
		
	}
}
