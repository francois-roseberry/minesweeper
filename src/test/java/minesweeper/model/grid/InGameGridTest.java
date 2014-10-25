package minesweeper.model.grid;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import minesweeper.model.Cell;
import minesweeper.model.CellState;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class InGameGridTest {

	private static final Cell CELL_1_1 = new Cell(1, 1);
	private static final Cell CELL_2_2 = new Cell(2, 2);

	@Test(expected = NullPointerException.class)
	public void creatingWithNullMineListShouldThrowException() {
		new InGameGrid(null, ImmutableList.<Cell> of());
	}

	@Test(expected = NullPointerException.class)
	public void creatingWithNullRevealedListShouldThrowException() {
		new InGameGrid(ImmutableList.<Cell> of(), null);
	}

	@Test
	public void revealingMineShouldReturnFixedGrid() {
		Grid grid = new InGameGrid(ImmutableList.of(CELL_1_1),
				ImmutableList.<Cell> of()).reveal(CELL_1_1);

		assertTrue(grid instanceof FixedGrid);
	}

	@Test
	public void revealingOneCellShouldLeaveOtherCellsUntouched() {
		Grid grid = new InGameGrid(ImmutableList.<Cell> of(),
				ImmutableList.<Cell> of()).reveal(CELL_1_1);

		assertEquals(CellState.HIDDEN, grid.at(CELL_2_2));
	}

	@Test
	public void afterRevealingSecondCellFirstOneStaysRevealed() {
		Grid grid = new InGameGrid(ImmutableList.<Cell> of(),
				ImmutableList.<Cell> of()).reveal(CELL_1_1).reveal(CELL_2_2);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
		assertEquals(CellState.REVEALED, grid.at(CELL_2_2));
	}
}
