package minesweeper.model;

public class Cell {

	private final int row;
	private final int column;

	public Cell(final int row, final int column) {
		this.row = row;
		this.column = column;
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

	@Override
	public String toString() {
		return "Cell [row=" + row + ", column=" + column + "]";
	}
}
