package minesweeper.model;

import minesweeper.form.GameGrid;

/**
 * La classe SquareButton repr�sente une case de la grille de jeu.
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
public class CustomGridObject {

	private static final int MIN_MINES_PERCENTAGE = 10;
	private static final int MAX_MINES_PERCENTAGE = 70;

	// Nombre de cases dans une rang�e, ou nombre de colonnes.
	private int rows;
	// Nombre de cases dans une colonne, ou nombre de rang�es.
	private int columns;
	private int mines;

	private CustomGridObject(final int rows, final int columns, int mines) {
		int nbSquares = rows * columns;
		int maxMines = (int) (nbSquares * CustomGridObject.MAX_MINES_PERCENTAGE / 100.0f);
		int minMines = (int) (nbSquares * CustomGridObject.MIN_MINES_PERCENTAGE / 100.0f);
		if (mines > maxMines) {
			mines = maxMines;
		} else if (mines < minMines) {
			mines = minMines;
		}
		this.rows = rows;
		this.columns = columns;
		this.mines = mines;
	}

	public static CustomGridObject createCustomGridObject(final int rows, final int columns, final int mines) {
		CustomGridObject c = null;
		if (rows >= GameGrid.MIN_SQUARES_IN_A_LINE && rows <= GameGrid.MAX_SQUARES_IN_A_LINE
				&& columns >= GameGrid.MIN_SQUARES_IN_A_LINE && columns <= GameGrid.MAX_SQUARES_IN_A_LINE
				&& mines >= GameGrid.MIN_MINES && mines <= GameGrid.MAX_MINES) {
			c = new CustomGridObject(rows, columns, mines);
		}
		return c;
	}

	public int getMinesPerRow() {
		return rows;
	}

	public int getMinesPerColumn() {
		return columns;
	}

	public int getNbMines() {
		return mines;
	}
}
