package minesweeper.model;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;
import minesweeper.model.exception.GameLostException;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class GridTest {

	private static final Cell CELL_2_2 = new Cell(2, 2);

	private static final MineGenerator EMPTY_MINE_GENERATOR = emptyMineGenerator();

	private static final Cell CELL_1_1 = new Cell(1, 1);

	private static final GridSize SIZE_2X2 = new GridSize(2, 2);
	private static final GridSize SIZE_1X1 = new GridSize(1, 1);

	@Test
	public void atCreationGridShouldHaveAllCellsHidden() {
		Grid grid = Grid.create(SIZE_1X1, EMPTY_MINE_GENERATOR);

		assertEquals(CellState.HIDDEN, grid.at(CELL_1_1));
	}

	@Test
	public void revealingAnyCellInEmptyGridShouldReturnNewGridWithCellRevealed() throws GameLostException {
		Grid grid = Grid.create(SIZE_2X2, EMPTY_MINE_GENERATOR).reveal(CELL_1_1);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
	}

	@Test(expected = GameLostException.class)
	public void revealingMineShouldLoseGame() throws GameLostException {
		MineGenerator generator = mineGenerator(ImmutableList.of(CELL_1_1));
		Grid.create(SIZE_2X2, generator).reveal(CELL_1_1);
	}

	@Test
	public void revealingAllNonMinedCellsShouldReturnWinningGrid() throws GameLostException {
		Grid grid = Grid.create(SIZE_1X1, EMPTY_MINE_GENERATOR);

		assertFalse(grid.isWinning());
		assertTrue(grid.reveal(CELL_1_1).isWinning());
	}

	@Test
	public void afterRevealingTwoCellsFirstOneStaysRevealed() throws GameLostException {
		Grid grid = Grid.create(SIZE_2X2, EMPTY_MINE_GENERATOR).reveal(CELL_1_1).reveal(CELL_2_2);

		assertEquals(CellState.REVEALED, grid.at(CELL_1_1));
		assertEquals(CellState.REVEALED, grid.at(CELL_2_2));
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
