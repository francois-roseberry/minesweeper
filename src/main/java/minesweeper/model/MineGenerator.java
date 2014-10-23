package minesweeper.model;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class MineGenerator {

	private final int mines;
	private final ImmutableList<Cell> cells;

	public MineGenerator(final int mines, final ImmutableList<Cell> cells) {
		this.mines = mines;
		this.cells = cells;
	}

	public ImmutableList<Cell> getMines(final Cell cellToAvoid) {
		int minesToGenerate = mines;
		List<Cell> openCells = getAvailableCellsForMines(cellToAvoid);

		ImmutableList.Builder<Cell> builder = ImmutableList.builder();
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

	private List<Cell> getAvailableCellsForMines(final Cell cellToAvoid) {
		List<Cell> cells = Lists.newArrayList();

		// Remplir le tableau des coordonn�es disponibles.
		for (Cell cell : this.cells) {
			if (!cellToAvoid.equals(cell)) {
				cells.add(cell);
			}
		}
		return cells;
	}

	private Cell pickCellAtRandom(final List<Cell> openCells, final Random random) {
		if (openCells.size() == 1) {
			return openCells.get(0);
		}

		int openIndex = random.nextInt(openCells.size());
		return openCells.get(openIndex);
	}
}
