package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

public class InGameGrid implements Grid {

	private final ImmutableSet<Cell> mines;
	private final ImmutableSet<Cell> revealed;
	private final MarkedCells marked;

	public InGameGrid(final ImmutableSet<Cell> mines,
			final ImmutableSet<Cell> revealed, final MarkedCells marked) {
		this.mines = Preconditions.checkNotNull(mines);
		this.revealed = Preconditions.checkNotNull(revealed);
		this.marked = Preconditions.checkNotNull(marked);
	}

	@Override
	public CellState at(final Cell cell) {
		if (isRevealed(cell)) {
			return CellState.REVEALED;
		}

		return marked.at(cell);
	}

	@Override
	public Grid reveal(final Cell cell) {
		if (isMined(cell)) {
			return new HitGrid(cell, this);
		}

		return new InGameGrid(mines, newRevealedCells(cell), marked);
	}

	@Override
	public Grid mark(final Cell cell) {
		return new InGameGrid(mines, revealed, marked.mark(cell));
	}

	private ImmutableSet<Cell> newRevealedCells(final Cell cell) {
		ImmutableSet.Builder<Cell> builder = ImmutableSet.builder();
		builder.addAll(revealed);
		builder.add(cell);
		for (Cell neighboor : cell.neighboors()) {
			if (!mines.contains(neighboor)) {
				builder.add(neighboor);
			}
		}
		return builder.build();
	}

	private boolean isRevealed(final Cell cell) {
		return revealed.contains(cell);
	}

	private boolean isMined(final Cell cell) {
		return mines.contains(cell);
	}
}
