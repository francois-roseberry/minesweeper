package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;
import minesweeper.model.MineGenerator;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

public class BlankGrid implements Grid {

	private final MineGenerator generator;
	private final MarkedCells marked;

	public static BlankGrid create(final MineGenerator generator) {
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

	public BlankGrid mark(final Cell cell) {
		return new BlankGrid(generator, marked.mark(cell));
	}

	private InGameGrid withMines(final Cell cell) {
		return new InGameGrid(generator.getMines(cell),
				ImmutableList.<Cell> of());
	}

	private static class MarkedCells {
		private final ImmutableList<Cell> marked;
		private final ImmutableList<Cell> unsure;

		public static MarkedCells empty() {
			return new MarkedCells(ImmutableList.<Cell> of(),
					ImmutableList.<Cell> of());
		}

		public MarkedCells(final ImmutableList<Cell> marked,
				final ImmutableList<Cell> unsure) {
			this.marked = marked;
			this.unsure = unsure;
		}

		public MarkedCells mark(final Cell cell) {
			if (marked.contains(cell)) {
				ImmutableList<Cell> marked = withoutCell(this.marked, cell);
				ImmutableList<Cell> unsure = withCell(this.unsure, cell);
				return new MarkedCells(marked, unsure);
			}

			if (unsure.contains(cell)) {
				ImmutableList<Cell> unsure = withoutCell(this.unsure, cell);
				return new MarkedCells(marked, unsure);
			}

			ImmutableList<Cell> marked = withCell(this.marked, cell);
			return new MarkedCells(marked, unsure);
		}

		public CellState at(final Cell cell) {
			if (marked.contains(cell)) {
				return CellState.MARKED;
			}

			if (unsure.contains(cell)) {
				return CellState.UNSURE;
			}

			return CellState.HIDDEN;
		}

		private static ImmutableList<Cell> withCell(
				final ImmutableList<Cell> cells, final Cell cell) {
			return ImmutableList.<Cell> builder().addAll(cells).add(cell)
					.build();
		}

		private static ImmutableList<Cell> withoutCell(
				final ImmutableList<Cell> cells, final Cell cell) {
			return ImmutableList.copyOf(Collections2.filter(cells,
					Predicates.not(Predicates.equalTo(cell))));
		}
	}
}
