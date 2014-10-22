package minesweeper.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import minesweeper.model.exception.MineFoundException;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class GridTest {

	private static final Cell CELL_2_2 = new Cell(2, 2);

	private static final MineGenerator EMPTY_MINE_GENERATOR = emptyMineGenerator();

	private static final Cell CELL_1_1 = new Cell(1, 1);

	private static final GridSize SIZE_2X2 = GridSize.create(2, 2);
	private static final GridSize SIZE_1X1 = GridSize.create(1, 1);

	@Test
	public void atCreationGridShouldHaveAllCellsHidden() {
		Grid grid = Grid.create(SIZE_1X1, EMPTY_MINE_GENERATOR);

		assertEquals(CellState.HIDDEN, grid.at(CELL_1_1));
	}

	@Test
	public void revealingAnyCellInEmptyGridShouldReturnNewGridWithCellRevealed()
			throws MineFoundException {
		Grid grid = Grid.create(SIZE_2X2, EMPTY_MINE_GENERATOR)
				.reveal(CELL_1_1);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
	}

	@Test
	public void revealingOneCellShouldLeaveOtherCellsUntouched()
			throws MineFoundException {
		Grid grid = Grid.create(SIZE_2X2, EMPTY_MINE_GENERATOR)
				.reveal(CELL_1_1);

		assertEquals(CellState.HIDDEN, grid.at(CELL_2_2));
	}

	@Test(expected = MineFoundException.class)
	public void revealingMineShouldThrowException() throws MineFoundException {
		MineGenerator generator = mineGenerator(ImmutableList.of(CELL_1_1));
		Grid.create(SIZE_2X2, generator).reveal(CELL_1_1);
	}

	@Test
	public void revealingAllNonMinedCellsShouldReturnWinningGrid()
			throws MineFoundException {
		Grid grid = Grid.create(SIZE_1X1, EMPTY_MINE_GENERATOR);

		assertFalse(grid.isWinning());
		assertTrue(grid.reveal(CELL_1_1).isWinning());
	}

	@Test
	public void afterRevealingSecondCellFirstOneStaysRevealed()
			throws MineFoundException {
		Grid grid = Grid.create(SIZE_2X2, EMPTY_MINE_GENERATOR)
				.reveal(CELL_1_1).reveal(CELL_2_2);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
		assertEquals(CellState.REVEALED, grid.at(CELL_2_2));
	}

	@Test
	public void minesShouldBePlacedAfterFirstCellIsRevealedOnly()
			throws MineFoundException {
		MineGenerator generator = emptyMineGenerator();

		Grid grid = Grid.create(SIZE_2X2, generator);
		verify(generator, never()).getMinedCells();

		grid.reveal(CELL_1_1);
		verify(generator).getMinedCells();
	}

	private static MineGenerator mineGenerator(final ImmutableList<Cell> mines) {
		MineGenerator generator = mock(MineGenerator.class);
		when(generator.getMinedCells()).thenReturn(mines);
		return generator;
	}

	private static MineGenerator emptyMineGenerator() {
		MineGenerator generator = mock(MineGenerator.class);
		when(generator.getMinedCells()).thenReturn(ImmutableList.<Cell> of());
		return generator;
	}
}
