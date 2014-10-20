package minesweeper.model;

import minesweeper.model.exception.MineFoundException;

import com.google.common.collect.ImmutableList;

//	Conditions de victoire :
// 	 		- Toutes les cases non-minées ont été révélées.
//			(autrement dit, le nombre de cases révélées = rows*columns - mines.size()
//          - Si les mines n'ont pas toutes été marquées, marquer celles qui restent
public class Grid {

	private final GridSize size;
	private final Cells cells;

	public static Grid create(final GridSize size, final MineGenerator generator) {
		return new Grid(size, emptyCells(generator));
	}

	private Grid(final GridSize size, final Cells cells) {
		this.size = size;
		this.cells = cells;
	}

	public Grid reveal(final Cell cell) throws MineFoundException {
		return new Grid(size, cells.reveal(cell));
	}

	public CellState at(final Cell cell) {
		return cells.at(cell);
	}

	public boolean isWinning() {
		return cells.size() == size.cellCount();
	}

	private interface Cells {

		CellState at(final Cell cell);

		Cells reveal(final Cell cell) throws MineFoundException;

		int size();
	}

	private static Cells emptyCells(final MineGenerator generator) {
		return new Cells() {

			@Override
			public int size() {
				return 0;
			}

			@Override
			public CellState at(final Cell cell) {
				return CellState.HIDDEN;
			}

			@Override
			public Cells reveal(final Cell cell) throws MineFoundException {
				return withMines(generator.getMinedCells()).reveal(cell);
			}
		};
	}

	private static Cells withMines(final ImmutableList<Cell> mines) {
		return inGameCells(ImmutableList.<Cell> of(), mines);
	}

	private static Cells inGameCells(final ImmutableList<Cell> revealed, final ImmutableList<Cell> mines) {
		return new Cells() {

			@Override
			public CellState at(final Cell cell) {
				if (isRevealed(cell)) {
					return CellState.REVEALED;
				}

				return CellState.HIDDEN;
			}

			@Override
			public Cells reveal(final Cell cell) throws MineFoundException {
				if (isMined(cell)) {
					throw new MineFoundException();
				}

				return inGameCells(newRevealedCells(cell), mines);
			}

			@Override
			public int size() {
				return mines.size() + revealed.size();
			}

			private ImmutableList<Cell> newRevealedCells(final Cell cell) {
				ImmutableList.Builder<Cell> builder = ImmutableList.builder();
				builder.addAll(revealed);
				builder.add(cell);
				// TODO : révéler les voisins, récursivement.
				return builder.build();
			}

			private boolean isRevealed(final Cell cell) {
				return revealed.contains(cell);
			}

			private boolean isMined(final Cell cell) {
				return mines.contains(cell);
			}
		};
	}
}
