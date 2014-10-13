package minesweeper.form;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import minesweeper.Loader;
import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.GameListener;
import minesweeper.model.event.SquareButtonListener;

import com.google.common.collect.Lists;

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
	private List<GameListener> listeners = Lists.newArrayList();
	// Images de num�ros.
	private final Image img1 = Loader.getImage("num1.gif");
	private final Image img2 = Loader.getImage("num2.gif");
	private final Image img3 = Loader.getImage("num3.gif");
	private final Image img4 = Loader.getImage("num4.gif");
	private final Image img5 = Loader.getImage("num5.gif");
	private final Image img6 = Loader.getImage("num6.gif");
	private final Image img7 = Loader.getImage("num7.gif");
	private final Image img8 = Loader.getImage("num8.gif");

	private final Image imgMineWrong = Loader.getImage("mine_wrong.gif");
	private final Image imgMine = Loader.getImage("mine.gif");
	private final Image imgMineHit = Loader.getImage("mine_hit.gif");
	private final Image imgMineCheated = Loader.getImage("mine_cheated.gif");
	// Nombre de cases en largeur de la grille.
	private int squaresPerRow;
	// Nombre de cases en hauteur de la grille.
	private int squaresPerColumn;
	// Nombre de mines dans la grille.
	private int mines;
	// Case contenant la mine qui a �t� cliqu�e (qui a caus�
	// la d�faite du joueur).
	private SquareButton hitSquare;

	private final GameServices gameServices;

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
	public GameGrid(final GameServices gameServices) {
		super();

		this.gameServices = gameServices;

		setBorder(BorderFactory.createLoweredBevelBorder());
	}

	/**
	 * Active le mode de triche en trichant sur chaque case.
	 * 
	 */
	public void cheat() {
		if (isGridNotCreated()) {
			return;
		}

		for (int j = 0; j < squares[0].length; j++) {
			for (int i = 0; i < squares.length; i++) {
				squares[i][j].cheat();
			}
		}
	}

	private void createSquares(final int nbSquaresWidth, final int nbSquaresHeight) {
		squaresPerRow = nbSquaresWidth;
		squaresPerColumn = nbSquaresHeight;
		deleteSquares();
		setLayout(new GridLayout(nbSquaresHeight, nbSquaresWidth));
		squares = new SquareButton[nbSquaresWidth][nbSquaresHeight];
		for (int j = 0; j < squares[0].length; j++) {
			for (int i = 0; i < squares.length; i++) {
				squares[i][j] = new SquareButton(i, j);
				squares[i][j].addMouseListener(this);
				squares[i][j].addSquareButtonListener(this);
				squares[i][j].setBorder(BorderFactory.createRaisedBevelBorder());
				add(squares[i][j]);
			}
		}
	}

	private void deleteSquares() {
		removeAll();
		squares = null;
	}

	private void resetSquareButtons() {
		if (isGridNotCreated()) {
			return;
		}

		for (int j = 0; j < squares[0].length; j++) {
			for (int i = 0; i < squares.length; i++) {
				squares[i][j].reset();
			}
		}
	}

	/*
	 * G�n�rer les emplacements al�atoires des mines.
	 * 
	 */
	private void generateMines(final SquareButton squareToAvoid, int nbMines) {
		if (gameServices.isInGame() && !gameServices.isFirstClicked()) {
			if (nbMines > 0 && nbMines < getSquaresCount() - 1) {
				// Cr�er une collection de cases disponibles.
				// (les cases qui peuvent recevoir des mines)
				List<Point> openCoords = Lists.newArrayList();

				// Remplir le tableau des coordonn�es disponibles.
				for (int i = 0; i < squares.length; i++) {
					for (int j = 0; j < squares[0].length; j++) {
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
					squares[(int) openCoords.get(openIndex).getX()][(int) openCoords.get(openIndex).getY()].setMine();
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
	private int getSquaresCount() {
		if (isGridNotCreated()) {
			return 0;
		}

		return squares.length * squares[0].length;
	}

	/*
	 * M�thode permettant de r�v�ler une case, compter les
	 * mines alentour et au besoin, s'appeler r�cursivement
	 * pour r�v�ler les cases autour.
	 * 
	 */
	private void revealNeighboorSquares(final SquareButton square) {
		if (square.getState() == SquareButtonState.HIDDEN) {
			countNeighboorMines(square);
			square.reveal();
			if (square.getNeighboorMinesCount() == 0) {
				// La case n'a pas de mines avoisinantes.
				// R�v�ler les voisins.
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if (i != 0 || j != 0) {
							if (isSquare(square.getXSquare() + i, square.getYSquare() + j)) {
								revealNeighboorSquares(squares[square.getXSquare() + i][square.getYSquare() + j]);
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
		if (isGridNotCreated()) {
			return false;
		}

		return (x >= 0 && x < squares.length &&
					y >= 0 && y < squares[0].length);
	}

	/*
	 * Finalise une partie perdue en r�v�lant les mines, etc.
	 * 
	 */
	private void finishLostGame() {
		if (isGridNotCreated()) {
			return;
		}

		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				if ((squares[i][j].getState() == SquareButtonState.HIDDEN ||
							squares[i][j].getState() == SquareButtonState.UNSURE)
							&& squares[i][j].isMined()) {
					// R�v�ler la mine.
					squares[i][j].reveal();
				}
				if (squares[i][j].getState() == SquareButtonState.MARKED
							&& !squares[i][j].isMined()) {
					// Marquer la case comme min�e incorrectement.
					squares[i][j].setWasFlagged();
					// R�v�ler la case.
					squares[i][j].reveal();
				}
			}
		}

	}

	/*
	 * Compte le nombre de mines voisines d'une case.
	 * 
	 */
	private void countNeighboorMines(final SquareButton square) {
		if (isGridNotCreated()) {
			return;
		}

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					if (isSquare(square.getXSquare() + i, square.getYSquare() + j)
								&& squares[square.getXSquare() + i][square.getYSquare() + j].isMined()) {
						squares[square.getXSquare()][square.getYSquare()].incrementNeighboorMinesCount();
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
		if (!gameServices.isFirstClicked()) {
			if (square.getState() == SquareButtonState.HIDDEN) {
				generateMines(square, this.mines);
				revealNeighboorSquares(square);
				gameServices.firstClicked();
			}
		} else {
			// Clic normal (pendant la partie).
			if (square.isMined() && square.getState() == SquareButtonState.HIDDEN) {
				// La partie a �t� perdue.
				hitSquare = square;
				finishLostGame();
				square.reveal();
				onGameLost(new GameEvent());
			} else {
				revealNeighboorSquares(square);
				// Si les cases restantes sont toutes min�es, alors
				// les marquer automatiquement.
				if (areAllHiddenSquaresMined()) {
					markHiddenSquares();
				}

				checkGameWon();
			}
		}
	}

	private void squareButton_rightClick(final SquareButton square) {
		if (square.getState() != SquareButtonState.REVEALED &&
				square.getState() != SquareButtonState.CHEATED) {
			square.rightClick();
		}

		checkGameWon();
	}

	private void markHiddenSquares() {
		if (isGridNotCreated()) {
			return;
		}

		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				if (squares[i][j].getState() == SquareButtonState.HIDDEN) {
					squares[i][j].rightClick();
				}
			}
		}
	}

	private void checkGameWon() {
		// Conditions de victoire :
		// 	 - Toutes les cases ont �t� r�v�l�es ou marqu�es.
		// 	 - Le compteur de cases marqu�es = le nombre de mines.
		if (areAllSquaresRevealedOrMarked() &&
				getMarkedSquaresCount() == mines) {
			onGameWon(new GameEvent());
		}
	}

	private boolean areAllHiddenSquaresMined() {
		boolean all = true;
		if (squares != null && squares[0] != null) {
			for (int i = 0; i < squares.length; i++) {
				for (int j = 0; j < squares[0].length; j++) {
					if (squares[i][j].getState() == SquareButtonState.HIDDEN &&
							!squares[i][j].isMined()) {
						all = false;
					}
				}
			}
		}
		return all;
	}

	private boolean areAllSquaresRevealedOrMarked() {
		if (isGridNotCreated()) {
			return true;
		}

		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				if (squares[i][j].getState() != SquareButtonState.REVEALED &&
							squares[i][j].getState() != SquareButtonState.MARKED) {
					return false;
				}
			}
		}
		return true;
	}

	private int getMarkedSquaresCount() {
		if (isGridNotCreated()) {
			return 0;
		}

		int count = 0;
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				if (squares[i][j].getState() == SquareButtonState.MARKED) {
					count++;
				}
			}
		}
		return count;
	}

	private void onGameLost(final GameEvent e) {
		for (GameListener listener : listeners) {
			listener.gameLost(e);
		}
	}

	private void onGameWon(final GameEvent e) {
		for (GameListener listener : listeners) {
			listener.gameWon(e);
		}
	}

	private void onSquareMarked(final GameEvent e) {
		for (GameListener listener : listeners) {
			listener.squareMarked(e);
		}
	}

	private void onSquareUnmarked(final GameEvent e) {
		for (GameListener listener : listeners) {
			listener.squareUnmarked(e);
		}
	}

	private void drawGrid(final Graphics g) {
		g.setColor(Color.BLACK);
		// Dessiner les lignes horizontales de la grille.
		for (int i = 0; i < squares.length; i++) {
			g.drawLine(20 * (i + 1) + 1, 0, 20 * (i + 1) + 1, this.getHeight());
		}

		// Dessiner les lignes verticales de la grille.
		for (int j = 0; j < squares[0].length; j++) {
			g.drawLine(0, 20 * (j + 1) + 1, this.getWidth(), 20 * (j + 1) + 1);
		}
	}

	private boolean isGridNotCreated() {
		return squares == null || squares[0] == null;
	}

	private void drawImages(final Graphics g) {
		// Dessiner les chiffres.
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				if (squares[i][j].getState() == SquareButtonState.REVEALED) {
					if (squares[i][j].isMined()) {
						if (squares[i][j].equalCoords(hitSquare)) {
							g.drawImage(imgMineHit, i * 20 + 2, j * 20 + 2, null);
						} else {
							g.drawImage(imgMine, i * 20 + 2, j * 20 + 2, null);
						}
					} else {
						if (squares[i][j].wasFlagged()) {
							g.drawImage(imgMineWrong, i * 20 + 2, j * 20 + 2, null);
						}
					}

					drawNumberImage(g, i, j);
				} else if (squares[i][j].getState() == SquareButtonState.CHEATED) {
					g.drawImage(imgMineCheated, i * 20 + 2, j * 20 + 1, null);
				}
			}
		}
	}

	private void drawNumberImage(final Graphics g, final int i, final int j) {
		switch (squares[i][j].getNeighboorMinesCount()) {
			case 1:
				g.drawImage(img1, i * 20 + 1, j * 20 + 1, null);
				break;
			case 2:
				g.drawImage(img2, i * 20 + 1, j * 20 + 1, null);
				break;
			case 3:
				g.drawImage(img3, i * 20 + 1, j * 20 + 1, null);
				break;
			case 4:
				g.drawImage(img4, i * 20 + 1, j * 20 + 1, null);
				break;
			case 5:
				g.drawImage(img5, i * 20 + 1, j * 20 + 1, null);
				break;
			case 6:
				g.drawImage(img6, i * 20 + 1, j * 20 + 1, null);
				break;
			case 7:
				g.drawImage(img7, i * 20 + 1, j * 20 + 1, null);
				break;
			case 8:
				g.drawImage(img8, i * 20 + 1, j * 20 + 1, null);
				break;
		}
	}

	public void addGameListener(final GameListener listener) {
		listeners.add(listener);
	}

	@Override
	public void paintComponent(final Graphics g) {
		// Dessiner la grille en dessous des composants.
		super.paintComponent(g);

		if (isGridNotCreated()) {
			return;
		}

		drawGrid(g);
		drawImages(g);
	}

	public void startGame(final int squaresPerRow, final int squaresPerColumn, final int mines) {
		if (squaresPerRow >= GameGrid.MIN_SQUARES_IN_A_LINE && squaresPerRow <= GameGrid.MAX_SQUARES_IN_A_LINE
				&& squaresPerColumn >= GameGrid.MIN_SQUARES_IN_A_LINE && squaresPerColumn <= GameGrid.MAX_SQUARES_IN_A_LINE) {
			// Stocke le nombre de mines de la grille.
			// (utilis� pour la g�n�ration par la suite).
			this.mines = mines;
			// Si les dimensions ne changent pas,
			// r�utilise la grille au lieu de la recr�er.
			// Petite optimisation ...
			if (squaresPerRow == this.squaresPerRow &&
					squaresPerColumn == this.squaresPerColumn) {
				resetSquareButtons();
			} else {
				createSquares(squaresPerRow, squaresPerColumn);
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
		if (gameServices.isInGame()) {
			gameServices.indicateMousePressed();
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		if (gameServices.isInGame()) {
			gameServices.indicateMouseReleased();
			SquareButton square = (SquareButton) e.getSource();
			if (e.getButton() == MouseEvent.BUTTON1) {
				squareButton_leftClick(square);
			} else if (e.isPopupTrigger()) {
				squareButton_rightClick(square);
			}
		}
	}

	@Override
	public void squareMarked(final GameEvent e) {
		onSquareMarked(e);
	}

	@Override
	public void squareUnmarked(final GameEvent e) {
		onSquareUnmarked(e);
	}
}