package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;

import com.google.common.base.Preconditions;

public class HitGrid implements Grid {

	private final Cell hit;
	private final Grid grid;

	public HitGrid(final Cell hit, final Grid grid) {
		this.hit = Preconditions.checkNotNull(hit);
		this.grid = Preconditions.checkNotNull(grid);
	}

	@Override
	public CellState at(final Cell cell) {
		if (cell.equals(hit)) {
			return CellState.HIT;
		}

		return grid.at(cell);
	}

	@Override
	public Grid reveal(final Cell cell) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Grid mark(final Cell cell) {
		throw new UnsupportedOperationException();
	}
}
