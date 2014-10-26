package minesweeper.model;

import com.google.common.collect.ImmutableSet;

public class Cell {

	private final int row;
	private final int column;

	public Cell(final int row, final int column) {
		this.row = row;
		this.column = column;
	}

	public ImmutableSet<Cell> neighboors() {
		ImmutableSet.Builder<Cell> builder = ImmutableSet.builder();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					Cell neighboorCell = new Cell(row + i, column + j);
					builder.add(neighboorCell);
				}
			}
		}
		return builder.build();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + row;
		result = prime * result + column;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Cell other = (Cell) obj;
		if (row != other.row) {
			return false;
		}
		if (column != other.column) {
			return false;
		}
		return true;
	}

	public int row() {
		return row;
	}

	public int column() {
		return column;
	}

	@Override
	public String toString() {
		return "Cell [row=" + row + ", column=" + column + "]";
	}
}
