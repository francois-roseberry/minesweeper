package minesweeper.model.grid;

import static junit.framework.Assert.assertEquals;
import minesweeper.model.Cell;
import minesweeper.model.CellState;

import org.junit.Test;

public class MarkedCellTest {
	private static final Cell CELL = new Cell(1, 1);

	@Test
	public void atCreationThenCellShouldBeHidden() {
		CellState state = MarkedCells.empty().at(CELL);

		assertEquals(CellState.HIDDEN, state);
	}

	@Test
	public void afterMarkingCellThenStateShouldBeMarked() {
		CellState state = MarkedCells.empty().mark(CELL)
				.at(CELL);

		assertEquals(CellState.MARKED, state);
	}

	@Test
	public void afterMarkingCellTwiceThenStateShouldBeUnsure() {
		CellState state = MarkedCells.empty().mark(CELL)
				.mark(CELL).at(CELL);

		assertEquals(CellState.UNSURE, state);
	}

	@Test
	public void afterMarkingCellThriceThenStateShouldBeHiddenAgain() {
		CellState state = MarkedCells.empty().mark(CELL)
				.mark(CELL).mark(CELL).at(CELL);

		assertEquals(CellState.HIDDEN, state);
	}
}
