package minesweeper.model.grid;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import minesweeper.model.Cell;
import minesweeper.model.CellState;

import org.junit.Test;

public class HitGridTest {
	@Test(expected = NullPointerException.class)
	public void creatingWithNullHitCellShouldThrowException() {
		new HitGrid(null, mock(Grid.class));
	}

	@Test(expected = NullPointerException.class)
	public void creatingWithNullGridShouldThrowException() {
		new HitGrid(mock(Cell.class), null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void revealingCellShouldThrowException() {
		new HitGrid(mock(Cell.class), mock(Grid.class))
				.reveal(mock(Cell.class));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void markingCellShouldThrowException() {
		new HitGrid(mock(Cell.class), mock(Grid.class)).mark(mock(Cell.class));
	}

	@Test
	public void gettingHitCellStateShouldReturnHit() {
		Cell cell = mock(Cell.class);
		CellState state = new HitGrid(cell, mock(Grid.class)).at(cell);

		assertEquals(CellState.HIT, state);
	}

	@Test
	public void gettingCellStateOfAnyCellOtherThanHitShouldGetItFromUnderlyingGrid() {
		Cell cell = mock(Cell.class);
		Grid grid = mock(Grid.class);
		when(grid.at(cell)).thenReturn(CellState.HIDDEN);

		CellState state = new HitGrid(mock(Cell.class), grid).at(cell);
		verify(grid).at(cell);

		assertEquals(CellState.HIDDEN, state);
	}
}
