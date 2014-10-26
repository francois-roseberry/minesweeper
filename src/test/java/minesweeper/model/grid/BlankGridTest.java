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

import com.google.common.collect.ImmutableList;

public class BlankGridTest {

	private static final MineGenerator EMPTY_MINE_GENERATOR = emptyMineGenerator();

	private static final Cell CELL_1_1 = new Cell(1, 1);

	@Test(expected = NullPointerException.class)
	public void creatingWithNullMineGeneratorShouldThrowException() {
		BlankGrid.create(null);
	}

	@Test
	public void atCreationGridShouldHaveAllCellsHidden() {
		Grid grid = BlankGrid.create(EMPTY_MINE_GENERATOR);

		assertEquals(CellState.HIDDEN, grid.at(CELL_1_1));
	}

	@Test
	public void afterBeingRevealedThenCellStateShouldBeRevealed() {
		Grid grid = BlankGrid.create(EMPTY_MINE_GENERATOR).reveal(CELL_1_1);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
	}

	@Test
	public void minesShouldBePlacedAfterFirstCellIsRevealedOnly() {
		MineGenerator generator = emptyMineGenerator();

		BlankGrid grid = BlankGrid.create(generator);
		verify(generator, never()).getMines(any(Cell.class));

		grid.reveal(CELL_1_1);
		verify(generator).getMines(any(Cell.class));
	}

	@Test
	public void afterMarkingShouldReturnBlankGrid() {
		assertTrue(BlankGrid.create(EMPTY_MINE_GENERATOR).mark(CELL_1_1) instanceof BlankGrid);
	}

	private static MineGenerator emptyMineGenerator() {
		MineGenerator generator = mock(MineGenerator.class);
		when(generator.getMines(any(Cell.class))).thenReturn(
				ImmutableList.<Cell> of());
		return generator;
	}
}
