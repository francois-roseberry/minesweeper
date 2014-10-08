package minesweeper.model.data.access;

import java.util.prefs.Preferences;

import minesweeper.model.DifficultyLevel;

import com.google.common.base.Preconditions;

public class JavaScoreManager implements ScoreManager {

	private final Preferences preferences;
	private static final String DEFAULT_NAME = "Anonyme";
	private static final int DEFAULT_SCORE = 999;

	/**
	 * Constructeur.
	 * 
	 */
	public JavaScoreManager() {
		this.preferences = Preferences.userNodeForPackage(getClass());
	}

	/**
	 * Lit un score de mani�re statique.
	 * 
	 * @param level
	 *            Le niveau de difficult� � lire.
	 * @return
	 */
	public static Score getScore(final DifficultyLevel level) {
		return new JavaScoreManager().readScore(level);
	}

	/**
	 * Lit un score.
	 * 
	 * @param level
	 *            Le niveau de difficult� � lire.
	 * @return
	 */
	public Score readScore(final DifficultyLevel level) {
		Preconditions.checkNotNull(level);

		String scoreString = this.preferences.get(level.toString(), JavaScoreManager.DEFAULT_SCORE + " " + JavaScoreManager.DEFAULT_NAME);
		int index = scoreString.indexOf(' ');
		return new Score(Integer.parseInt(scoreString.substring(0, index)), scoreString.substring(index + 1));
	}

	public void saveScore(final DifficultyLevel level, final Score score) {
		Preconditions.checkNotNull(level);
		Preconditions.checkNotNull(score);

		preferences.put(level.toString(), score.getTime() + " " + score.getName());
	}

	public void resetScores() {
		saveScore(DifficultyLevel.BEGINNER, new Score());
		saveScore(DifficultyLevel.MEDIUM, new Score());
		saveScore(DifficultyLevel.EXPERT, new Score());
	}
}
