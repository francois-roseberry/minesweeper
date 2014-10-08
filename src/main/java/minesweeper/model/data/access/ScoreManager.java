package minesweeper.model.data.access;

import minesweeper.model.DifficultyLevel;

public interface ScoreManager {

	Score readScore(final DifficultyLevel level);

	void saveScore(final DifficultyLevel level, final Score score);
}
