package minesweeper.model;

import com.google.common.base.Preconditions;

public class GridSize {

	private final int rows;
	private final int columns;

	public static GridSize create(final int rows, final int columns) {
		Preconditions.checkArgument(rows > 0);
		Preconditions.checkArgument(columns > 0);
		return new GridSize(rows, columns);
	}

	private GridSize(final int rows, final int columns) {
		this.rows = rows;
		this.columns = columns;
	}

	public int cellCount() {
		return rows * columns;
	}

	public int rows() {
		return rows;
	}

	public int columns() {
		return columns;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columns;
		result = prime * result + rows;
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
		GridSize other = (GridSize) obj;
		if (columns != other.columns) {
			return false;
		}
		if (rows != other.rows) {
			return false;
		}
		return true;
	}
}
