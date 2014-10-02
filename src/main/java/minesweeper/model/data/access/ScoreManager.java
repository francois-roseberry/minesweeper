package minesweeper.model.data.access;

import java.util.prefs.Preferences;

import minesweeper.model.DifficultyLevel;


public class ScoreManager
{
	private Preferences prefs;
	private static final String DEFAULT_NAME = "Anonyme";
	private static final int DEFAULT_SCORE = 999;
	
	/**
	 * Constructeur.
	 *
	 */
	public ScoreManager()
	{
		this.prefs = Preferences.userNodeForPackage(this.getClass());
	}
	
	/**
	 * Lit un score de manière statique.
	 * 
	 * @param level	Le niveau de difficulté à lire.
	 * @return
	 */
	public static Score getScore(DifficultyLevel level)
	{
		return new ScoreManager().readScore(level);
	}
	
	/**
	 * Lit un score.
	 * 
	 * @param level Le niveau de difficulté à lire.
	 * @return
	 */
	public Score readScore(DifficultyLevel level)
	{
		Score s = null;
		if (level != null)
		{
			String scoreString = this.prefs.get(level.toString(), ScoreManager.DEFAULT_SCORE + " " + ScoreManager.DEFAULT_NAME);
			int index = scoreString.indexOf(' ');
			s = new Score(Integer.parseInt(scoreString.substring(0, index)), scoreString.substring(index + 1));
		}
		return s;
	}
	
	public void saveScore(DifficultyLevel level, Score score)
	{
		if (level != null && score != null)
			this.prefs.put(level.toString(), score.getScore() + " " + score.getName());
	}
	
	public void resetScores()
	{
		this.saveScore(DifficultyLevel.BEGINNER, new Score(ScoreManager.DEFAULT_SCORE, ScoreManager.DEFAULT_NAME));
		this.saveScore(DifficultyLevel.MEDIUM, new Score(ScoreManager.DEFAULT_SCORE, ScoreManager.DEFAULT_NAME));
		this.saveScore(DifficultyLevel.EXPERT, new Score(ScoreManager.DEFAULT_SCORE, ScoreManager.DEFAULT_NAME));
	}
}
