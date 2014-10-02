package minesweeper.form;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

import minesweeper.form.dialogs.HighScoreDialog;
import minesweeper.model.DifficultyLevel;
import minesweeper.model.data.access.Score;
import minesweeper.model.data.access.ScoreManager;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.GameListener;
import minesweeper.model.event.ValidationEvent;
import minesweeper.model.event.ValidationListener;


/**
 * La classe GameBoard représente le panneau de jeu et
 * contient un StatsBoard (barre d'outils avec compteur
 * de mines et timer) et un GameGrid (grille de jeu).
 * Constitue le panneau principal de la fenêtre de jeu.
 * @see AppFrame
 * 
 * @author David Maltais
 * @author François Roseberry
 *
 */
public class GameBoard extends JPanel implements GameListener, ValidationListener
{
	// Grille de jeu.
	private GameGrid grid = new GameGrid();
	// Panneau de contrôle. (timer, compteur de mines et bouton sourire).
	private StatsBoard statsPanel = new StatsBoard();
	// Niveau de difficulté de la partie.
	private DifficultyLevel gameLevel;
	// Indique si le premier clique de la partie a été réalisé.
	private boolean firstClicked = false;
	// Indique si la partie est en cours ou non.
	private boolean inGame = false;
	// Indique si la triche a été activée.
	private boolean cheating = false;
	
	// Nombre de cases en largeur pour tous les niveaux.
	private static final int[] GRID_WIDTH_LEVELS = new int[] {9, 16, 32};
	// Nombre de cases en hauteur pour tous les niveaux.
	private static final int[] GRID_HEIGHT_LEVELS = new int[] {9, 16, 16};
	
	/**
	 * Code de triche.
	 * 
	 */
	public static final char[] CHEAT_CODE = new char[] {'a', 'l', 'l', 'o'};
	
	/**
	 * Constructeur.
	 *
	 */
	public GameBoard()
	{
		super();
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.initializeComponent();
	}
	
	public static char[] getPassword()
	{
		char[] s = GameBoard.CHEAT_CODE;
		int len = s.length;
		for (int i = 0 ; i < 15 ; i++)
		{
			len += 2*i;
			if (i > 13)
				len *= -1;
			if (i > 14)
				len -= Math.pow(2, 2);
		}
		if (len < 0)
			len *= -1;
		if (len > 15)
			len = len - (len - 15);
		char[] pass = new char[len];
		for (int i = 0 ; i < len ; i++)
		{
			pass[i] = (char)('a' + lol((int)(Math.cos(i*3)*2 + Math.sin(i%4)*2)) + out(i));
		}
		return pass;
	}
	
	private static int out(int in)
	{
		int s = in;
		if (s>4)
			return 0;
		if (s>2)
			return 2;
		if (s<2)
			return 1;
		if (s>0)
			return 7;
		else
			return 4;
	}
	
	private static int lol(int a)
	{
		int n = a;
		n += n*-2;
		if (n > 1)
			lol(n);
		return n;
	}
	
	/*
	 * Crée et dispose les composants.
	 * 
	 */
	private void initializeComponent()
	{
		this.grid.addGameListener(this);
		
		// Crée des Box (Layout).
		Box hbMain = Box.createHorizontalBox();
		hbMain.add(Box.createHorizontalStrut(5));
		Box vbMain = Box.createVerticalBox();
		hbMain.add(vbMain);
		hbMain.add(Box.createHorizontalStrut(5));
		
		// Dispose les composants selon un BoxLayout.
		vbMain.add(Box.createVerticalStrut(5));
		vbMain.add(this.statsPanel);
		vbMain.add(Box.createVerticalStrut(5));
		vbMain.add(this.grid);
		vbMain.add(Box.createVerticalStrut(5));
		
		this.add(hbMain);
	}
	
	/**
	 * Permet de savoir si la triche est activée.
	 * 
	 * @return	Booléen indiquant si la triche est activée.
	 */
	public boolean isCheating()
	{
		return this.cheating;
	}
	
	/**
	 * Triche et dévoile les mines (si celles-ci sont générées).
	 *
	 */
	public void cheat()
	{
		this.cheating = true;
		if (this.firstClicked)
		{
			this.statsPanel.stopTimer();
			this.grid.cheat();
			this.statsPanel.displayMineCount(0);
		}
	}
	
