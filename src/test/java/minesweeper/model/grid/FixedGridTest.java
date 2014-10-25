package minesweeper.model.grid;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import minesweeper.model.Cell;
import minesweeper.model.CellState;

import org.junit.Test;

public class FixedGridTest {
	@Test(expected = NullPointerException.class)
	public void creatingWithNullGridShouldThrowException() {
		new FixedGrid(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void revealingCellShouldThrowException() {
		new FixedGrid(mock(Grid.class)).reveal(mock(Cell.class));
	}

	@Test
	public void gettingCellStateShouldGetItFromUnderlyingGrid() {
		Cell cell = mock(Cell.class);
		Grid grid = mock(Grid.class);
		when(grid.at(cell)).thenReturn(CellState.HIDDEN);

		CellState state = new FixedGrid(grid).at(cell);
		verify(grid).at(cell);

		assertEquals(CellState.HIDDEN, state);
	}
}
