package minesweeper.model.data.access;

public class Score {

	private static final int DEFAULT_SCORE = 999;

	private static final String DEFAULT_NAME = "Anonyme";

	private final int time;
	private final String name;

	public Score() {
		this(DEFAULT_SCORE, Score.DEFAULT_NAME);
	}

	public Score(int time, final String name) {
		if (time < 1) {
			time = 1;
		}
		this.time = time;
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public String getName() {
		return new String(name);
	}
}
