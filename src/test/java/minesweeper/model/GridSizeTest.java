package minesweeper.model;

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
}
