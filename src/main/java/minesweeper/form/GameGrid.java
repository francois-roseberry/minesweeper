package minesweeper.form;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.GameListener;
import minesweeper.model.event.SquareButtonListener;

import util.ImageUtil;


/**
 * La classe GameGrid représente la grille de jeu et
 * contient toutes les cases (boutons).
 * 
 * @author David Maltais
 * @author François Roseberry
 *
 */
public class GameGrid extends JPanel implements MouseListener, SquareButtonListener
{
	// Tableau des cases de la grille.
	// On garde un tableau des cases du jeu même si tous
	// les composants sont des cases donc théoriquement on
	// pourrait y accéder par getComponents() mais comme
	// il faudrait faire énormément trop de castings (coûteux
	// en performances) on garde les références dans un
	// tableau.
	// Aussi, le tableau de components a une seule dimension
	// tandis qu'un tableau de squares a 2 dimensions.
	private SquareButton[][] squares = null;
	// Liste des écouteurs GameListener de cette classe.
	private ArrayList<GameListener> listeners = new ArrayList<GameListener>();
	// Images de numéros.
	private Image img1 = ImageUtil.getImage(this, "num1.gif");
	private Image img2 = ImageUtil.getImage(this, "num2.gif");
	private Image img3 = ImageUtil.getImage(this, "num3.gif");
	private Image img4 = ImageUtil.getImage(this, "num4.gif");
	private Image img5 = ImageUtil.getImage(this, "num5.gif");
	private Image img6 = ImageUtil.getImage(this, "num6.gif");
	private Image img7 = ImageUtil.getImage(this, "num7.gif");
	private Image img8 = ImageUtil.getImage(this, "num8.gif");
	
	private Image imgMineWrong = ImageUtil.getImage(this, "mine_wrong.gif");
	private Image imgMine = ImageUtil.getImage(this, "mine.gif");
	private Image imgMineHit = ImageUtil.getImage(this, "mine_hit.gif");
	private Image imgMineCheated = ImageUtil.getImage(this, "mine_cheated.gif");
	// Nombre de cases en largeur de la grille.
	private int squaresPerRow;
	// Nombre de cases en hauteur de la grille.
	private int squaresPerColumn;
	// Nombre de mines dans la grille.
	private int nbMines;
	// Case contenant la mine qui a été cliquée (qui a causé
	// la défaite du joueur).
	private SquareButton hitSquare;
	
	/**
	 * Nombre de mines à placer dans la grille pour chaque niveau.
	 * 
	 */
	public static final int[] MINES_PER_LEVEL = new int[] {10, 40, 99};
	/**
	 * Nombre de carrés minimum acceptés en hauteur ou en largeur.
	 *  
	 */
	public static final int MIN_SQUARES_IN_A_LINE = 9;
	/**
	 * Nombre de carrés maximum acceptés en hauteur ou en largeur.
	 *  
	 */
	public static final int MAX_SQUARES_IN_A_LINE = 32;
	/**
	 * Nombre de mines minimal.
	 * 
	 */
	public static final int MIN_MINES = 10;
	/**
	 * Nombre de mines maximal.
	 * 
	 */
	public static final int MAX_MINES = 630;
	
	/**
	 * Constructeur. Crée une grille vide.
	 *
	 */
	public GameGrid()
	{
		super();
		this.setBorder(BorderFactory.createLoweredBevelBorder());
	}
	
	/**
	 * Active le mode de triche en trichant sur chaque case.
	 *
	 */
	public void cheat()
	{
		if (this.squares != null && this.squares[0] != null)
		{
			for (int j = 0 ; j < this.squares[0].length ; j++)
			{
				for (int i = 0 ; i < this.squares.length ; i++)
				{
					this.squares[i][j].cheat();
				}
			}
		}
	}
	
