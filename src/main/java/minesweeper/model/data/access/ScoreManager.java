package minesweeper.model.data.access;

import java.util.prefs.Preferences;

import minesweeper.model.DifficultyLevel;

public class ScoreManager {

	private Preferences prefs;
	private static final String DEFAULT_NAME = "Anonyme";
	private static final int DEFAULT_SCORE = 999;

	/**
	 * Constructeur.
	 * 
	 */
	public ScoreManager() {
		this.prefs = Preferences.userNodeForPackage(getClass());
	}

	/**
	 * Lit un score de mani�re statique.
	 * 
	 * @param level
	 *            Le niveau de difficult� � lire.
	 * @return
	 */
	public static Score getScore(final DifficultyLevel level) {
		return new ScoreManager().readScore(level);
	}

	/**
	 * Lit un score.
	 * 
	 * @param level
	 *            Le niveau de difficult� � lire.
	 * @return
	 */
	public Score readScore(final DifficultyLevel level) {
		Score s = null;
		if (level != null) {
			String scoreString = this.prefs.get(level.toString(), ScoreManager.DEFAULT_SCORE + " " + ScoreManager.DEFAULT_NAME);
			int index = scoreString.indexOf(' ');
			s = new Score(Integer.parseInt(scoreString.substring(0, index)), scoreString.substring(index + 1));
		}
		return s;
	}

	public void saveScore(final DifficultyLevel level, final Score score) {
		if (level != null && score != null) {
			prefs.put(level.toString(), score.getScore() + " " + score.getName());
		}
	}

	public void resetScores() {
		saveScore(DifficultyLevel.BEGINNER, new Score());
		saveScore(DifficultyLevel.MEDIUM, new Score());
		saveScore(DifficultyLevel.EXPERT, new Score());
	}
}
