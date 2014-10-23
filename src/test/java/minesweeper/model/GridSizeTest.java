package minesweeper.model;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

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
	public void gettingCellsOfSingleCellGridShoulReturnOnlyCell() {
		GridSize size = GridSize.create(1, 1);
		assertEquals(ImmutableList.of(new Cell(1, 1)), size.cells());
	}

	@Test
	public void gettingCellsOfBiggerGridShouldReturnAllCells() {
		GridSize size = GridSize.create(2, 3);
		assertEquals(ImmutableList.of(new Cell(1, 1), new Cell(1, 2), new Cell(1, 3),
				new Cell(2, 1), new Cell(2, 2), new Cell(2, 3)), size.cells());
	}
}
