package minesweeper.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import minesweeper.Loader;
import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.SquareButtonListener;

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
	private int x;
	private int y;
	private int neighboorMinesCount = 0;
	private ImageIcon iconFlag = Loader.getImageIcon("flag.gif");
	private ImageIcon iconQuestion = Loader.getImageIcon("question.gif");
	private boolean wasFlagged = false;

	public static final Dimension SQUARE_SIZE = new Dimension(20, 20);

	public SquareButton(final int x, final int y) {
		super();

		this.x = x;
		this.y = y;

		setPreferredSize(SquareButton.SQUARE_SIZE);
	}

	public int getNeighboorMinesCount() {
		return neighboorMinesCount;
	}

	public void incrementNeighboorMinesCount() {
		if (neighboorMinesCount < 8) {
			neighboorMinesCount++;
		}
	}

	public boolean wasFlagged() {
		return wasFlagged;
	}

	public void setWasFlagged() {
		wasFlagged = true;
	}

	public void reset() {
		state = SquareButtonState.HIDDEN;
		setVisible(true);
		hasMine = false;
		neighboorMinesCount = 0;
		setIcon(null);
		wasFlagged = false;
	}

	public void reveal() {
		state = SquareButtonState.REVEALED;
		setVisible(false);
	}

	public int getXSquare() {
		return this.x;
	}

	public int getYSquare() {
		return this.y;
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
		if (x == this.x && y == this.y) {
			equals = true;
		}
		return equals;
	}

	@Override
	public String toString() {
		return "x = " + this.x + ", y = " + this.y;
	}
}
