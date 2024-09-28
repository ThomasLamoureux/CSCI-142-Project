package combatEntities;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JLabel;

import combat.CombatEntity;
import combat.Move;
import combatEntities.Slime.BumpMove;

public class Spider extends CombatEntity {
	public static JLabel getSprite() {
		JLabel slimeSprite = new JLabel();
		slimeSprite.setPreferredSize(new Dimension(180, 180));
		slimeSprite.setSize(new Dimension(180, 180));
		slimeSprite.setBackground(new Color(20, 0, 255));
		slimeSprite.setOpaque(false);
		
		return slimeSprite;
	}
	
	
	public Spider(boolean flip) {
		super("Slime", 120, null, getSprite(), flip);
		
		File targetFile = new File("Resources/Images/Spider.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		Move[] moveSet = {new BumpMove(this)};
		moveSet[0].setDamage(50);
		this.setMoveSet(moveSet);
	}
}
