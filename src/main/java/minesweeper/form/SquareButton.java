package minesweeper.form;

import java.awt.Dimension;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import minesweeper.Loader;
import minesweeper.model.Cell;
import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.SquareButtonListener;

import com.google.common.base.Preconditions;
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
	private final Cell cell;
	private int neighboorMinesCount = 0;
	private final ImageIcon iconFlag = Loader.getImageIcon("flag.gif");
	private final ImageIcon iconQuestion = Loader.getImageIcon("question.gif");
	private boolean wasFlagged = false;

	public static final Dimension SQUARE_SIZE = new Dimension(20, 20);

	public SquareButton(final Cell cell) {
		super();

		this.cell = cell;

		setPreferredSize(SquareButton.SQUARE_SIZE);
	}

	public int getNeighboorMineCount() {
		return neighboorMinesCount;
	}

	public void setNeighboorMineCount(final int mines) {
		Preconditions.checkArgument(mines >= 0 && mines <= 8);

		neighboorMinesCount = mines;
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

	public Cell getCell() {
		return cell;
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
			setIcon(iconFlag);
			state = SquareButtonState.MARKED;
			onSquareMarked(new GameEvent());
		} else if (state == SquareButtonState.MARKED) {
			setIcon(iconQuestion);
			state = SquareButtonState.UNSURE;
			onSquareUnmarked(new GameEvent());
		} else if (state == SquareButtonState.UNSURE) {
			setIcon(null);
			state = SquareButtonState.HIDDEN;
		}
	}

	private void onSquareMarked(final GameEvent event) {
		for (SquareButtonListener listener : listeners) {
			listener.squareMarked(event);
		}
	}

	private void onSquareUnmarked(final GameEvent event) {
		for (SquareButtonListener listener : listeners) {
			listener.squareUnmarked(event);
		}
	}

	public void addSquareButtonListener(final SquareButtonListener listener) {
		listeners.add(listener);
	}

	public void reset() {
		state = SquareButtonState.HIDDEN;
		setVisible(true);
		hasMine = false;
		neighboorMinesCount = 0;
		setIcon(null);
		wasFlagged = false;
	}
}
