package minesweeper.model;

import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class MineGeneratorTest {

	@Test
	public void placingZeroMinesShouldReturnEmptyList() {
		ImmutableSet<Cell> mines = new MineGenerator(0).getMines(ImmutableSet
				.of(new Cell(1, 1), new Cell(1, 2)));
		assertTrue(mines.isEmpty());
	}

	@Test
	public void alwaysPlacesMinesOnAvailableCells() {
		ImmutableSet<Cell> cells = ImmutableSet.of(new Cell(1, 1), new Cell(1,
				2));
		ImmutableSet<Cell> mines = new MineGenerator(1).getMines(cells);
		assertTrue(cells.containsAll(mines));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryingToPlaceMoreMinesThanAvailableCellsShouldThrowException() {
		MineGenerator generator = new MineGenerator(1);

		generator.getMines(ImmutableSet.<Cell> of());
	}
}
