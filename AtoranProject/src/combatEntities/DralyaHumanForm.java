package combatEntities;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JLabel;

import combat.CombatEntity;
import combat.Move;
import combatEntities.DralyaDragonForm.DragonSlash;
import combatEntities.DralyaDragonForm.PowerfulDragonSlash;

public class DralyaHumanForm extends CombatEntity {
	public static JLabel getSprite() {
		JLabel atoranSprite = new JLabel("Atoran");
		atoranSprite.setPreferredSize(new Dimension(225, 225));
		atoranSprite.setSize(new Dimension(225, 225));
		atoranSprite.setBackground(new Color(20, 0, 255));
		atoranSprite.setOpaque(false);
		
		return atoranSprite;
	}

	// Atoran entity, the main character
	public DralyaHumanForm(boolean flip) {
		super("Dralya", 250, null, getSprite(), flip);
		
		File targetFile = new File("Resources/Images/DralyaHumanForm.png");
		this.setImageFile(targetFile);
		
		this.flipIfFacingLeft = true;
		
		Move[] moveSet = {new DragonSlash(this), new PowerfulDragonSlash(this)};
		this.setMoveSet(moveSet);
	}	
}
