package minesweeper.model.grid;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import minesweeper.model.Cell;
import minesweeper.model.CellState;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class InGameGridTest {

	private static final Cell CELL_1_1 = new Cell(1, 1);
	private static final Cell CELL_2_2 = new Cell(2, 2);
	private static final MarkedCells EMPTY_MARKED_CELLS = MarkedCells.empty();

	@Test(expected = NullPointerException.class)
	public void creatingWithNullMineListShouldThrowException() {
		InGameGrid.create(null, EMPTY_MARKED_CELLS);
	}

	@Test(expected = NullPointerException.class)
	public void creatingWithNullMarkedCellsShouldThrowException() {
		InGameGrid.create(ImmutableSet.<Cell> of(), null);
	}

	@Test
	public void revealingMineShouldReturnHitGrid() {
		Grid grid = InGameGrid.create(ImmutableSet.of(CELL_1_1),
				EMPTY_MARKED_CELLS).reveal(CELL_1_1);

		assertTrue(grid instanceof HitGrid);
	}

	@Test
	public void afterRevealingMineThenResultingGridShouldHaveCellHit() {
		Grid grid = InGameGrid.create(ImmutableSet.of(CELL_1_1),
				EMPTY_MARKED_CELLS).reveal(CELL_1_1);

		assertEquals(CellState.HIT, grid.at(CELL_1_1));
	}

	@Test
	public void revealingCellShouldLeaveMinesHidden() {
		Grid grid = InGameGrid.create(ImmutableSet.of(CELL_2_2),
				EMPTY_MARKED_CELLS).reveal(CELL_1_1);

		assertEquals(CellState.HIDDEN, grid.at(CELL_2_2));
	}

	@Test
	public void revealingCellShouldRevealImmediateNeighboorsIfTheyAreNotMined() {
		Grid grid = InGameGrid.create(ImmutableSet.<Cell> of(),
				EMPTY_MARKED_CELLS).reveal(CELL_1_1);

		assertEquals(CellState.REVEALED, grid.at(new Cell(1, 2)));
		assertEquals(CellState.REVEALED, grid.at(CELL_2_2));
	}

	@Ignore
	@Test
	public void revealingCellShouldRevealNonImmediateNeighboorsIfPathLeadingToThemIsNotMined() {
		Grid grid = InGameGrid.create(ImmutableSet.<Cell> of(),
				EMPTY_MARKED_CELLS).reveal(CELL_1_1);

		assertEquals(CellState.REVEALED, grid.at(new Cell(1, 3)));
	}

	@Test
	public void afterRevealingSecondCellFirstOneStaysRevealed() {
		Grid grid = InGameGrid
				.create(ImmutableSet.<Cell> of(), EMPTY_MARKED_CELLS)
				.reveal(CELL_1_1).reveal(CELL_2_2);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
		assertEquals(CellState.REVEALED, grid.at(CELL_2_2));
	}

	@Test
	public void markingCellShouldReturnInGameGrid() {
		Grid grid = InGameGrid.create(ImmutableSet.<Cell> of(),
				EMPTY_MARKED_CELLS).mark(CELL_1_1);

		assertTrue(grid instanceof InGameGrid);
	}

	@Test
	public void markingCellsShouldCallMarkedCells() {
		MarkedCells markedCells = mock(MarkedCells.class);
		when(markedCells.mark(CELL_1_1)).thenReturn(mock(MarkedCells.class));
		InGameGrid.create(ImmutableSet.<Cell> of(), markedCells).mark(CELL_1_1);

		verify(markedCells).mark(CELL_1_1);
	}
}
