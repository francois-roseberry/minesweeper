package minesweeper.form;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.GameListener;
import minesweeper.model.event.SquareButtonListener;
import util.ImageUtil;

/**
 * La classe GameGrid repr�sente la grille de jeu et contient toutes les cases (boutons).
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
@SuppressWarnings("serial")
public class GameGrid extends JPanel implements MouseListener, SquareButtonListener {

	// Tableau des cases de la grille.
	// On garde un tableau des cases du jeu m�me si tous
	// les composants sont des cases donc th�oriquement on
	// pourrait y acc�der par getComponents() mais comme
	// il faudrait faire �norm�ment trop de castings (co�teux
	// en performances) on garde les r�f�rences dans un
	// tableau.
	// Aussi, le tableau de components a une seule dimension
	// tandis qu'un tableau de squares a 2 dimensions.
	private SquareButton[][] squares = null;
	// Liste des �couteurs GameListener de cette classe.
	private List<GameListener> listeners = new ArrayList<GameListener>();
	// Images de num�ros.
	private Image img1 = ImageUtil.getImage(getClass(), "num1.gif");
	private Image img2 = ImageUtil.getImage(getClass(), "num2.gif");
	private Image img3 = ImageUtil.getImage(getClass(), "num3.gif");
	private Image img4 = ImageUtil.getImage(getClass(), "num4.gif");
	private Image img5 = ImageUtil.getImage(getClass(), "num5.gif");
	private Image img6 = ImageUtil.getImage(getClass(), "num6.gif");
	private Image img7 = ImageUtil.getImage(getClass(), "num7.gif");
	private Image img8 = ImageUtil.getImage(getClass(), "num8.gif");

	private Image imgMineWrong = ImageUtil.getImage(getClass(), "mine_wrong.gif");
	private Image imgMine = ImageUtil.getImage(getClass(), "mine.gif");
	private Image imgMineHit = ImageUtil.getImage(getClass(), "mine_hit.gif");
	private Image imgMineCheated = ImageUtil.getImage(getClass(), "mine_cheated.gif");
	// Nombre de cases en largeur de la grille.
	private int squaresPerRow;
	// Nombre de cases en hauteur de la grille.
	private int squaresPerColumn;
	// Nombre de mines dans la grille.
	private int nbMines;
	// Case contenant la mine qui a �t� cliqu�e (qui a caus�
	// la d�faite du joueur).
	private SquareButton hitSquare;

	/**
	 * Nombre de mines � placer dans la grille pour chaque niveau.
	 * 
	 */
	public static final int[] MINES_PER_LEVEL = new int[] { 10, 40, 99 };
	/**
	 * Nombre de carr�s minimum accept�s en hauteur ou en largeur.
	 * 
	 */
	public static final int MIN_SQUARES_IN_A_LINE = 9;
	/**
	 * Nombre de carr�s maximum accept�s en hauteur ou en largeur.
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
	 * Constructeur. Cr�e une grille vide.
	 * 
	 */
	public GameGrid() {
		super();
		this.setBorder(BorderFactory.createLoweredBevelBorder());
	}

	/**
	 * Active le mode de triche en trichant sur chaque case.
	 * 
	 */
	public void cheat() {
		if (this.squares != null && this.squares[0] != null) {
			for (int j = 0; j < this.squares[0].length; j++) {
				for (int i = 0; i < this.squares.length; i++) {
					this.squares[i][j].cheat();
				}
			}
		}
	}

	private void createSquares(final int nbSquaresWidth, final int nbSquaresHeight) {
		this.deleteSquares();
		this.setLayout(new GridLayout(nbSquaresHeight, nbSquaresWidth));
		this.squares = new SquareButton[nbSquaresWidth][nbSquaresHeight];
		for (int j = 0; j < this.squares[0].length; j++) {
			for (int i = 0; i < this.squares.length; i++) {
				this.squares[i][j] = new SquareButton(i, j);
				this.squares[i][j].addMouseListener(this);
				this.squares[i][j].addSquareButtonListener(this);
				this.squares[i][j].setBorder(BorderFactory.createRaisedBevelBorder());
				this.add(this.squares[i][j]);
			}
		}
	}

	private void deleteSquares() {
		// Tous les composants sont des cases de jeu, on
		// les enl�ve donc tous.
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

	private void resetSquareButtons() {
		if (this.squares != null && this.squares[0] != null) {
			for (int j = 0; j < this.squares[0].length; j++) {
				for (int i = 0; i < this.squares.length; i++) {
					this.squares[i][j].reset();
				}
			}
		}
	}

	/*
	 * G�n�rer les emplacements al�atoires des mines.
	 * 
	 */
	private void generateMines(final SquareButton squareToAvoid, int nbMines) {
		if (AppFrame.getInstance().getGameBoard().isInGame() && !AppFrame.getInstance().getGameBoard().isFirstClicked()) {
			if (nbMines > 0 && nbMines < this.getNumSquares() - 1) {
				// Cr�er une collection de cases disponibles.
				// (les cases qui peuvent recevoir des mines)
				ArrayList<Point> openCoords = new ArrayList<Point>();

				// Remplir le tableau des coordonn�es disponibles.
				for (int i = 0; i < this.squares.length; i++) {
					for (int j = 0; j < this.squares[0].length; j++) {
						if (!squareToAvoid.equalCoords(i, j)) {
							openCoords.add(new Point(i, j));
						}
					}
				}
				int openIndex;
				Random rnd = new Random();
				while (nbMines > 0) {
					// G�n�rer un index al�atoire.
					openIndex = rnd.nextInt(openCoords.size());
					this.squares[(int) openCoords.get(openIndex).getX()][(int) openCoords.get(openIndex).getY()].setMine();
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
	private int getNumSquares() {
		int numSquares = 0;
		if (this.squares != null && this.squares[0] != null) {
			numSquares = this.squares.length * this.squares[0].length;
		}
		return numSquares;
	}

	/*
	 * M�thode permettant de r�v�ler une case, compter les
	 * mines alentour et au besoin, s'appeler r�cursivement
	 * pour r�v�ler les cases autour.
	 * 
	 */
	private void revealNeighboorSquares(final SquareButton square) {
		if (square.getState() == SquareButtonState.HIDDEN) {
			this.countNeighboorMines(square);
			square.reveal();
			if (square.getNeighboorMinesCount() == 0) {
				// La case n'a pas de mines avoisinantes.
				// R�v�ler les voisins.
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if (i != 0 || j != 0) {
							if (this.isSquare(square.getXSquare() + i, square.getYSquare() + j)) {
								this.revealNeighboorSquares(this.squares[square.getXSquare() + i][square.getYSquare() + j]);
							}
						}
					}
				}
			}
		}
	}

	/*
	 * V�rifie si les coordonn�es fournies (plut�t indices de
	 * tableau) correspondent � une case de la grille.
	 * 
	 */
	private boolean isSquare(final int x, final int y) {
		boolean isSquare = false;
		if (this.squares != null && this.squares[0] != null) {
			if (x >= 0 && x < this.squares.length &&
					y >= 0 && y < this.squares[0].length) {
				isSquare = true;
			}
		}
		return isSquare;
	}

	/*
	 * Finalise une partie perdue en r�v�lant les mines, etc.
	 * 
	 */
	private void finishLostGame() {
		if (this.squares != null && this.squares[0] != null) {
			for (int i = 0; i < this.squares.length; i++) {
				for (int j = 0; j < this.squares[0].length; j++) {
					if ((this.squares[i][j].getState() == SquareButtonState.HIDDEN ||
							this.squares[i][j].getState() == SquareButtonState.UNSURE)
							&& this.squares[i][j].isMined()) {
						// R�v�ler la mine.
						this.squares[i][j].reveal();
					}
					if (this.squares[i][j].getState() == SquareButtonState.MARKED
							&& !this.squares[i][j].isMined()) {
						// Marquer la case comme min�e incorrectement.
						this.squares[i][j].setWasFlagged();
						// R�v�ler la case.
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
	private void countNeighboorMines(final SquareButton square) {
		if (this.squares != null && this.squares[0] != null) {
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (i != 0 || j != 0) {
						if (this.isSquare(square.getXSquare() + i, square.getYSquare() + j)
								&& this.squares[square.getXSquare() + i][square.getYSquare() + j].isMined()) {
							this.squares[square.getXSquare()][square.getYSquare()].incrementNeighboorMinesCount();
						}
					}
				}
			}
		}
	}

	/*
	 * G�re le clic gauche sur une case.
	 * 
	 */
	private void squareButton_leftClick(final SquareButton square) {
		// Premier clique de la partie.
		if (!AppFrame.getInstance().getGameBoard().isFirstClicked()) {
			if (square.getState() == SquareButtonState.HIDDEN) {
				// G�n�rer les mines.
				this.generateMines(square, this.nbMines);
				// D�couvrir la case qui a �t� cliqu�e.
				this.revealNeighboorSquares(square);
				// Indiquer que le premier clique a �t� r�alis�.
				AppFrame.getInstance().getGameBoard().firstClicked();
			}
		} else {
			// Clic normal (pendant la partie).
			if (square.isMined() && square.getState() == SquareButtonState.HIDDEN) {
				// La partie a �t� perdue.
				this.hitSquare = square;
				this.finishLostGame();
				square.reveal();
				this.onGameLost(new GameEvent());
			} else {
				this.revealNeighboorSquares(square);
				// Si les cases restantes sont toutes min�es, alors
				// les marquer automatiquement.
				if (this.areAllHiddenSquaresMined()) {
					this.markHiddenSquares();
				}

				// V�rifier si la partie a �t� gagn�e.
				this.checkGameWon();
			}
		}
	}

	/*
	 * G�re le clique droit sur une case.
	 * 
	 */
	private void squareButton_rightClick(final SquareButton square) {
		if (square.getState() != SquareButtonState.REVEALED &&
				square.getState() != SquareButtonState.CHEATED) {
			square.rightClick();
		}
		// V�rifier si la partie a �t� gagn�e.
		this.checkGameWon();
	}

	private void markHiddenSquares() {
		if (this.squares != null && this.squares[0] != null) {
			for (int i = 0; i < this.squares.length; i++) {
				for (int j = 0; j < this.squares[0].length; j++) {
					if (this.squares[i][j].getState() == SquareButtonState.HIDDEN) {
						this.squares[i][j].rightClick();
					}
				}
			}
		}
	}

	/*
	 * V�rifie si la partie a �t� gagn�e.
	 * 
	 */
	private void checkGameWon() {
		// Conditions de victoire :
		// 	 - Toutes les cases ont �t� r�v�l�es ou marqu�es.
		// 	 - Le compteur de cases marqu�es = le nombre de mines.
		if (this.areAllSquaresRevealedOrMarked() &&
				this.getMarkedSquaresCount() == this.nbMines) {
			this.onGameWon(new GameEvent());
		}
	}

	private boolean areAllHiddenSquaresMined() {
		boolean all = true;
		if (this.squares != null && this.squares[0] != null) {
			for (int i = 0; i < this.squares.length; i++) {
				for (int j = 0; j < this.squares[0].length; j++) {
					if (this.squares[i][j].getState() == SquareButtonState.HIDDEN &&
							!this.squares[i][j].isMined()) {
						all = false;
					}
				}
			}
		}
		return all;
	}

	private boolean areAllSquaresRevealedOrMarked() {
		boolean all = true;
		if (this.squares != null && this.squares[0] != null) {
			for (int i = 0; i < this.squares.length; i++) {
				for (int j = 0; j < this.squares[0].length; j++) {
					if (this.squares[i][j].getState() != SquareButtonState.REVEALED &&
							this.squares[i][j].getState() != SquareButtonState.MARKED) {
						all = false;
					}
				}
			}
		}
		return all;
	}

	private int getMarkedSquaresCount() {
		int count = 0;
		if (this.squares != null && this.squares[0] != null) {
			for (int i = 0; i < this.squares.length; i++) {
				for (int j = 0; j < this.squares[0].length; j++) {
					if (this.squares[i][j].getState() == SquareButtonState.MARKED) {
						count++;
					}
				}
			}
		}
		return count;
	}

	private void onGameLost(final GameEvent e) {
		for (GameListener listener : this.listeners) {
			listener.gameLost(e);
		}
	}

	private void onGameWon(final GameEvent e) {
		for (GameListener listener : this.listeners) {
			listener.gameWon(e);
		}
	}

	private void onSquareMarked(final GameEvent e) {
		for (GameListener listener : this.listeners) {
			listener.squareMarked(e);
		}
	}

	private void onSquareUnmarked(final GameEvent e) {
		for (GameListener listener : this.listeners) {
			listener.squareUnmarked(e);
		}
	}

	private void drawGrid(final Graphics g) {
		if (this.squares != null && this.squares[0] != null) {
			g.setColor(Color.BLACK);
			// Dessiner les lignes horizontales de la grille.
			for (int i = 0; i < this.squares.length; i++) {
				g.drawLine(20 * (i + 1) + 1, 0, 20 * (i + 1) + 1, this.getHeight());
			}

			// Dessiner les lignes verticales de la grille.
			for (int j = 0; j < this.squares[0].length; j++) {
				g.drawLine(0, 20 * (j + 1) + 1, this.getWidth(), 20 * (j + 1) + 1);
			}
		}
	}

	private void drawImages(final Graphics g) {
		if (this.squares != null && this.squares[0] != null) {
			// Dessiner les chiffres.
			for (int i = 0; i < this.squares.length; i++) {
				for (int j = 0; j < this.squares[0].length; j++) {
					if (this.squares[i][j].getState() == SquareButtonState.REVEALED) {
						if (this.squares[i][j].isMined()) {
							if (this.squares[i][j].equalCoords(this.hitSquare)) {
								g.drawImage(this.imgMineHit, i * 20 + 2, j * 20 + 2, null);
							} else {
								g.drawImage(this.imgMine, i * 20 + 2, j * 20 + 2, null);
							}
						} else {
							if (this.squares[i][j].wasFlagged()) {
								g.drawImage(this.imgMineWrong, i * 20 + 2, j * 20 + 2, null);
							}
						}

						switch (this.squares[i][j].getNeighboorMinesCount()) {
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
					} else if (this.squares[i][j].getState() == SquareButtonState.CHEATED) {
						g.drawImage(this.imgMineCheated, i * 20 + 2, j * 20 + 1, null);
					}
				}
			}
		}
	}

	public void addGameListener(final GameListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void paintComponent(final Graphics g) {
		// Dessiner la grille en dessous des composants.
		super.paintComponent(g);
		if (this.squares != null && this.squares[0] != null) {
			this.drawGrid(g);

			this.drawImages(g);
		}
	}

	public void startGame(final int squaresPerRow, final int squaresPerColumn, final int mines) {
		if (squaresPerRow >= GameGrid.MIN_SQUARES_IN_A_LINE && squaresPerRow <= GameGrid.MAX_SQUARES_IN_A_LINE
				&& squaresPerColumn >= GameGrid.MIN_SQUARES_IN_A_LINE && squaresPerColumn <= GameGrid.MAX_SQUARES_IN_A_LINE) {
			// Stocke le nombre de mines de la grille.
			// (utilis� pour la g�n�ration par la suite).
			this.nbMines = mines;
			// Si les dimensions ne changent pas,
			// r�utilise la grille au lieu de la recr�er.
			// Petite optimisation ...
			if (squaresPerRow == this.squaresPerRow &&
					squaresPerColumn == this.squaresPerColumn) {
				//System.out.println("R�initialisation de la grille");
				this.resetSquareButtons();
			} else {
				this.squaresPerRow = squaresPerRow;
				this.squaresPerColumn = squaresPerColumn;
				//System.out.println("Cr�ation d'une nouvelle grille");
				this.createSquares(squaresPerRow, squaresPerColumn);
			}
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {}

	@Override
	public void mouseEntered(final MouseEvent e) {}

	@Override
	public void mouseExited(final MouseEvent e) {}

	@Override
	public void mousePressed(final MouseEvent e) {
		if (AppFrame.getInstance().getGameBoard().isInGame()) {
			AppFrame.getInstance().getGameBoard().indicateMousePressed();
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		if (AppFrame.getInstance().getGameBoard().isInGame()) {
			AppFrame.getInstance().getGameBoard().indicateMouseReleased();
			SquareButton square = (SquareButton) e.getSource();
			if (e.getButton() == MouseEvent.BUTTON1) {
				this.squareButton_leftClick(square);
			} else if (e.isPopupTrigger()) {
				this.squareButton_rightClick(square);
			}
		}
	}

	@Override
	public void squareMarked(final GameEvent e) {
		this.onSquareMarked(e);
	}

	@Override
	public void squareUnmarked(final GameEvent e) {
		this.onSquareUnmarked(e);
	}
}
