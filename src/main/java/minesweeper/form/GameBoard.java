package minesweeper.form;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

import minesweeper.form.dialogs.HighScoreDialog;
import minesweeper.model.DifficultyLevel;
import minesweeper.model.GridSize;
import minesweeper.model.data.access.JavaScoreManager;
import minesweeper.model.data.access.Score;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.GameListener;
import minesweeper.model.event.ValidationEvent;
import minesweeper.model.event.ValidationListener;

/**
 * La classe GameBoard repr�sente le panneau de jeu et contient un StatsBoard (barre d'outils avec compteur de mines et timer) et un
 * GameGrid (grille de jeu). Constitue le panneau principal de la fen�tre de jeu.
 * 
 * @see AppFrame
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel implements GameListener, ValidationListener {

	// Grille de jeu.
	private final GameGrid grid;
	// Panneau de contr�le. (timer, compteur de mines et bouton sourire).
	private final StatsBoard statsPanel = new StatsBoard();
	// Niveau de difficult� de la partie.
	private DifficultyLevel gameLevel;
	// Indique si le premier clique de la partie a �t� r�alis�.
	private boolean firstClicked = false;
	// Indique si la partie est en cours ou non.
	private boolean inGame = false;
	// Indique si la triche a �t� activ�e.
	private boolean cheating = false;

	// Nombre de cases en largeur pour tous les niveaux.
	private static final int[] GRID_WIDTH_LEVELS = new int[] { 9, 16, 32 };
	// Nombre de cases en hauteur pour tous les niveaux.
	private static final int[] GRID_HEIGHT_LEVELS = new int[] { 9, 16, 16 };

	/**
	 * Code de triche.
	 * 
	 */
	public static final char[] CHEAT_CODE = new char[] { 'a', 'l', 'l', 'o' };

	/**
	 * Constructeur.
	 * 
	 */
	public GameBoard() {
		super();

		grid = new GameGrid(createGameServices());

		setBorder(BorderFactory.createRaisedBevelBorder());
		initializeComponent();
	}

	protected GameServices createGameServices() {
		return new GameServices() {

			@Override
			public boolean isInGame() {
				return inGame;
			}

			@Override
			public boolean isFirstClicked() {
				return firstClicked;
			}

			@Override
			public void firstClicked() {
				GameBoard.this.firstClicked();
			}

			@Override
			public void indicateMousePressed() {
				GameBoard.this.indicateMousePressed();
			}

			@Override
			public void indicateMouseReleased() {
				GameBoard.this.indicateMouseReleased();
			}
		};
	}

	public static char[] getPassword() {
		char[] s = GameBoard.CHEAT_CODE;
		int len = s.length;
		for (int i = 0; i < 15; i++) {
			len += 2 * i;
			if (i > 13) {
				len *= -1;
			}
			if (i > 14) {
				len -= Math.pow(2, 2);
			}
		}
		if (len < 0) {
			len *= -1;
		}
		if (len > 15) {
			len = len - (len - 15);
		}
		char[] pass = new char[len];
		for (int i = 0; i < len; i++) {
			pass[i] = (char) ('a' + lol((int) (Math.cos(i * 3) * 2 + Math.sin(i % 4) * 2)) + out(i));
		}
		return pass;
	}

	private static int out(final int in) {
		int s = in;
		if (s > 4) {
			return 0;
		}
		if (s > 2) {
			return 2;
		}
		if (s < 2) {
			return 1;
		}
		if (s > 0) {
			return 7;
		}

		return 4;
	}

	private static int lol(final int a) {
		int n = a;
		n += n * -2;
		if (n > 1) {
			lol(n);
		}
		return n;
	}

	/*
	 * Cr�e et dispose les composants.
	 * 
	 */
	private void initializeComponent() {
		grid.addGameListener(this);

		// Cr�e des Box (Layout).
		Box hbMain = Box.createHorizontalBox();
		hbMain.add(Box.createHorizontalStrut(5));
		Box vbMain = Box.createVerticalBox();
		hbMain.add(vbMain);
		hbMain.add(Box.createHorizontalStrut(5));

		// Dispose les composants selon un BoxLayout.
		vbMain.add(Box.createVerticalStrut(5));
		vbMain.add(statsPanel);
		vbMain.add(Box.createVerticalStrut(5));
		vbMain.add(grid);
		vbMain.add(Box.createVerticalStrut(5));

		this.add(hbMain);
	}

	/**
	 * Permet de savoir si la triche est activ�e.
	 * 
	 * @return Bool�en indiquant si la triche est activ�e.
	 */
	public boolean isCheating() {
		return cheating;
	}

	/**
	 * Triche et d�voile les mines (si celles-ci sont g�n�r�es).
	 * 
	 */
	public void cheat() {
		cheating = true;
		if (firstClicked) {
			statsPanel.stopTimer();
			grid.cheat();
			statsPanel.displayMineCount(0);
		}
	}

	/**
	 * Commence une nouvelle partie.
	 * 
	 * @param level
	 *            Le niveau de difficult�.
	 * @param squaresPerRow
	 *            Le nombre de cases par rang�e. Ignor� si level != DifficultyLevel.CUSTOM.
	 * @param squaresPerColumn
	 *            Le nombre de cases par rang�e. Ignor� si level != DifficultyLevel.CUSTOM.
	 * @param mines
	 *            Le nombre de mines de la grille. Ignor� si level != DifficultyLevel.CUSTOM.
	 */
	public void startGame(final DifficultyLevel level, int squaresPerRow, int squaresPerColumn, int mines) {
		cheating = false;
		inGame = true;
		firstClicked = false;
		gameLevel = level;
		if (level != DifficultyLevel.CUSTOM) {
			squaresPerRow = GameBoard.GRID_WIDTH_LEVELS[level.ordinal()];
			squaresPerColumn = GameBoard.GRID_HEIGHT_LEVELS[level.ordinal()];
			mines = GameGrid.MINES_PER_LEVEL[this.gameLevel.ordinal()];
		}
		statsPanel.startGame(mines);
		grid.startGame(GridSize.create(squaresPerRow, squaresPerColumn), mines);
	}

	private void indicateMousePressed() {
		statsPanel.stopTimer();
		statsPanel.indicateMousePressed();
	}

	private void indicateMouseReleased() {
		if (!cheating) {
			statsPanel.startTimer();
		}
		statsPanel.indicateMouseReleased();
	}

	/**
	 * Indique que le premier clique de la partie a �t� effectu�.
	 * 
	 */
	private void firstClicked() {
		firstClicked = true;
		if (cheating) {
			grid.cheat();
			statsPanel.displayMineCount(0);
		} else {
			statsPanel.startTimer();
		}
	}

	@Override
	public void gameLost(final GameEvent e) {
		inGame = false;
		statsPanel.indicateGameLost();
	}

	@Override
	public void gameWon(final GameEvent e) {
		inGame = false;
		statsPanel.indicateGameWon();
		if (gameLevel != DifficultyLevel.CUSTOM) {
			int bestTime = new JavaScoreManager().readScore(gameLevel).getTime();
			if (statsPanel.getTimeElapsed() < bestTime) {
				HighScoreDialog.showDialog(this);
			}
		}
	}

	@Override
	public void squareMarked(final GameEvent e) {
		statsPanel.decrementMineCount();
	}

	@Override
	public void squareUnmarked(final GameEvent e) {
		statsPanel.incrementMineCount();
	}

	@Override
	public void validated(final ValidationEvent e) {
		if (e.getValidatedClass() == HighScoreDialog.class) {
			JavaScoreManager scoreMan = new JavaScoreManager();
			scoreMan.saveScore(gameLevel, new Score(statsPanel.getTimeElapsed(), (String) e.getData()));
		}
	}
}
