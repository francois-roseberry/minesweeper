package minesweeper.form;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import minesweeper.model.DifficultyLevel;

/**
 * La classe AppFrame repr�sente la fen�tre de jeu. Elle ne peut �tre instanci�e plus qu'une fois. On obtient la r�f�rence � l'objet du
 * singleton par getInstance().
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
@SuppressWarnings("serial")
public class AppFrame extends JFrame implements WindowListener {

	// Titre de la fen�tre.
	private static final String TITLE = "D�mineur";
	// Question demand�e lorsque l'utilisateur essaie de quitter.
	private static final String EXIT_QUESTION = "Voulez-vous quitter?";
	private static final String EXIT_TITLE = "Question";
	// Instance unique de cette classe (singleton).
	private static AppFrame instance = null;

	// Panneau de jeu.
	private GameBoard gamePanel = new GameBoard();
	// Barre de menus.
	private AppMenu mb = new AppMenu();
	private int squaresPerRow = 0;
	private int squaresPerColumn = 0;
	private int mines = 0;

	/**
	 * Constructeur.
	 * 
	 */
	public AppFrame() {
		super(AppFrame.TITLE);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(this);
		initializeComponent();
		centerWindow();
		startGame(DifficultyLevel.BEGINNER, 0, 0, 0);
	}

	/**
	 * Obtient l'insatance unique du AppFrame. Impl�mentation du singleton.
	 * 
	 * @return L'instance de AppFrame.
	 */
	public static AppFrame getInstance() {
		if (instance == null) {
			instance = new AppFrame();
		}
		return instance;
	}

	/*
	 * Centre la fen�tre � l'�cran.
	 */
	private void centerWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
	}

	public GameBoard getGameBoard() {
		if (instance != null) {
			return gamePanel;
		}

		return null;
	}

	/*
	 * Cr�e et dispose les composants.
	 * 
	 */
	private void initializeComponent() {
		setJMenuBar(mb);
		add(gamePanel);
	}

	/**
	 * Demande � l'utilisateur une confirmation pour quitter.
	 * 
	 */
	public void askExitQuestion() {
		if (JOptionPane.showConfirmDialog(null, AppFrame.EXIT_QUESTION, AppFrame.EXIT_TITLE, 0) == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	@Override
	public void windowActivated(final WindowEvent e) {}

	@Override
	public void windowClosed(final WindowEvent e) {}

	@Override
	public void windowClosing(final WindowEvent e) {
		askExitQuestion();
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {}

	@Override
	public void windowDeiconified(final WindowEvent e) {}

	@Override
	public void windowIconified(final WindowEvent e) {}

	@Override
	public void windowOpened(final WindowEvent e) {}

	/**
	 * Commence une nouvelle partie.
	 * 
	 */
	public void startGame() {
		DifficultyLevel level = mb.getSelectedGameLevel();
		if (level != DifficultyLevel.CUSTOM) {
			startGame(level, 0, 0, 0);
		} else {
			startGame(level, squaresPerRow, squaresPerColumn, mines);
		}
	}

	/**
	 * Commence une nouvelle partie.
	 * 
	 * @param level
	 *            Le niveau de difficult�.
	 * @param squaresPerRow
	 *            Nombre de cases par rang�e. Inutile si level != DifficultyLevel.CUSTOM.
	 * @param squaresPerColumn
	 *            Nombre de cases par colonne. Inutile si level != DifficultyLevel.CUSTOM.
	 * @param mines
	 *            Nombre de mines de la grille. Inutile si level != DifficultyLevel.CUSTOM.
	 * 
	 */
	public void startGame(final DifficultyLevel level, final int squaresPerRow, final int squaresPerColumn, final int mines) {
		gamePanel.startGame(level, squaresPerRow, squaresPerColumn, mines);
		if ((squaresPerRow == 0 && squaresPerColumn == 0) ||
				(squaresPerRow != this.squaresPerRow && squaresPerColumn != this.squaresPerColumn)) {
			this.squaresPerRow = squaresPerRow;
			this.squaresPerColumn = squaresPerColumn;
			pack();
			centerWindow();
		}
		this.mines = mines;
	}
}
