package minesweeper.model;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class MineGeneratorTest {

	@Test
	public void placingZeroMinesShouldReturnEmptyList() {
		MineGenerator generator = new MineGenerator(0, ImmutableSet.of(
				new Cell(1, 1), new Cell(1, 2)));

		assertTrue(generator.getMines(new Cell(1, 1)).isEmpty());
	}

	@Test
	public void neverPlacesMineOnCellToAvoid() {
		MineGenerator generator = new MineGenerator(1, ImmutableSet.of(
				new Cell(1, 1), new Cell(1, 2)));

		Cell cellToAvoid = new Cell(1, 1);
		assertFalse(generator.getMines(cellToAvoid).contains(cellToAvoid));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryingToPlaceMoreMinesThanAvailableCellsShouldThrowException() {
		Cell cellToAvoid = new Cell(1, 1);
		MineGenerator generator = new MineGenerator(1,
				ImmutableSet.of(cellToAvoid));

		generator.getMines(cellToAvoid);
	}
}
