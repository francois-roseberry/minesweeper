package minesweeper.form;

public interface GameServices {

	boolean isInGame();

	boolean isFirstClicked();

	void firstClicked();

	void indicateMousePressed();

	void indicateMouseReleased();
}
