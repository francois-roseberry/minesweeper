package minesweeper.model;

import minesweeper.form.GameGrid;

/**
 * La classe SquareButton représente une case de la grille
 * de jeu.
 * 
 * @author David Maltais
 * @author François Roseberry
 *
 */
public class CustomGridObject
{
	private static final int MIN_MINES_PERCENTAGE = 10;
	private static final int MAX_MINES_PERCENTAGE = 70;
	
	// Nombre de cases dans une rangée, ou nombre de colonnes.
	private int rows;
	// Nombre de cases dans une colonne, ou nombre de rangées.
	private int columns;
	private int mines;
	
	private CustomGridObject(int rows, int columns, int mines)
	{
		int nbSquares = rows * columns;
		int maxMines = (int)(nbSquares * CustomGridObject.MAX_MINES_PERCENTAGE / 100.0f);
		int minMines = (int)(nbSquares * CustomGridObject.MIN_MINES_PERCENTAGE / 100.0f);
		if (mines > maxMines)
		{
			mines = maxMines;
		}
		else if (mines < minMines)
		{
			mines = minMines;
		}
		this.rows = rows;
		this.columns = columns;
		this.mines = mines;
	}
	
	public static CustomGridObject createCustomGridObject(int rows, int columns, int mines)
	{
		CustomGridObject c = null;
		if (rows >= GameGrid.MIN_SQUARES_IN_A_LINE && rows <= GameGrid.MAX_SQUARES_IN_A_LINE
				&& columns >= GameGrid.MIN_SQUARES_IN_A_LINE && columns <= GameGrid.MAX_SQUARES_IN_A_LINE
				&& mines >= GameGrid.MIN_MINES && mines <= GameGrid.MAX_MINES)
		{
			c = new CustomGridObject(rows, columns, mines);
		}
		return c;
	}
	
	public int getMinesPerRow()
	{
		return this.rows;
	}
	
	public int getMinesPerColumn()
	{
		return this.columns;
	}
	
	public int getNbMines()
	{
		return this.mines;
	}
}
