package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

class MarkedCells {
	private final ImmutableSet<Cell> marked;
	private final ImmutableSet<Cell> unsure;

	public static MarkedCells empty() {
		return new MarkedCells(ImmutableSet.<Cell> of(),
				ImmutableSet.<Cell> of());
	}

	private MarkedCells(final ImmutableSet<Cell> marked,
			final ImmutableSet<Cell> unsure) {
		this.marked = marked;
		this.unsure = unsure;
	}

	public MarkedCells mark(final Cell cell) {
		if (marked.contains(cell)) {
			ImmutableSet<Cell> marked = withoutCell(this.marked, cell);
			ImmutableSet<Cell> unsure = withCell(this.unsure, cell);
			return new MarkedCells(marked, unsure);
		}

		if (unsure.contains(cell)) {
			ImmutableSet<Cell> unsure = withoutCell(this.unsure, cell);
			return new MarkedCells(marked, unsure);
		}

		ImmutableSet<Cell> marked = withCell(this.marked, cell);
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

	private static ImmutableSet<Cell> withCell(final ImmutableSet<Cell> cells,
			final Cell cell) {
		return ImmutableSet.<Cell> builder().addAll(cells).add(cell).build();
	}

	private static ImmutableSet<Cell> withoutCell(
			final ImmutableSet<Cell> cells, final Cell cell) {
		return ImmutableSet.copyOf(Collections2.filter(cells,
				Predicates.not(Predicates.equalTo(cell))));
	}
}