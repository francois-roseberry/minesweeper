package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class InGameGrid implements Grid {

	private final ImmutableSet<Cell> mines;
	private final AllCells cells;
	private final MarkedCells marked;

	public static InGameGrid create(final ImmutableSet<Cell> mines,
			final ImmutableSet<Cell> cells, final MarkedCells marked) {
		Preconditions.checkNotNull(mines);
		Preconditions.checkNotNull(cells);
		Preconditions.checkNotNull(marked);

		return new InGameGrid(mines, AllCells.blank(cells), marked);
	}

	private InGameGrid(final ImmutableSet<Cell> mines, final AllCells cells,
			final MarkedCells marked) {
		this.mines = mines;
		this.cells = cells;
		this.marked = marked;
	}

	@Override
	public CellState at(final Cell cell) {
		if (cells.isRevealed(cell)) {
			return CellState.REVEALED;
		}

		return marked.at(cell);
	}

	@Override
	public Grid reveal(final Cell cell) {
		if (isMined(cell)) {
			return new HitGrid(cell, this);
		}

		return new InGameGrid(mines, cells.reveal(cell, mines), marked);
	}

	@Override
	public Grid mark(final Cell cell) {
		return new InGameGrid(mines, cells, marked.mark(cell));
	}

	private boolean isMined(final Cell cell) {
		return mines.contains(cell);
	}

	private static class AllCells {
		private final ImmutableSet<Cell> hidden;
		private final ImmutableSet<Cell> revealed;

		public static AllCells blank(final ImmutableSet<Cell> cells) {
			return new AllCells(cells, ImmutableSet.<Cell> of());
		}

		private AllCells(final ImmutableSet<Cell> hidden,
				final ImmutableSet<Cell> revealed) {
			this.hidden = hidden;
			this.revealed = revealed;
		}

		public AllCells reveal(final Cell cell, final ImmutableSet<Cell> mines) {
			ImmutableSet.Builder<Cell> builder = ImmutableSet.builder();
			builder.add(cell);

			for (Cell neighboor : cell.neighboors()) {
				// 1 - Ne pas révéler une mine
				// 2 - Ne pas révéler une cellule qui ne fait pas partie des
				// cellules cachées
				if (!mines.contains(neighboor) && hidden.contains(neighboor)) {
					builder.add(neighboor);
				}
			}

			ImmutableSet<Cell> newRevealed = builder.build();
			ImmutableSet<Cell> revealed = withCells(this.revealed, newRevealed);
			ImmutableSet<Cell> hidden = withoutCells(this.hidden, newRevealed);
			return new AllCells(hidden, revealed);
		}

		private static ImmutableSet<Cell> withCells(
				final ImmutableSet<Cell> cells, final ImmutableSet<Cell> cells2) {
			return ImmutableSet.copyOf(Sets.union(cells, cells2));
		}

		private static ImmutableSet<Cell> withoutCells(
				final ImmutableSet<Cell> cells,
				final ImmutableSet<Cell> cellsToRemove) {
			return ImmutableSet.copyOf(Collections2.filter(cells,
					Predicates.not(Predicates.in(cellsToRemove))));
		}

		public boolean isRevealed(final Cell cell) {
			return revealed.contains(cell);
		}
	}
}