	private void createSquares(int nbSquaresWidth, int nbSquaresHeight)
	{
		this.deleteSquares();
		this.setLayout(new GridLayout(nbSquaresHeight, nbSquaresWidth));
		this.squares = new SquareButton[nbSquaresWidth][nbSquaresHeight];
		for (int j = 0 ; j < this.squares[0].length ; j++)
		{
			for (int i = 0 ; i < this.squares.length ; i++)
			{
				this.squares[i][j] = new SquareButton(i, j);
				this.squares[i][j].addMouseListener(this);
				this.squares[i][j].addSquareButtonListener(this);
				this.squares[i][j].setBorder(BorderFactory.createRaisedBevelBorder());
				this.add(this.squares[i][j]);
			}
		}
	}
	
	private void deleteSquares()
	{
		// Tous les composants sont des cases de jeu, on
		// les enlève donc tous.
		this.removeAll();
		/*if (this.squares != null)
		{
			for (int i = 0 ; i < this.squares.length ; i++)
			{
				for (int j = 0 ; j < this.squares[0].length ; j++)
				{
					this.remove(this.squares[i][j]);
					this.squares[i][j] = null;
				}
			}
		}*/
		this.squares = null;
	}
	
	private void resetSquareButtons()
	{
		if (this.squares != null && this.squares[0] != null)
		{
			for (int j = 0 ; j < this.squares[0].length ; j++)
			{
				for (int i = 0 ; i < this.squares.length ; i++)
				{
					this.squares[i][j].reset();
				}
			}
		}
	}
	
	/*
	 * Générer les emplacements aléatoires des mines.
	 * 
	 */
	private void generateMines(SquareButton squareToAvoid, int nbMines)
	{
		if (AppFrame.getInstance().getGameBoard().isInGame() && !AppFrame.getInstance().getGameBoard().isFirstClicked())
		{
			if (nbMines > 0 && nbMines < this.getNumSquares() - 1)
			{
				// Créer une collection de cases disponibles.
				// (les cases qui peuvent recevoir des mines)
				ArrayList<Point> openCoords = new ArrayList<Point>();
				
				// Remplir le tableau des coordonnées disponibles.
				for (int i = 0 ; i < this.squares.length ; i++)
				{
					for (int j = 0 ; j < this.squares[0].length; j++)
					{
						if (!squareToAvoid.equalCoords(i, j))
						{
							openCoords.add(new Point(i, j));
						}
					}
				}
				int openIndex;
				Random rnd = new Random();
				while (nbMines > 0)
				{
					// Générer un index aléatoire.
					openIndex = rnd.nextInt(openCoords.size());
					this.squares[(int)openCoords.get(openIndex).getX()]
					     [(int)openCoords.get(openIndex).getY()].setMine();
					// Enlever la case de la liste.
					openCoords.remove(openIndex);
					nbMines--;
				}
			}
		}
	}
	
	/*
	 * Obtient le nombre de cases de la grille.
	 * 
	 */
	private int getNumSquares()
	{
		int numSquares = 0;
		if (this.squares != null && this.squares[0] != null)
		{
			numSquares = this.squares.length * this.squares[0].length;
		}
		return numSquares;
	}
	
	/*
	 * Méthode permettant de révéler une case, compter les
	 * mines alentour et au besoin, s'appeler récursivement
	 * pour révéler les cases autour.
	 * 
	 */
	private void revealNeighboorSquares(SquareButton square)
	{
		if (square.getState() == SquareButtonState.HIDDEN)
		{
			this.countNeighboorMines(square);
			square.reveal();
			if (square.getNeighboorMinesCount() == 0)
			{
				// La case n'a pas de mines avoisinantes.
				// Révéler les voisins.
				for (int i = -1 ; i <= 1 ; i++)
				{
					for (int j = -1 ; j <= 1 ; j++)
					{
						if (i != 0 || j != 0)
						{
							if (this.isSquare(square.getXSquare() + i, square.getYSquare() + j))
							{
								this.revealNeighboorSquares(this.squares[square.getXSquare() + i][square.getYSquare() + j]);
							}
						}
					}
				}
			}
		}
	}
	
