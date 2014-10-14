package minesweeper.model;

import minesweeper.model.exception.GameLostException;

import com.google.common.collect.ImmutableList;

public class Grid {

	private final GridSize size;
	private final Cells cells;

	public static Grid create(final GridSize size, final MineGenerator generator) {
		ImmutableList<Cell> mines = generator.getMinedCells();
		Cells cells = new Cells(mines, ImmutableList.<Cell> of());
		return new Grid(size, cells);
	}

	private Grid(final GridSize size, final Cells cells) {
		this.size = size;
		this.cells = cells;
	}

	// Doit retourner (une grille!) :
	// - si la partie est perdue (une mine a été révélée)
	// - si la partie a été gagnée
	// 		Conditions de victoire :
	// 	 		- Toutes les cases non-minées ont été révélées.
	//			(autrement dit, le nombre de cases révélées = rows*columns - mines.size()
	//          - Si les mines n'ont pas toutes été marquées, marquer celles qui restent
	// - sinon le jeu doit continuer
	public Grid reveal(final Cell cell) throws GameLostException {
		Cells newCells = cells.reveal(cell);
		return new Grid(size, newCells);
	}

	public CellState at(final Cell cell) {
		return cells.at(cell);
	}

	public boolean isWinning() {
		return cells.size() == size.cellCount();
	}

	private static class Cells {

		private final ImmutableList<Cell> mines;
		private final ImmutableList<Cell> revealed;

		public Cells(final ImmutableList<Cell> mines, final ImmutableList<Cell> revealed) {
			this.mines = mines;
			this.revealed = revealed;
		}

		private CellState at(final Cell cell) {
			if (isRevealed(cell)) {
				return CellState.REVEALED;
			}

			return CellState.HIDDEN;
		}

		private Cells reveal(final Cell cell) throws GameLostException {
			if (isMined(cell)) {
				throw new GameLostException();
			}

			return new Cells(mines, newRevealedCells(cell));
		}

		private ImmutableList<Cell> newRevealedCells(final Cell cell) {
			ImmutableList.Builder<Cell> builder = ImmutableList.builder();
			builder.addAll(revealed);
			builder.add(cell);
			// TODO : révéler les voisins, récursivement.
			return builder.build();
		}

		private int size() {
			return mines.size() + revealed.size();
		}

		private boolean isMined(final Cell cell) {
			return mines.contains(cell);
		}

		private boolean isRevealed(final Cell cell) {
			return revealed.contains(cell);
		}
	}
}
