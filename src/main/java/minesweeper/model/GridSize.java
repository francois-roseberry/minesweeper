package minesweeper.model;

public class GridSize {

	private final int rows;
	private final int columns;

	public GridSize(final int rows, final int columns) {
		this.rows = rows;
		this.columns = columns;
	}

	public int cellCount() {
		return rows * columns;
	}
}
