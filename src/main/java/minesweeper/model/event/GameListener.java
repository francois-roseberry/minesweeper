package minesweeper.model.event;

public interface GameListener extends SquareButtonListener {

	void gameLost(GameEvent e);

	void gameWon(GameEvent e);
}
