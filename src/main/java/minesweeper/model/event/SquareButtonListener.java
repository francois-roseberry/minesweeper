package minesweeper.model.event;

public interface SquareButtonListener {

	void squareMarked(GameEvent e);

	void squareUnmarked(GameEvent e);
}
