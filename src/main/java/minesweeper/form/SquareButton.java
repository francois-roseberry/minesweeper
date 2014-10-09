package minesweeper.form;

import java.awt.Dimension;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import minesweeper.Loader;
import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.SquareButtonListener;

import com.google.common.collect.Lists;

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

	private final List<SquareButtonListener> listeners = Lists.newArrayList();
	private boolean hasMine = false;
	private final int x;
	private final int y;
	private int neighboorMinesCount = 0;
	private final ImageIcon iconFlag = Loader.getImageIcon("flag.gif");
	private final ImageIcon iconQuestion = Loader.getImageIcon("question.gif");
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

	public void reveal() {
		state = SquareButtonState.REVEALED;
		setVisible(false);
	}

	public int getXSquare() {
		return x;
	}

	public int getYSquare() {
		return y;
	}

	public SquareButtonState getState() {
		return state;
	}

	public boolean isMined() {
		return hasMine;
	}

	public void setMine() {
		hasMine = true;
	}

	public void cheat() {
		if (hasMine) {
			state = SquareButtonState.CHEATED;
			setVisible(false);
		}
	}

	public void rightClick() {
		if (state == SquareButtonState.HIDDEN) {
			setIcon(this.iconFlag);
			state = SquareButtonState.MARKED;
			onSquareMarked(new GameEvent());
		} else if (state == SquareButtonState.MARKED) {
			setIcon(this.iconQuestion);
			state = SquareButtonState.UNSURE;
			onSquareUnmarked(new GameEvent());
		} else if (state == SquareButtonState.UNSURE) {
			setIcon(null);
			state = SquareButtonState.HIDDEN;
		}
	}

	private void onSquareMarked(final GameEvent e) {
		for (SquareButtonListener listener : listeners) {
			listener.squareMarked(e);
		}
	}

	private void onSquareUnmarked(final GameEvent e) {
		for (SquareButtonListener listener : listeners) {
			listener.squareUnmarked(e);
		}
	}

	public void addSquareButtonListener(final SquareButtonListener listener) {
		listeners.add(listener);
	}

	public boolean equalCoords(final SquareButton button) {
		if (button == null) {
			return false;
		}

		return (button.getXSquare() == x && button.getYSquare() == y);
	}

	public boolean equalCoords(final int x, final int y) {
		return (x == this.x && y == this.y);
	}

	public void reset() {
		state = SquareButtonState.HIDDEN;
		setVisible(true);
		hasMine = false;
		neighboorMinesCount = 0;
		setIcon(null);
		wasFlagged = false;
	}

	@Override
	public String toString() {
		return "x = " + x + ", y = " + y;
	}
}