	/*
	 * Vérifie si les coordonnées fournies (plutôt indices de
	 * tableau) correspondent à une case de la grille.
	 * 
	 */
	private boolean isSquare(int x, int y)
	{
		boolean isSquare = false;
		if (this.squares != null && this.squares[0] != null)
		{
			if (x >= 0 && x < this.squares.length &&
					y >= 0 && y < this.squares[0].length)
			{
				isSquare = true;
			}
		}
		return isSquare;
	}
	
	/*
	 * Finalise une partie perdue en révélant les mines, etc.
	 * 
	 */
	private void finishLostGame()
	{
		if (this.squares != null && this.squares[0] != null)
		{
			for (int i = 0 ; i < this.squares.length ; i++)
			{
				for (int j = 0 ; j < this.squares[0].length ; j++)
				{
					if ((this.squares[i][j].getState() == SquareButtonState.HIDDEN ||
							this.squares[i][j].getState() == SquareButtonState.UNSURE)
							&& this.squares[i][j].isMined())
					{
						// Révéler la mine.
						this.squares[i][j].reveal();
					}
					if (this.squares[i][j].getState() == SquareButtonState.MARKED
							&& !this.squares[i][j].isMined())
					{
						// Marquer la case comme minée incorrectement.
						this.squares[i][j].setWasFlagged();
						// Révéler la case.
						this.squares[i][j].reveal();
					}
				}
			}
		}
	}
	
	/*
	 * Compte le nombre de mines voisines d'une case.
	 * 
	 */
	private void countNeighboorMines(SquareButton square)
	{
		if (this.squares != null && this.squares[0] != null)
		{
			for (int i = -1 ; i <= 1 ; i++)
			{
				for (int j = -1 ; j <= 1 ; j++)
				{
					if (i != 0 || j != 0)
					{
						if (this.isSquare(square.getXSquare() + i, square.getYSquare() + j) && this.squares[square.getXSquare() + i][square.getYSquare() + j].isMined())
						{
							this.squares[square.getXSquare()][square.getYSquare()].incrementNeighboorMinesCount();
						}
					}
				}
			}
		}
	}
	
	/*
	 * Gère le clic gauche sur une case.
	 * 
	 */
	private void squareButton_leftClick(SquareButton square)
	{
		// Premier clique de la partie.
		if (!AppFrame.getInstance().getGameBoard().isFirstClicked())
		{
			if (square.getState() == SquareButtonState.HIDDEN)
			{
				// Générer les mines.
				this.generateMines(square, this.nbMines);
				// Découvrir la case qui a été cliquée.
				this.revealNeighboorSquares(square);
				// Indiquer que le premier clique a été réalisé.
				AppFrame.getInstance().getGameBoard().firstClicked();
			}
		}
		else
		{
			// Clic normal (pendant la partie).
			if (square.isMined() && square.getState() == SquareButtonState.HIDDEN)
			{
				// La partie a été perdue.
				this.hitSquare = square;
				this.finishLostGame();
				square.reveal();
				this.onGameLost(new GameEvent());
			}
			else
			{
				this.revealNeighboorSquares(square);
				// Si les cases restantes sont toutes minées, alors
				// les marquer automatiquement.
				if (this.areAllHiddenSquaresMined())
				{
					this.markHiddenSquares();
				}
				
				// Vérifier si la partie a été gagnée.
				this.checkGameWon();
			}
		}
	}
	
	/*
	 * Gère le clique droit sur une case.
	 * 
	 */
	private void squareButton_rightClick(SquareButton square)
	{
		if (square.getState() != SquareButtonState.REVEALED &&
				square.getState() != SquareButtonState.CHEATED)
		{
			square.rightClick();
		}
		// Vérifier si la partie a été gagnée.
		this.checkGameWon();
	}
	
	private void markHiddenSquares()
	{
		if (this.squares != null && this.squares[0] != null)
		{
			for (int i = 0 ; i < this.squares.length ; i++)
			{
				for (int j = 0 ; j < this.squares[0].length ; j++)
				{
					if (this.squares[i][j].getState() == SquareButtonState.HIDDEN)
					{
						this.squares[i][j].rightClick();
					}
				}
			}
		}
	}
	
