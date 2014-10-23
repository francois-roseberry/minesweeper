package minesweeper.form;

import minesweeper.model.Cell;

public class SquareButtonProvider {

	SquareButton create(final Cell cell) {
		return new SquareButton(cell);
	}
}
