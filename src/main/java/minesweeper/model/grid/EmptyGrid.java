package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;
import minesweeper.model.Grid;
import minesweeper.model.MineGenerator;
import minesweeper.model.exception.MineFoundException;

import com.google.common.collect.ImmutableList;

public class EmptyGrid implements Grid {

	private final MineGenerator generator;

	public EmptyGrid(final MineGenerator generator) {
		this.generator = generator;
	}

	@Override
	public CellState at(final Cell cell) {
		return CellState.HIDDEN;
	}

	@Override
	public Grid reveal(final Cell cell) throws MineFoundException {
		return new InGameGrid(generator.getMines(cell), ImmutableList.<Cell> of()).reveal(cell);
	}
}