	/*
	 * Vérifie si la partie a été gagnée.
	 * 
	 */
	private void checkGameWon()
	{
		// Conditions de victoire :
		// 	 - Toutes les cases ont été révélées ou marquées.
		// 	 - Le compteur de cases marquées = le nombre de mines.
		if (this.areAllSquaresRevealedOrMarked() &&
				this.getMarkedSquaresCount() == this.nbMines)
		{
			this.onGameWon(new GameEvent());
		}
	}
	
	private boolean areAllHiddenSquaresMined()
	{
		boolean all = true;
		if (this.squares != null && this.squares[0] != null)
		{
			for (int i = 0 ; i < this.squares.length ; i++)
			{
				for (int j = 0 ; j < this.squares[0].length ; j++)
				{
					if (this.squares[i][j].getState() == SquareButtonState.HIDDEN &&
							!this.squares[i][j].isMined())
					{
						all = false;
					}
				}
			}
		}
		return all;
	}
	
	private boolean areAllSquaresRevealedOrMarked()
	{
		boolean all = true;
		if (this.squares != null && this.squares[0] != null)
		{
			for (int i = 0 ; i < this.squares.length ; i++)
			{
				for (int j = 0 ; j < this.squares[0].length ; j++)
				{
					if (this.squares[i][j].getState() != SquareButtonState.REVEALED &&
							this.squares[i][j].getState() != SquareButtonState.MARKED)
					{
						all = false;
					}
				}
			}
		}
		return all;
	}
	
	private int getMarkedSquaresCount()
	{
		int count = 0;
		if (this.squares != null && this.squares[0] != null)
		{
			for (int i = 0 ; i < this.squares.length ; i++)
			{
				for (int j = 0 ; j < this.squares[0].length ; j++)
				{
					if (this.squares[i][j].getState() == SquareButtonState.MARKED)
					{
						count++;
					}
				}
			}
		}
		return count;
	}
	
	private void onGameLost(GameEvent e)
	{
		for (GameListener listener : this.listeners)
		{
			listener.gameLost(e);
		}
	}
	
	private void onGameWon(GameEvent e)
	{
		for (GameListener listener : this.listeners)
		{
			listener.gameWon(e);
		}
	}
	
	private void onSquareMarked(GameEvent e)
	{
		for (GameListener listener : this.listeners)
		{
			listener.squareMarked(e);
		}
	}
	
	private void onSquareUnmarked(GameEvent e)
	{
		for (GameListener listener : this.listeners)
		{
			listener.squareUnmarked(e);
		}
	}
	
	private void drawGrid(Graphics g)
	{
		if (this.squares != null && this.squares[0] != null)
		{
			g.setColor(Color.BLACK);
			// Dessiner les lignes horizontales de la grille.
			for (int i = 0 ; i < this.squares.length ; i++)
			{
				g.drawLine(20 * (i + 1) + 1, 0, 20 * (i + 1) + 1, this.getHeight());
			}
		
			// Dessiner les lignes verticales de la grille.
			for (int j = 0 ; j < this.squares[0].length ; j++)
			{
				g.drawLine(0, 20 * (j + 1) + 1, this.getWidth(), 20 * (j + 1) + 1);
			}
		}
	}
	
