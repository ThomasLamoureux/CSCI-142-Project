package combatEntities;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JLabel;

import combat.Move;
import combatEntities.Slime.BumpMove;

public class RedSlime extends Slime{
	public static JLabel getSprite() {
		JLabel slimeSprite = new JLabel();
		slimeSprite.setPreferredSize(new Dimension(125, 125));
		slimeSprite.setSize(new Dimension(125, 125));
		slimeSprite.setBackground(new Color(20, 0, 255));
		slimeSprite.setOpaque(false);
		
		return slimeSprite;
	}
	
	
	public RedSlime(boolean flip) {
		super(flip);
		this.health = 100;
		this.maxHealth = 100;
		
		File targetFile = new File("Resources/Images/RedSlime.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = false;
		
		Move[] moveSet = {new BumpMove(this)};
		moveSet[0].setDamage(40);
		this.setMoveSet(moveSet);
	}
}
