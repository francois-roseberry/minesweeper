package minesweeper.model.grid;

import minesweeper.model.Cell;
import minesweeper.model.CellState;

//Conditions de victoire :
//		- Toutes les cases non-minées ont été révélées.
//	(autrement dit, le nombre de cases révélées = rows*columns - mines.size()
//  - Si les mines n'ont pas toutes été marquées, marquer celles qui restent
public interface Grid {

	Grid reveal(final Cell cell);

	Grid mark(Cell cell);

	CellState at(final Cell cell);
}
