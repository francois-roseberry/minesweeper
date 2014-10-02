package minesweeper.form;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import util.ImageUtil;

/**
 * La classe SmileyButton représente la bouton avec un
 * "Bonhomme Sourire" qui permet de recommencer la partie.
 * 
 * @author David Maltais
 * @author François Roseberry
 *
 */
public class SmileyButton extends JButton
{
	private ImageIcon iconSmile = ImageUtil.getImageIcon(this, "smiley_happy.gif");
	private ImageIcon iconWon = ImageUtil.getImageIcon(this, "smiley_won.gif");
	private ImageIcon iconLost = ImageUtil.getImageIcon(this, "smiley_lost.gif");
	private ImageIcon iconHo = ImageUtil.getImageIcon(this, "smiley_ho.gif");
	
	public SmileyButton()
	{
		super();
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setPreferredSize(new Dimension(36, 36));
		this.setMaximumSize(this.getPreferredSize());
		this.reset();
	}
	
	public void indicateGameWon()
	{
		this.setIcon(this.iconWon);
	}
	
	public void indicateGameLost()
	{
		this.setIcon(this.iconLost);
	}
	
	public void indicateMousePressed()
	{
		this.setIcon(this.iconHo);
	}
	
	public void indicateMouseReleased()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.setIcon(this.iconSmile);
	}
}
