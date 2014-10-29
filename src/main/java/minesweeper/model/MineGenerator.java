package minesweeper.model;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class MineGenerator {

	private final int mines;

	public MineGenerator(final int mines) {
		this.mines = mines;
	}

	public ImmutableSet<Cell> getMines(final ImmutableSet<Cell> availableCells) {
		int minesToGenerate = mines;
		List<Cell> openCells = Lists.newArrayList(availableCells);

		ImmutableSet.Builder<Cell> builder = ImmutableSet.builder();
		Random random = new Random();
		while (minesToGenerate > 0) {
			// G�n�rer un index al�atoire.
			Cell chosenCell = pickCellAtRandom(openCells, random);
			builder.add(chosenCell);
			// Enlever la case de la liste.
			openCells.remove(chosenCell);
			minesToGenerate--;
		}
		return builder.build();
	}

	private Cell pickCellAtRandom(final List<Cell> openCells,
			final Random random) {
		if (openCells.size() == 1) {
			return openCells.get(0);
		}

		int openIndex = random.nextInt(openCells.size());
		return openCells.get(openIndex);
	}
}
