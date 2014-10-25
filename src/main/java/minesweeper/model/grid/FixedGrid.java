package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;

import com.google.common.base.Preconditions;

public class FixedGrid implements Grid {

	private final Grid grid;

	public FixedGrid(final Grid grid) {
		this.grid = Preconditions.checkNotNull(grid);
	}

	@Override
	public Grid reveal(final Cell cell) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CellState at(final Cell cell) {
		return grid.at(cell);
	}

}
