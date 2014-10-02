package minesweeper.form;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import minesweeper.model.DifficultyLevel;


import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * La classe AppFrame représente la fenêtre de jeu. Elle
 * ne peut être instanciée plus qu'une fois. On obtient
 * la référence à l'objet du singleton par getInstance().
 * 
 * @author David Maltais
 * @author François Roseberry
 *
 */
public class AppFrame extends JFrame implements WindowListener
{
	// Titre de la fenêtre.
	private static final String TITLE = "Démineur";	
	// Question demandée lorsque l'utilisateur essaie de quitter.
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
	private int nbMines = 0;
	
	/**
	 * Constructeur.
	 *
	 */
	public AppFrame()
	{
		super(AppFrame.TITLE);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.addWindowListener(this);
		this.initializeComponent();
		//this.pack();
		this.centerWindow();
		this.startGame(DifficultyLevel.BEGINNER, 0, 0, 0);
	}
	
	/**
	 * Obtient l'insatance unique du AppFrame.
	 * Implémentation du singleton.
	 * 
	 * @return	L'instance de AppFrame.
	 */
	public static AppFrame getInstance()
	{
		if (instance == null)
		{
			instance = new AppFrame();
		}
		return instance;
	}
	
	/*
	 * Centre la fenêtre à l'écran.
	 */
	private void centerWindow()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
	}
	
	public GameBoard getGameBoard()
	{
		if (instance != null)
		{
			return this.gamePanel;
		}
		else
		{
			return null;
		}
	}
	
	/*
	 * Crée et dispose les composants.
	 * 
	 */
	private void initializeComponent()
	{
		this.setJMenuBar(this.mb);
		this.add(this.gamePanel);
	}
	
	/**
	 * Demande à l'utilisateur une confirmation pour quitter.
	 *
	 */
	public void askExitQuestion()
	{
		if (JOptionPane.showConfirmDialog(null, AppFrame.EXIT_QUESTION, AppFrame.EXIT_TITLE, 0)
				== JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
	}

	public void windowActivated(WindowEvent e)
	{}

	public void windowClosed(WindowEvent e)
	{}

	public void windowClosing(WindowEvent e)
	{
		this.askExitQuestion();
	}

	public void windowDeactivated(WindowEvent e)
	{}

	public void windowDeiconified(WindowEvent e)
	{}

	public void windowIconified(WindowEvent e)
	{}

	public void windowOpened(WindowEvent e)
	{}
	
	/**
	 * Commence une nouvelle partie.
	 *
	 */
	public void startGame()
	{
		DifficultyLevel level = mb.getSelectedGameLevel();
		if (level != DifficultyLevel.CUSTOM)
		{
			this.startGame(level, 0, 0, 0);
		}
		else
		{
			this.startGame(level, this.squaresPerRow, this.squaresPerColumn, this.nbMines);
		}
	}
	
	/**
	 * Commence une nouvelle partie.
	 * 
	 * @param level				Le niveau de difficulté.
	 * @param squaresPerRow		Nombre de cases par rangée. Inutile
	 * 							si level != DifficultyLevel.CUSTOM.
	 * @param squaresPerColumn	Nombre de cases par colonne. Inutile
	 * 							si level != DifficultyLevel.CUSTOM.
	 * @param mines				Nombre de mines de la grille. Inutile
	 * 							si level != DifficultyLevel.CUSTOM.
	 * 
	 */
	public void startGame(DifficultyLevel level, int squaresPerRow, int squaresPerColumn, int mines)
	{
		// Commence une nouvelle partie (dans le panneau de jeu).
		this.gamePanel.startGame(level, squaresPerRow, squaresPerColumn, mines);
		if ((squaresPerRow == 0 && squaresPerColumn == 0) ||
			(squaresPerRow != this.squaresPerRow && squaresPerColumn != this.squaresPerColumn))
		{
			this.squaresPerRow = squaresPerRow;
			this.squaresPerColumn = squaresPerColumn;
			// Redimensionner tous les composants en fonction de la grille de jeu.
			//System.out.println("Réajustement et centrage de la fenêtre");
			this.pack();
			// Re-centrer la fenêtre.
			this.centerWindow();
		}
		this.nbMines = mines;
	}
}