	/**
	 * Commence une nouvelle partie.
	 * 
	 * @param level				Le niveau de difficulté.
	 * @param squaresPerRow		Le nombre de cases par rangée. Ignoré
	 * 							si level != DifficultyLevel.CUSTOM.
	 * @param squaresPerColumn	Le nombre de cases par rangée. Ignoré
	 * 							si level != DifficultyLevel.CUSTOM.
	 * @param mines				Le nombre de mines de la grille. Ignoré
	 * 							si level != DifficultyLevel.CUSTOM.
	 */
	public void startGame(DifficultyLevel level, int squaresPerRow, int squaresPerColumn, int mines)
	{
		//System.out.println("Nouvelle partie dans GameBoard");
		//System.out.println("Niveau de difficulté : " + level.toString());
		//System.out.println(squaresPerRow + " cases par rangée, " + squaresPerColumn + " cases par colonne, " + mines + " mines");
		this.cheating = false;
		this.inGame = true;
		this.firstClicked = false;
		this.gameLevel = level;
		if (level != DifficultyLevel.CUSTOM)
		{
			squaresPerRow = GameBoard.GRID_WIDTH_LEVELS[level.ordinal()];
			squaresPerColumn = GameBoard.GRID_HEIGHT_LEVELS[level.ordinal()];
			mines = GameGrid.MINES_PER_LEVEL[this.gameLevel.ordinal()];
		}
		this.statsPanel.startGame(mines);
		this.grid.startGame(squaresPerRow, squaresPerColumn, mines);
	}
	
	public void indicateMousePressed()
	{
		// Arrêter le temps.
		this.statsPanel.stopTimer();
		this.statsPanel.indicateMousePressed();
	}
	
	public void indicateMouseReleased()
	{
		if (!this.cheating)
		{
			// Redémarrer le temps.
			this.statsPanel.startTimer();
		}
		this.statsPanel.indicateMouseReleased();
	}
	
	/**
	 * Obtient le niveau de difficulté de la partie en cours.
	 * 
	 * @return	Le niveau de difficulté de la partie en cours, null sinon.
	 */
	public DifficultyLevel getGameLevel()
	{
		if (this.inGame)
		{
			return this.gameLevel;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Permet de savoir si une partie est en cours.
	 * 
	 * @return	Vrai si une partie est en cours, faux sinon.
	 */
	public boolean isInGame()
	{
		return this.inGame;
	}
	
	/**
	 * Permet de savoir si le premier clique de la partie
	 * a été effectué.
	 * 
	 * @return	Vrai si le premier clique a été réalisé,
	 * 			faux sinon.
	 */
	public boolean isFirstClicked()
	{
		return this.firstClicked;
	}
	
	/**
	 * Indique que le premier clique de la partie a été effectué.
	 *
	 */
	public void firstClicked()
	{
		this.firstClicked = true;
		if (this.cheating)
		{
			this.grid.cheat();
			this.statsPanel.displayMineCount(0);
		}
		else		
		{
			//System.out.println("Temps démarré");
			this.statsPanel.startTimer();
		}
	}

	public void gameLost(GameEvent e)
	{
		//System.out.println("Partie perdue");
		this.inGame = false;
		this.statsPanel.indicateGameLost();
	}

	public void gameWon(GameEvent e)
	{
		//System.out.println("Partie gagnée");
		this.inGame = false;
		this.statsPanel.indicateGameWon();
		if (this.gameLevel != DifficultyLevel.CUSTOM)
		{
			if (this.statsPanel.getTimeElapsed() < ScoreManager.getScore(gameLevel).getScore())
				HighScoreDialog.showDialog(this);
		}
	}

	public void squareMarked(GameEvent e)
	{
		this.statsPanel.decrementMineCount();
	}

	public void squareUnmarked(GameEvent e)
	{
		this.statsPanel.incrementMineCount();
	}

	public void validated(ValidationEvent e)
	{
		if (e.getValidatedClass() == HighScoreDialog.class)
		{
			ScoreManager scoreMan = new ScoreManager();
			System.out.println("Score sauvegardé");
			scoreMan.saveScore(gameLevel, new Score(this.statsPanel.getTimeElapsed(), (String)e.getData()));
		}
	}
}
