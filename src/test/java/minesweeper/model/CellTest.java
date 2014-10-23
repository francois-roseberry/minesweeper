package minesweeper.model;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class CellTest {

	@Test
	public void neighboorListShouldContainEightCells() {
		assertEquals(8, new Cell(1, 1).neighboors().size());
	}

	@Test
	public void neighboorListShoudNotContainCellItself() {
		Cell cell = new Cell(1, 1);
		assertFalse(cell.neighboors().contains(cell));
	}

	@Test
	public void neighboorShouldBeImmediate() {
		ImmutableList<Cell> neighboors = new Cell(2, 2).neighboors();

		assertTrue(neighboors.contains(new Cell(1, 1))); // Northwest
		assertTrue(neighboors.contains(new Cell(1, 2))); // North
		assertTrue(neighboors.contains(new Cell(1, 3))); // Northeast
		assertTrue(neighboors.contains(new Cell(2, 3))); // East
		assertTrue(neighboors.contains(new Cell(3, 3))); // Southeast
		assertTrue(neighboors.contains(new Cell(3, 2))); // South
		assertTrue(neighboors.contains(new Cell(3, 1))); // Southwest
		assertTrue(neighboors.contains(new Cell(2, 1))); // West
	}
}
