package minesweeper.model.data.access;

public class Score {

	private static final int DEFAULT_SCORE = 999;

	private static final String DEFAULT_NAME = "Anonyme";

	private final int score;
	private final String name;

	public Score() {
		this(DEFAULT_SCORE, Score.DEFAULT_NAME);
	}

	public Score(int score, final String name) {
		if (score < 1) {
			score = 1;
		}
		this.score = score;
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public String getName() {
		return new String(name);
	}
}
