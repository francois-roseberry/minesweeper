package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;
import minesweeper.model.Grid;
import minesweeper.model.MineGenerator;
import minesweeper.model.exception.MineFoundException;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class BlankGrid implements Grid {

	private final MineGenerator generator;

	public BlankGrid(final MineGenerator generator) {
		this.generator = Preconditions.checkNotNull(generator);
	}

	@Override
	public CellState at(final Cell cell) {
		return CellState.HIDDEN;
	}

	@Override
	public Grid reveal(final Cell cell) throws MineFoundException {
		return withMines(cell).reveal(cell);
	}

	private InGameGrid withMines(final Cell cell) {
		return new InGameGrid(generator.getMines(cell), ImmutableList.<Cell> of());
	}
}
