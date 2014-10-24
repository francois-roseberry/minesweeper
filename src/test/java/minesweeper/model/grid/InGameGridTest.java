package minesweeper.model.grid;

import static junit.framework.Assert.*;
import minesweeper.model.Cell;
import minesweeper.model.CellState;
import minesweeper.model.Grid;
import minesweeper.model.exception.MineFoundException;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class InGameGridTest {

	private static final Cell CELL_1_1 = new Cell(1, 1);
	private static final Cell CELL_2_2 = new Cell(2, 2);

	@Test(expected = MineFoundException.class)
	public void revealingMineShouldThrowException() throws MineFoundException {
		new InGameGrid(ImmutableList.of(CELL_1_1), ImmutableList.<Cell> of()).reveal(CELL_1_1);
	}

	@Test
	public void revealingOneCellShouldLeaveOtherCellsUntouched()
			throws MineFoundException {
		Grid grid = new InGameGrid(ImmutableList.<Cell> of(), ImmutableList.<Cell> of())
				.reveal(CELL_1_1);

		assertEquals(CellState.HIDDEN, grid.at(CELL_2_2));
	}

	@Test
	public void afterRevealingSecondCellFirstOneStaysRevealed()
			throws MineFoundException {
		Grid grid = new InGameGrid(ImmutableList.<Cell> of(), ImmutableList.<Cell> of())
				.reveal(CELL_1_1).reveal(CELL_2_2);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
		assertEquals(CellState.REVEALED, grid.at(CELL_2_2));
	}
}