	private void drawImages(Graphics g)
	{
		if (this.squares != null && this.squares[0] != null)
		{
			// Dessiner les chiffres.
			for (int i = 0 ; i < this.squares.length ; i++)
			{
				for (int j = 0 ; j < this.squares[0].length ; j++)
				{
					if (this.squares[i][j].getState() == SquareButtonState.REVEALED)
					{
						if (this.squares[i][j].isMined())
						{
							if (this.squares[i][j].equalCoords(this.hitSquare))
							{
								g.drawImage(this.imgMineHit, i * 20 + 2, j * 20 + 2, null);
							}
							else
							{
								g.drawImage(this.imgMine, i * 20 + 2, j * 20 + 2, null);
							}
						}
						else
						{
							if (this.squares[i][j].wasFlagged())
							{
								g.drawImage(this.imgMineWrong, i * 20 + 2, j * 20 + 2, null);
							}
						}
						
						switch (this.squares[i][j].getNeighboorMinesCount())
						{
							case 1:
								g.drawImage(this.img1, i * 20 + 1, j * 20 + 1, null);
								break;
							case 2:
								g.drawImage(this.img2, i * 20 + 1, j * 20 + 1, null);
								break;
							case 3:
								g.drawImage(this.img3, i * 20 + 1, j * 20 + 1, null);
								break;
							case 4:
								g.drawImage(this.img4, i * 20 + 1, j * 20 + 1, null);
								break;
							case 5:
								g.drawImage(this.img5, i * 20 + 1, j * 20 + 1, null);
								break;
							case 6:
								g.drawImage(this.img6, i * 20 + 1, j * 20 + 1, null);
								break;
							case 7:
								g.drawImage(this.img7, i * 20 + 1, j * 20 + 1, null);
								break;
							case 8:
								g.drawImage(this.img8, i * 20 + 1, j * 20 + 1, null);
								break;
						}
					}
					else if (this.squares[i][j].getState() == SquareButtonState.CHEATED)
					{
						g.drawImage(this.imgMineCheated, i * 20 + 2, j * 20 + 1, null);
					}
				}
			}
		}
	}
	
	public void addGameListener(GameListener listener)
	{
		this.listeners.add(listener);
	}
	
	public void paintComponent(Graphics g)
	{
		// Dessiner la grille en dessous des composants.
		super.paintComponent(g);
		if (this.squares != null && this.squares[0] != null)
		{
			this.drawGrid(g);
			
			this.drawImages(g);
		}
	}
	
	public void startGame(int squaresPerRow, int squaresPerColumn, int mines)
	{
		if (squaresPerRow >= GameGrid.MIN_SQUARES_IN_A_LINE && squaresPerRow <= GameGrid.MAX_SQUARES_IN_A_LINE
			&& squaresPerColumn >= GameGrid.MIN_SQUARES_IN_A_LINE && squaresPerColumn <= GameGrid.MAX_SQUARES_IN_A_LINE)
		{
			// Stocke le nombre de mines de la grille.
			// (utilisé pour la génération par la suite).
			this.nbMines = mines;
			// Si les dimensions ne changent pas,
			// réutilise la grille au lieu de la recréer.
			// Petite optimisation ...
			if (squaresPerRow == this.squaresPerRow &&
					squaresPerColumn == this.squaresPerColumn)
			{
				//System.out.println("Réinitialisation de la grille");
				this.resetSquareButtons();
			}
			else
			{
				this.squaresPerRow = squaresPerRow;
				this.squaresPerColumn = squaresPerColumn;
				//System.out.println("Création d'une nouvelle grille");
				this.createSquares(squaresPerRow, squaresPerColumn);
			}
		}
	}

	public void mouseClicked(MouseEvent e)
	{}

	public void mouseEntered(MouseEvent e)
	{}

	public void mouseExited(MouseEvent e)
	{}

	public void mousePressed(MouseEvent e)
	{
		if (AppFrame.getInstance().getGameBoard().isInGame())
			AppFrame.getInstance().getGameBoard().indicateMousePressed();
	}

	public void mouseReleased(MouseEvent e)
	{
		if (AppFrame.getInstance().getGameBoard().isInGame())
		{
			AppFrame.getInstance().getGameBoard().indicateMouseReleased();
			SquareButton square = (SquareButton)e.getSource();
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				this.squareButton_leftClick(square);
			}
			else if (e.isPopupTrigger())
			{
				this.squareButton_rightClick(square);
			}
		}
	}

	public void squareMarked(GameEvent e)
	{
		this.onSquareMarked(e);
	}

	public void squareUnmarked(GameEvent e)
	{
		this.onSquareUnmarked(e);
	}
}
