package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;
import minesweeper.model.MineGenerator;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class BlankGrid implements Grid {

	private final MineGenerator generator;
	private final MarkedCells marked;

	public static Grid create(final MineGenerator generator) {
		Preconditions.checkNotNull(generator);

		return new BlankGrid(generator, MarkedCells.empty());
	}

	private BlankGrid(final MineGenerator generator, final MarkedCells marked) {
		this.generator = generator;
		this.marked = marked;
	}

	@Override
	public CellState at(final Cell cell) {
		return marked.at(cell);
	}

	@Override
	public Grid reveal(final Cell cell) {
		return withMines(cell).reveal(cell);
	}

	@Override
	public Grid mark(final Cell cell) {
		return new BlankGrid(generator, marked.mark(cell));
	}

	private InGameGrid withMines(final Cell cell) {
		return new InGameGrid(generator.getMines(cell),
				ImmutableList.<Cell> of(), marked);
	}
}
