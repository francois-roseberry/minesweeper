package minesweeper.model.event;

public interface SquareButtonListener
{
	public void squareMarked(GameEvent e);
	public void squareUnmarked(GameEvent e);
}
