package minesweeper.form;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * La classe StatsBoard repr�sente la barre d'outils du d�mineur. Elle contient un panneau d'affichage du compteur de mines, un bouton
 * "Sourire" et un panneau d'affichage du temps �coul�.
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
@SuppressWarnings("serial")
public class StatsBoard extends JPanel implements ActionListener {

	private LCDPanel lblMines = LCDPanel.createLCDPanel(3);
	private LCDPanel lblTime = LCDPanel.createLCDPanel(3);
	private SmileyButton btnSmiley = new SmileyButton();
	private Timer timer = new Timer(1000, this);

	private int secondsElapsed = 0;
	private int mineCount = 0;

	public StatsBoard() {
		super();

		setBorder(BorderFactory.createLoweredBevelBorder());
		initializeComponent();
	}

	private void initializeComponent() {
		// Cette ligne n'a aucun rapport mais doit �tre l�
		// pour que �a fonctionne
		setLayout(new BorderLayout());
		Box vbMain = Box.createVerticalBox();
		Box hbMain = Box.createHorizontalBox();

		btnSmiley.addActionListener(this);

		hbMain.add(Box.createHorizontalStrut(5));
		hbMain.add(lblMines);
		hbMain.add(Box.createHorizontalGlue());
		hbMain.add(btnSmiley);
		hbMain.add(Box.createHorizontalGlue());
		hbMain.add(lblTime);
		hbMain.add(Box.createHorizontalStrut(5));

		vbMain.add(Box.createVerticalStrut(5));
		vbMain.add(hbMain);
		vbMain.add(Box.createVerticalStrut(5));

		this.add(vbMain);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == timer) {
			if (secondsElapsed < 999) {
				secondsElapsed++;
				lblTime.displayNumber(secondsElapsed);
			} else {
				timer.stop();
			}
		} else if (e.getSource() == btnSmiley) {
			AppFrame.getInstance().startGame();
		}
	}

	public void resetTimer() {
		if (timer.isRunning()) {
			timer.stop();
		}
		secondsElapsed = 0;
		lblTime.displayNumber(this.secondsElapsed);
	}

	public void stopTimer() {
		if (timer.isRunning()) {
			timer.stop();
		}
	}

	public int getTimeElapsed() {
		return secondsElapsed;
	}

	public void startTimer() {
		if (!timer.isRunning()) {
			timer.start();
		}
	}

	public void startGame(final int mines) {
		this.resetTimer();
		this.btnSmiley.reset();
		this.displayMineCount(mines);
	}

	public void indicateGameWon() {
		this.stopTimer();
		this.btnSmiley.indicateGameWon();
	}

	public void indicateGameLost() {
		this.stopTimer();
		this.btnSmiley.indicateGameLost();
	}

	public void indicateMousePressed() {
		this.btnSmiley.indicateMousePressed();
	}

	public void indicateMouseReleased() {
		this.btnSmiley.indicateMouseReleased();
	}

	public void displayMineCount(final int mineCount) {
		if (mineCount >= 0) {
			this.mineCount = mineCount;
			this.lblMines.displayNumber(this.mineCount);
		}
	}

	public void incrementMineCount() {
		this.mineCount++;
		if (this.mineCount >= 0) {
			this.lblMines.displayNumber(this.mineCount);
		}
	}

	public void decrementMineCount() {
		this.mineCount--;
		if (this.mineCount >= 0) {
			this.lblMines.displayNumber(this.mineCount);
		}
	}
}
