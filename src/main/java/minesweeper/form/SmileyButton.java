package minesweeper.form;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import util.ImageUtil;

/**
 * La classe SmileyButton repr�sente la bouton avec un "Bonhomme Sourire" qui permet de recommencer la partie.
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
@SuppressWarnings("serial")
public class SmileyButton extends JButton {

	private ImageIcon iconSmile = ImageUtil.getImageIcon(getClass(), "smiley_happy.gif");
	private ImageIcon iconWon = ImageUtil.getImageIcon(getClass(), "smiley_won.gif");
	private ImageIcon iconLost = ImageUtil.getImageIcon(getClass(), "smiley_lost.gif");
	private ImageIcon iconHo = ImageUtil.getImageIcon(getClass(), "smiley_ho.gif");

	public SmileyButton() {
		super();

		setBorder(BorderFactory.createRaisedBevelBorder());
		setPreferredSize(new Dimension(36, 36));
		setMaximumSize(this.getPreferredSize());
		reset();
	}

	public void indicateGameWon() {
		setIcon(iconWon);
	}

	public void indicateGameLost() {
		setIcon(iconLost);
	}

	public void indicateMousePressed() {
		setIcon(iconHo);
	}

	public void indicateMouseReleased() {
		reset();
	}

	public void reset() {
		setIcon(iconSmile);
	}
}
