package minesweeper.form;

public class SquareButtonProvider {

	SquareButton create(final int row, final int column) {
		return new SquareButton(row, column);
	}
}
