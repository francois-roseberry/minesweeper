package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class InGameGrid implements Grid {

	private final ImmutableList<Cell> mines;
	private final ImmutableList<Cell> revealed;

	public InGameGrid(final ImmutableList<Cell> mines,
			final ImmutableList<Cell> revealed) {
		this.mines = Preconditions.checkNotNull(mines);
		this.revealed = Preconditions.checkNotNull(revealed);
	}

	@Override
	public CellState at(final Cell cell) {
		if (isRevealed(cell)) {
			return CellState.REVEALED;
		}

		return CellState.HIDDEN;
	}

	@Override
	public Grid reveal(final Cell cell) {
		if (isMined(cell)) {
			return new FixedGrid(this);
		}

		return new InGameGrid(mines, newRevealedCells(cell));
	}

	private ImmutableList<Cell> newRevealedCells(final Cell cell) {
		ImmutableList.Builder<Cell> builder = ImmutableList.builder();
		builder.addAll(revealed);
		builder.add(cell);
		// TODO : révéler les voisins, récursivement.
		return builder.build();
	}

	private boolean isRevealed(final Cell cell) {
		return revealed.contains(cell);
	}

	private boolean isMined(final Cell cell) {
		return mines.contains(cell);
	}
}
