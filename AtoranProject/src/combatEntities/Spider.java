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
	
	
	@Override
	public void reset() {
		this.health = this.maxHealth;
		this.damageMultiplier = 1.0;
		this.damageResistence = 0.0;
		this.dead = false;
		
		this.sprite = getSprite();
	}
	
	
	public Spider(boolean flip) {
		super("Slime", 120, null, getSprite(), flip);
		
		File targetFile = new File("Resources/Images/Spider.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		Move[] moveSet = {new BumpMove(this)};
		moveSet[0].setDamage(40);
		this.setMoveSet(moveSet);
	}
}
