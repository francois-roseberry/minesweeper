package minesweeper.model.grid;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import minesweeper.model.Cell;
import minesweeper.model.CellState;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class InGameGridTest {

	private static final Cell CELL_1_1 = new Cell(1, 1);
	private static final Cell CELL_2_2 = new Cell(2, 2);
	private static final MarkedCells EMPTY_MARKED_CELLS = MarkedCells.empty();

	@Test(expected = NullPointerException.class)
	public void creatingWithNullMineListShouldThrowException() {
		new InGameGrid(null, ImmutableList.<Cell> of(), EMPTY_MARKED_CELLS);
	}

	@Test(expected = NullPointerException.class)
	public void creatingWithNullRevealedListShouldThrowException() {
		new InGameGrid(ImmutableList.<Cell> of(), null, EMPTY_MARKED_CELLS);
	}

	@Test(expected = NullPointerException.class)
	public void creatingWithNullMarkedCellsShouldThrowException() {
		new InGameGrid(ImmutableList.<Cell> of(), ImmutableList.<Cell> of(),
				null);
	}

	@Test
	public void revealingMineShouldReturnFixedGrid() {
		Grid grid = new InGameGrid(ImmutableList.of(CELL_1_1),
				ImmutableList.<Cell> of(), EMPTY_MARKED_CELLS).reveal(CELL_1_1);

		assertTrue(grid instanceof FixedGrid);
	}

	@Test
	public void revealingOneCellShouldLeaveMinesHidden() {
		Grid grid = new InGameGrid(ImmutableList.of(CELL_2_2),
				ImmutableList.<Cell> of(), EMPTY_MARKED_CELLS).reveal(CELL_1_1);

		assertEquals(CellState.HIDDEN, grid.at(CELL_2_2));
	}

	@Test
	public void revealingOneCellShouldRevealNeighboorsIfTheyAreNotMined() {
		Grid grid = new InGameGrid(ImmutableList.<Cell> of(),
				ImmutableList.<Cell> of(), EMPTY_MARKED_CELLS).reveal(CELL_1_1);

		assertEquals(CellState.REVEALED, grid.at(CELL_2_2));
	}

	@Test
	public void afterRevealingSecondCellFirstOneStaysRevealed() {
		Grid grid = new InGameGrid(ImmutableList.<Cell> of(),
				ImmutableList.<Cell> of(), EMPTY_MARKED_CELLS).reveal(CELL_1_1)
				.reveal(CELL_2_2);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
		assertEquals(CellState.REVEALED, grid.at(CELL_2_2));
	}

	@Test
	public void markingCellShouldReturnInGameGrid() {
		Grid grid = new InGameGrid(ImmutableList.<Cell> of(),
				ImmutableList.<Cell> of(), EMPTY_MARKED_CELLS).mark(CELL_1_1);

		assertTrue(grid instanceof InGameGrid);
	}

	@Test
	public void markingCellsShouldCallMarkedCells() {
		MarkedCells markedCells = mock(MarkedCells.class);
		when(markedCells.mark(CELL_1_1)).thenReturn(mock(MarkedCells.class));
		new InGameGrid(ImmutableList.<Cell> of(), ImmutableList.<Cell> of(),
				markedCells).mark(CELL_1_1);

		verify(markedCells).mark(CELL_1_1);
	}
}
