package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

public class InGameGrid implements Grid {

	private final ImmutableSet<Cell> mines;
	private final ImmutableSet<Cell> hidden;
	private final ImmutableSet<Cell> revealed;
	private final MarkedCells marked;

	public static InGameGrid create(final ImmutableSet<Cell> mines,
			final ImmutableSet<Cell> cells, final MarkedCells marked) {
		return new InGameGrid(Preconditions.checkNotNull(mines),
				Preconditions.checkNotNull(cells), ImmutableSet.<Cell> of(),
				Preconditions.checkNotNull(marked));
	}

	private InGameGrid(final ImmutableSet<Cell> mines,
			final ImmutableSet<Cell> hidden, final ImmutableSet<Cell> revealed,
			final MarkedCells marked) {
		this.mines = mines;
		this.hidden = hidden;
		this.revealed = revealed;
		this.marked = marked;
	}

	@Override
	public CellState at(final Cell cell) {
		if (isRevealed(cell)) {
			return CellState.REVEALED;
		}

		// Si cette case n'est pas minée et qu'un chemin non-miné mène de cette
		// case à
		// une case déjà révélée, alors cette case est révélée aussi.
		// Cette idée, si implémentée telle quelle, est beaucoup trop
		// inefficace.
		// L'idée est bonne pourtant, faut je la retravaille.

		return marked.at(cell);
	}

	@Override
	public Grid reveal(final Cell cell) {
		if (isMined(cell)) {
			return new HitGrid(cell, this);
		}

		return new InGameGrid(mines, hidden, newRevealedCells(cell), marked);
	}

	@Override
	public Grid mark(final Cell cell) {
		return new InGameGrid(mines, hidden, revealed, marked.mark(cell));
	}

	private ImmutableSet<Cell> newRevealedCells(final Cell cell) {
		ImmutableSet.Builder<Cell> builder = ImmutableSet.builder();
		builder.addAll(revealed);
		builder.add(cell);

		// TODO
		// Au lieu de révéler récursivement les voisins (et ainsi être obligé
		// d'avoir
		// les dimensions de la grille), les révéler uniquement quand on demande
		// leur
		// état dans at().
		for (Cell neighboor : cell.neighboors()) {
			if (!mines.contains(neighboor)) {
				builder.add(neighboor);
			}
		}

		return builder.build();
	}

	private boolean isRevealed(final Cell cell) {
		return revealed.contains(cell);
	}

	private boolean isMined(final Cell cell) {
		return mines.contains(cell);
	}
}
