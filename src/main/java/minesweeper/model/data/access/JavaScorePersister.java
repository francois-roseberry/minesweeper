package minesweeper.model.data.access;

import java.util.prefs.Preferences;

import minesweeper.model.DifficultyLevel;

import com.google.common.base.Preconditions;

public class JavaScorePersister implements ScorePersister {

	private static final String DEFAULT_NAME = "Anonyme";
	private static final int DEFAULT_SCORE = 999;

	private final Preferences preferences;

	public JavaScorePersister() {
		this.preferences = Preferences.userNodeForPackage(getClass());
	}

	@Override
	public Score readScore(final DifficultyLevel level) {
		Preconditions.checkNotNull(level);

		String scoreString = this.preferences.get(level.toString(), DEFAULT_SCORE + " " + DEFAULT_NAME);
		int index = scoreString.indexOf(' ');
		return new Score(Integer.parseInt(scoreString.substring(0, index)), scoreString.substring(index + 1));
	}

	@Override
	public void saveScore(final DifficultyLevel level, final Score score) {
		Preconditions.checkNotNull(level);
		Preconditions.checkNotNull(score);

		preferences.put(level.toString(), score.getScore() + " " + score.getName());
	}
}
