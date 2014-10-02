package minesweeper.model.event;

public interface GameListener extends SquareButtonListener
{
	public void gameLost(GameEvent e);
	public void gameWon(GameEvent e);
}
