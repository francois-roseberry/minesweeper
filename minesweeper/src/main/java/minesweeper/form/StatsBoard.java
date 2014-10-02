package minesweeper.form;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * La classe StatsBoard représente la barre d'outils
 * du démineur. Elle contient un panneau d'affichage
 * du compteur de mines, un bouton "Sourire" et un
 * panneau d'affichage du temps écoulé.
 * 
 * @author David Maltais
 * @author François Roseberry
 *
 */
public class StatsBoard extends JPanel implements ActionListener
{
	private LCDPanel lblMines = LCDPanel.createLCDPanel(3);
	private LCDPanel lblTime = LCDPanel.createLCDPanel(3);
	private SmileyButton btnSmiley = new SmileyButton();
	private Timer timer = new Timer(1000, this);
	
	private int secondsElapsed = 0;
	private int mineCount = 0;
	
	public StatsBoard()
	{
		super();
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.initializeComponent();
	}
	
	private void initializeComponent()
	{
		// Cette ligne n'a aucun rapport mais doit être là
		// pour que ça fonctionne
		this.setLayout(new BorderLayout());
		Box vbMain = Box.createVerticalBox();
		Box hbMain = Box.createHorizontalBox();
		
		this.btnSmiley.addActionListener(this);
		
		hbMain.add(Box.createHorizontalStrut(5));
		hbMain.add(this.lblMines);
		hbMain.add(Box.createHorizontalGlue());
		hbMain.add(this.btnSmiley);
		hbMain.add(Box.createHorizontalGlue());
		hbMain.add(this.lblTime);
		hbMain.add(Box.createHorizontalStrut(5));
		
		vbMain.add(Box.createVerticalStrut(5));
		vbMain.add(hbMain);
		vbMain.add(Box.createVerticalStrut(5));
		
		this.add(vbMain);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.timer)
		{
			if (this.secondsElapsed < 999)
			{
				this.secondsElapsed++;
				this.lblTime.displayNumber(this.secondsElapsed);
			}
			else
			{
				this.timer.stop();
			}
		}
		else if (e.getSource() == this.btnSmiley)
		{
			AppFrame.getInstance().startGame();
		}
	}
	
	public void resetTimer()
	{
		if (this.timer.isRunning())
		{
			this.timer.stop();
		}
		this.secondsElapsed = 0;
		this.lblTime.displayNumber(this.secondsElapsed);
	}
	
	public void stopTimer()
	{
		if (this.timer.isRunning())
		{
			this.timer.stop();
		}
	}
	
	public int getTimeElapsed()
	{
		return this.secondsElapsed;
	}
	
	public void startTimer()
	{
		if (!this.timer.isRunning())
		{
			this.timer.start();
		}
	}
	
	public void startGame(int mines)
	{
		this.resetTimer();
		this.btnSmiley.reset();
		this.displayMineCount(mines);
	}
	
	public void indicateGameWon()
	{
		this.stopTimer();
		this.btnSmiley.indicateGameWon();
	}
	
	public void indicateGameLost()
	{
		this.stopTimer();
		this.btnSmiley.indicateGameLost();
	}
	
	public void indicateMousePressed()
	{
		this.btnSmiley.indicateMousePressed();
	}
	
	public void indicateMouseReleased()
	{
		this.btnSmiley.indicateMouseReleased();
	}
	
	public void displayMineCount(int mineCount)
	{
		if (mineCount >= 0)
		{
			this.mineCount = mineCount;
			this.lblMines.displayNumber(this.mineCount);
		}
	}
	
	public void incrementMineCount()
	{
		this.mineCount++;
		if (this.mineCount >= 0)
		{
			this.lblMines.displayNumber(this.mineCount);
		}
	}
	
	public void decrementMineCount()
	{
		this.mineCount--;
		if (this.mineCount >= 0)
		{
			this.lblMines.displayNumber(this.mineCount);
		}
	}
}
