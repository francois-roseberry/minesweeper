package minesweeper.model;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class GridSizeTest {

	@Test(expected = IllegalArgumentException.class)
	public void creatingWithZeroOrLessRowsShouldThrowException() {
		GridSize.create(0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void creatingWithZeroOrLessColumnsShouldThrowException() {
		GridSize.create(1, 0);
	}

	@Test
	public void cellCountShouldBeRowTimesColumns() {
		int rows = 3;
		int columns = 4;
		GridSize size = GridSize.create(rows, columns);
		assertEquals(rows * columns, size.cellCount());
	}
}
