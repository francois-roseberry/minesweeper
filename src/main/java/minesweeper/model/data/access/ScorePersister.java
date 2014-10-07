package minesweeper.model.data.access;

import minesweeper.model.DifficultyLevel;

public interface ScorePersister {

	Score readScore(final DifficultyLevel level);

	void saveScore(final DifficultyLevel level, final Score score);
}
