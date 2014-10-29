package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;
import minesweeper.model.MineGenerator;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

public class BlankGrid implements Grid {

	private final MineGenerator generator;
	private final ImmutableSet<Cell> cells;
	private final MarkedCells marked;

	public static Grid create(final ImmutableSet<Cell> cells,
			final MineGenerator generator) {
		Preconditions.checkNotNull(cells);
		Preconditions.checkArgument(!cells.isEmpty());
		Preconditions.checkNotNull(generator);

		return new BlankGrid(cells, generator, MarkedCells.empty());
	}

	private BlankGrid(final ImmutableSet<Cell> cells,
			final MineGenerator generator, final MarkedCells marked) {
		this.cells = cells;
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
		return new BlankGrid(cells, generator, marked.mark(cell));
	}

	private InGameGrid withMines(final Cell cell) {
		ImmutableSet<Cell> availableCells = getAvailableCellsForMines(cell);
		return InGameGrid.create(generator.getMines(availableCells), marked);
	}

	private ImmutableSet<Cell> getAvailableCellsForMines(final Cell cellToAvoid) {
		ImmutableSet.Builder<Cell> builder = ImmutableSet.builder();

		// Remplir le tableau des coordonn�es disponibles.
		for (Cell cell : cells) {
			if (!cellToAvoid.equals(cell)) {
				builder.add(cell);
			}
		}
		return builder.build();
	}
}
