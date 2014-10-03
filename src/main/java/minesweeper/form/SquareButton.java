package minesweeper.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.SquareButtonListener;
import util.ImageUtil;

/**
 * La classe SquareButton repr�sente une case de la grille de jeu.
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
@SuppressWarnings("serial")
public class SquareButton extends JButton {

	// �tat du bouton.
	private SquareButtonState state = SquareButtonState.HIDDEN;

	private List<SquareButtonListener> listeners = new ArrayList<SquareButtonListener>();
	private boolean hasMine = false;
	private int xSquare;
	private int ySquare;
	private int neighboorMinesCount = 0;
	private ImageIcon iconFlag = ImageUtil.getImageIcon(getClass(), "flag.gif");
	private ImageIcon iconQuestion = ImageUtil.getImageIcon(getClass(), "question.gif");
	private boolean wasFlagged = false;

	public static final Dimension SQUARE_SIZE = new Dimension(20, 20);

	public SquareButton(final int x, final int y) {
		super();

		this.setPreferredSize(SquareButton.SQUARE_SIZE);
		this.xSquare = x;
		this.ySquare = y;
	}

	public int getNeighboorMinesCount() {
		return this.neighboorMinesCount;
	}

	public void incrementNeighboorMinesCount() {
		if (this.neighboorMinesCount < 8) {
			this.neighboorMinesCount++;
		}
	}

	public boolean wasFlagged() {
		return this.wasFlagged;
	}

	public void setWasFlagged() {
		this.wasFlagged = true;
	}

	public void reset() {
		this.state = SquareButtonState.HIDDEN;
		this.setVisible(true);
		this.hasMine = false;
		this.neighboorMinesCount = 0;
		this.setIcon(null);
		this.wasFlagged = false;
	}

	public void reveal() {
		this.state = SquareButtonState.REVEALED;
		this.setVisible(false);
	}

	public int getXSquare() {
		return this.xSquare;
	}

	public int getYSquare() {
		return this.ySquare;
	}

	public SquareButtonState getState() {
		return this.state;
	}

	public boolean isMined() {
		return this.hasMine;
	}

	public void setMine() {
		this.hasMine = true;
	}

	public void cheat() {
		if (this.hasMine) {
			this.state = SquareButtonState.CHEATED;
			this.setVisible(false);
		}
	}

	public void rightClick() {
		if (this.state == SquareButtonState.HIDDEN) {
			this.setIcon(this.iconFlag);
			this.state = SquareButtonState.MARKED;
			this.onSquareMarked(new GameEvent());
		} else if (state == SquareButtonState.MARKED) {
			this.setIcon(this.iconQuestion);
			this.state = SquareButtonState.UNSURE;
			this.onSquareUnmarked(new GameEvent());
		} else if (state == SquareButtonState.UNSURE) {
			this.setIcon(null);
			this.state = SquareButtonState.HIDDEN;
		}
	}

	private void onSquareMarked(final GameEvent e) {
		for (SquareButtonListener listener : this.listeners) {
			listener.squareMarked(e);
		}
	}

	private void onSquareUnmarked(final GameEvent e) {
		for (SquareButtonListener listener : this.listeners) {
			listener.squareUnmarked(e);
		}
	}

	public void addSquareButtonListener(final SquareButtonListener listener) {
		this.listeners.add(listener);
	}

	public boolean equalCoords(final SquareButton btn) {
		boolean equals = false;
		if (btn != null) {
			if (btn.getXSquare() == this.getXSquare() && btn.getYSquare() == this.getYSquare()) {
				equals = true;
			}
		}
		return equals;
	}

	public boolean equalCoords(final int x, final int y) {
		boolean equals = false;
		if (x == this.xSquare && y == this.ySquare) {
			equals = true;
		}
		return equals;
	}

	@Override
	public String toString() {
		return "x = " + this.xSquare + ", y = " + this.ySquare;
	}
}
