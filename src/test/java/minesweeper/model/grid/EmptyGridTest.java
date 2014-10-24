package minesweeper.model.grid;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import minesweeper.model.Cell;
import minesweeper.model.CellState;
import minesweeper.model.Grid;
import minesweeper.model.MineGenerator;
import minesweeper.model.exception.MineFoundException;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class EmptyGridTest {

	private static final MineGenerator EMPTY_MINE_GENERATOR = emptyMineGenerator();

	private static final Cell CELL_1_1 = new Cell(1, 1);

	@Test(expected = NullPointerException.class)
	public void creatingWithNullMineGeneratorShouldThrowException() {
		new EmptyGrid(null);
	}

	@Test
	public void atCreationGridShouldHaveAllCellsHidden() {
		Grid grid = new EmptyGrid(EMPTY_MINE_GENERATOR);

		assertEquals(CellState.HIDDEN, grid.at(CELL_1_1));
	}

	@Test
	public void revealingAnyCellShouldReturnNewGridWithCellRevealed()
			throws MineFoundException {
		Grid grid = new EmptyGrid(EMPTY_MINE_GENERATOR).reveal(CELL_1_1);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
	}

	@Test
	public void minesShouldBePlacedAfterFirstCellIsRevealedOnly()
			throws MineFoundException {
		MineGenerator generator = emptyMineGenerator();

		EmptyGrid grid = new EmptyGrid(generator);
		verify(generator, never()).getMines(any(Cell.class));

		grid.reveal(CELL_1_1);
		verify(generator).getMines(any(Cell.class));
	}

	private static MineGenerator emptyMineGenerator() {
		MineGenerator generator = mock(MineGenerator.class);
		when(generator.getMines(any(Cell.class))).thenReturn(ImmutableList.<Cell> of());
		return generator;
	}
}
