package minesweeper.model.grid;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import minesweeper.model.Cell;
import minesweeper.model.CellState;
import minesweeper.model.MineGenerator;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class BlankGridTest {

	private static final MineGenerator EMPTY_MINE_GENERATOR = emptyMineGenerator();
	private static final Cell CELL_1_1 = new Cell(1, 1);
	private static final ImmutableSet<Cell> SINGLE_CELL_SET = ImmutableSet
			.of(CELL_1_1);

	@Test(expected = NullPointerException.class)
	public void creatingWithNullCellSetShouldThrowException() {
		BlankGrid.create(null, EMPTY_MINE_GENERATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void creatingWithEmptyCellSetShouldThrowException() {
		BlankGrid.create(ImmutableSet.<Cell> of(), EMPTY_MINE_GENERATOR);
	}

	@Test(expected = NullPointerException.class)
	public void creatingWithNullMineGeneratorShouldThrowException() {
		BlankGrid.create(SINGLE_CELL_SET, null);
	}

	@Test
	public void atCreationGridShouldHaveAllCellsHidden() {
		Grid grid = BlankGrid.create(SINGLE_CELL_SET, EMPTY_MINE_GENERATOR);

		assertEquals(CellState.HIDDEN, grid.at(CELL_1_1));
	}

	@Test
	public void afterBeingRevealedThenCellStateShouldBeRevealed() {
		Grid grid = BlankGrid.create(SINGLE_CELL_SET, EMPTY_MINE_GENERATOR)
				.reveal(CELL_1_1);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
	}

	@Test
	public void minesShouldBePlacedAfterFirstCellIsRevealedOnly() {
		MineGenerator generator = emptyMineGenerator();

		Grid grid = BlankGrid.create(SINGLE_CELL_SET, generator);
		verify(generator, never()).getMines(any(ImmutableSet.class));

		grid.reveal(CELL_1_1);
		verify(generator).getMines(any(ImmutableSet.class));
	}

	@Test
	public void afterMarkingShouldReturnBlankGrid() {
		assertTrue(BlankGrid.create(SINGLE_CELL_SET, EMPTY_MINE_GENERATOR)
				.mark(CELL_1_1) instanceof BlankGrid);
	}

	@Test
	public void mineGeneratorShouldReceiveCellListMinusCellToAvoid() {
		MineGenerator generator = emptyMineGenerator();

		BlankGrid.create(ImmutableSet.of(CELL_1_1), generator).reveal(CELL_1_1);
		verify(generator).getMines(ImmutableSet.<Cell> of());
	}

	private static MineGenerator emptyMineGenerator() {
		MineGenerator generator = mock(MineGenerator.class);
		when(generator.getMines(any(ImmutableSet.class))).thenReturn(
				ImmutableSet.<Cell> of());
		return generator;
	}
}
