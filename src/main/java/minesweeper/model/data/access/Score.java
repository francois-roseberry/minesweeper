package minesweeper.model.data.access;

public class Score
{
	private static final String DEFAULT_NAME = "Anonyme";
	
	private int score;
	private String name;
	
	public Score()
	{
		this(1, Score.DEFAULT_NAME);
	}
	
	public Score(Score scoreObj)
	{
		this(scoreObj.getScore(), scoreObj.getName());
	}
	
	public Score(int score, String name)
	{
		if (score < 1)
			score = 1;
		this.score = score;
		this.name = name;
	}
	
	public void setScore(int score)
	{
		if (score > 0)
			this.score = score;
	}
	
	public int getScore()
	{
		return this.score;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return new String(this.name);
	}
}
