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
import minesweeper.model.GridSize;
import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.GameListener;
import minesweeper.model.event.SquareButtonListener;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * La classe GameGrid repr�sente la grille de jeu et contient toutes les cases
 * (boutons).
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
@SuppressWarnings("serial")
public class GameGrid extends JPanel implements MouseListener,
		SquareButtonListener {

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
	private GridSize size;
	// Nombre de mines dans la grille.
	private int mines;
	// Case contenant la mine qui a �t� cliqu�e (qui a caus�
	// la d�faite du joueur).
	private SquareButton hitSquare;

	private final GameServices gameServices;
	private final SquareButtonProvider provider;

	/**
	 * Nombre de mines � placer dans la grille pour chaque niveau.
	 * 
	 */
	public static final int[] MINES_PER_LEVEL = new int[] { 10, 40, 99 };
	/**
	 * Nombre de carr�s minimum accept�s en hauteur ou en largeur.
	 * 
	 */
	public static final int MIN_ROWS = 9;
	/**
	 * Nombre de carr�s maximum accept�s en hauteur ou en largeur.
	 * 
	 */
	public static final int MAX_ROWS = 32;
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
	public GameGrid(final GameServices gameServices,
			final SquareButtonProvider provider) {
		super();

		this.gameServices = gameServices;
		this.provider = provider;

		setBorder(BorderFactory.createLoweredBevelBorder());
	}

	/**
	 * Active le mode de triche en trichant sur chaque case.
	 * 
	 */
	public void cheat() {
		Preconditions.checkArgument(isGridCreated());

		for (SquareButton square : getSquareButtons()) {
			square.cheat();
		}
	}

	private void createSquares(final GridSize size) {
		this.size = size;
		deleteSquares();
		setLayout(new GridLayout(size.columns(), size.rows()));
		squares = new SquareButton[size.rows()][size.columns()];
		for (int column = 0; column < squares[0].length; column++) {
			for (int row = 0; row < squares.length; row++) {
				squares[row][column] = createSquareButton(column, row);
				add(squares[row][column]);
			}
		}
	}

	private SquareButton createSquareButton(final int column, final int row) {
		SquareButton square = provider.create(row, column);
		square.addMouseListener(this);
		square.addSquareButtonListener(this);
		square.setBorder(BorderFactory.createRaisedBevelBorder());
		return square;
	}

	private void deleteSquares() {
		removeAll();
		squares = null;
	}

	private ImmutableList<SquareButton> getSquareButtons() {
		ImmutableList.Builder<SquareButton> builder = ImmutableList.builder();
		for (int j = 0; j < squares[0].length; j++) {
			for (int i = 0; i < squares.length; i++) {
				builder.add(squares[i][j]);
			}
		}
		return builder.build();
	}

	private void resetSquareButtons() {
		for (SquareButton square : getSquareButtons()) {
			square.reset();
		}
	}

	/*
	 * G�n�rer les emplacements al�atoires des mines.
	 */
	private void generateMines(final SquareButton squareToAvoid, int mines) {
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
		while (mines > 0) {
			// G�n�rer un index al�atoire.
			openIndex = rnd.nextInt(openCoords.size());
			squares[(int) openCoords.get(openIndex).getX()][(int) openCoords
					.get(openIndex).getY()].setMine();
			// Enlever la case de la liste.
			openCoords.remove(openIndex);
			mines--;
		}
	}

	/*
	 * M�thode permettant de r�v�ler une case, compter les mines alentour et au
	 * besoin, s'appeler r�cursivement pour r�v�ler les cases autour.
	 */
	private void revealNeighboorSquares(final SquareButton square) {
		if (square.getState() == SquareButtonState.HIDDEN) {
			square.reveal();
			countNeighboorMines(square);
			if (square.getNeighboorMineCount() == 0) {
				// La case n'a pas de mines avoisinantes.
				// Révéler les voisins, récursivement.
				for (SquareButton neighboor : getNeighboors(square)) {
					revealNeighboorSquares(neighboor);
				}
			}
		}
	}

	private ImmutableList<SquareButton> getNeighboors(final SquareButton square) {
		ImmutableList.Builder<SquareButton> builder = ImmutableList.builder();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					if (isInGrid(square.getXSquare() + i, square.getYSquare()
							+ j)) {
						builder.add(squares[square.getXSquare() + i][square
								.getYSquare() + j]);
					}
				}
			}
		}
		return builder.build();
	}

	/*
	 * V�rifie si les coordonn�es fournies (plut�t indices de tableau)
	 * correspondent � une case de la grille.
	 */
	private boolean isInGrid(final int row, final int column) {
		return (row >= 0 && row < squares.length && column >= 0 && column < squares[0].length);
	}

	/*
	 * Finalise une partie perdue en r�v�lant les mines, etc.
	 */
	private void finishLostGame() {
		for (SquareButton square : getSquareButtons()) {
			if ((square.getState() == SquareButtonState.HIDDEN || square
					.getState() == SquareButtonState.UNSURE)
					&& square.isMined()) {
				// R�v�ler la mine.
				square.reveal();
			}
			if (square.getState() == SquareButtonState.MARKED
					&& !square.isMined()) {
				// Marquer la case comme min�e incorrectement.
				square.setWasFlagged();
				// R�v�ler la case.
				square.reveal();
			}
		}
	}

	/*
	 * Compte le nombre de mines voisines d'une case.
	 */
	private void countNeighboorMines(final SquareButton square) {
		int mines = 0;
		for (SquareButton neighboor : getNeighboors(square)) {
			if (neighboor.isMined()) {
				mines++;
			}
		}

		squares[square.getXSquare()][square.getYSquare()]
				.setNeighboorMineCount(mines);
	}

	/*
	 * G�re le clic gauche sur une case.
	 */
	private void squareButton_leftClick(final SquareButton square) {
		Preconditions.checkArgument(isGridCreated());
		// Premier clique de la partie.
		if (!gameServices.isFirstClicked()) {
			if (square.getState() == SquareButtonState.HIDDEN) {
				generateMines(square, this.mines);
				revealNeighboorSquares(square);
				gameServices.firstClicked();
			}
		} else {
			// Clic normal (pendant la partie).
			if (square.isMined()
					&& square.getState() == SquareButtonState.HIDDEN) {
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
		if (square.getState() != SquareButtonState.REVEALED
				&& square.getState() != SquareButtonState.CHEATED) {
			square.rightClick();
		}

		checkGameWon();
	}

	private void markHiddenSquares() {
		for (SquareButton square : getSquareButtons()) {
			if (square.getState() == SquareButtonState.HIDDEN) {
				square.rightClick();
			}
		}
	}

	private void checkGameWon() {
		// Conditions de victoire :
		// - Toutes les cases ont �t� r�v�l�es ou marqu�es.
		// - Le compteur de cases marqu�es = le nombre de mines.
		if (areAllSquaresRevealedOrMarked() && getMarkedSquaresCount() == mines) {
			onGameWon(new GameEvent());
		}
	}

	private boolean areAllHiddenSquaresMined() {
		for (SquareButton square : getSquareButtons()) {
			if (square.getState() == SquareButtonState.HIDDEN
					&& !square.isMined()) {
				return false;
			}
		}
		return true;
	}

	private boolean areAllSquaresRevealedOrMarked() {
		for (SquareButton square : getSquareButtons()) {
			if (square.getState() != SquareButtonState.REVEALED
					&& square.getState() != SquareButtonState.MARKED) {
				return false;
			}
		}
		return true;
	}

	private int getMarkedSquaresCount() {
		int count = 0;
		for (SquareButton square : getSquareButtons()) {
			if (square.getState() == SquareButtonState.MARKED) {
				count++;
			}
		}
		return count;
	}

	private void onGameLost(final GameEvent event) {
		for (GameListener listener : listeners) {
			listener.gameLost(event);
		}
	}

	private void onGameWon(final GameEvent event) {
		for (GameListener listener : listeners) {
			listener.gameWon(event);
		}
	}

	private void onSquareMarked(final GameEvent event) {
		for (GameListener listener : listeners) {
			listener.squareMarked(event);
		}
	}

	private void onSquareUnmarked(final GameEvent event) {
		for (GameListener listener : listeners) {
			listener.squareUnmarked(event);
		}
	}

	private void drawGrid(final Graphics graphics) {
		graphics.setColor(Color.BLACK);
		// Dessiner les lignes horizontales de la grille.
		for (int row = 0; row < squares.length; row++) {
			graphics.drawLine(20 * (row + 1) + 1, 0, 20 * (row + 1) + 1,
					this.getHeight());
		}

		// Dessiner les lignes verticales de la grille.
		for (int column = 0; column < squares[0].length; column++) {
			graphics.drawLine(0, 20 * (column + 1) + 1, this.getWidth(),
					20 * (column + 1) + 1);
		}
	}

	private boolean isGridCreated() {
		return squares != null && squares[0] != null;
	}

	private void drawImages(final Graphics graphics) {
		// Dessiner les chiffres.
		for (int row = 0; row < squares.length; row++) {
			for (int column = 0; column < squares[0].length; column++) {
				if (squares[row][column].getState() == SquareButtonState.REVEALED) {
					if (squares[row][column].isMined()) {
						if (squares[row][column].equalCoords(hitSquare)) {
							graphics.drawImage(imgMineHit, row * 20 + 2,
									column * 20 + 2, null);
						} else {
							graphics.drawImage(imgMine, row * 20 + 2,
									column * 20 + 2, null);
						}
					} else {
						if (squares[row][column].wasFlagged()) {
							graphics.drawImage(imgMineWrong, row * 20 + 2,
									column * 20 + 2, null);
						}
					}

					drawNumberImage(graphics, row, column);
				} else if (squares[row][column].getState() == SquareButtonState.CHEATED) {
					graphics.drawImage(imgMineCheated, row * 20 + 2,
							column * 20 + 1, null);
				}
			}
		}
	}

	private void drawNumberImage(final Graphics graphics, final int row,
			final int column) {
		switch (squares[row][column].getNeighboorMineCount()) {
		case 1:
			graphics.drawImage(img1, row * 20 + 1, column * 20 + 1, null);
			break;
		case 2:
			graphics.drawImage(img2, row * 20 + 1, column * 20 + 1, null);
			break;
		case 3:
			graphics.drawImage(img3, row * 20 + 1, column * 20 + 1, null);
			break;
		case 4:
			graphics.drawImage(img4, row * 20 + 1, column * 20 + 1, null);
			break;
		case 5:
			graphics.drawImage(img5, row * 20 + 1, column * 20 + 1, null);
			break;
		case 6:
			graphics.drawImage(img6, row * 20 + 1, column * 20 + 1, null);
			break;
		case 7:
			graphics.drawImage(img7, row * 20 + 1, column * 20 + 1, null);
			break;
		case 8:
			graphics.drawImage(img8, row * 20 + 1, column * 20 + 1, null);
			break;
		}
	}

	public void addGameListener(final GameListener listener) {
		listeners.add(listener);
	}

	@Override
	public void paintComponent(final Graphics graphics) {
		// Dessiner la grille en dessous des composants.
		super.paintComponent(graphics);

		if (!isGridCreated()) {
			return;
		}

		drawGrid(graphics);
		drawImages(graphics);
	}

	public void startGame(final GridSize size, final int mines) {
		// Stocke le nombre de mines de la grille.
		// (utilis� pour la g�n�ration par la suite).
		this.mines = mines;
		// Si les dimensions ne changent pas,
		// r�utilise la grille au lieu de la recr�er.
		// Petite optimisation ...
		if (isGridCreated() && size.equals(this.size)) {
			resetSquareButtons();
		} else {
			createSquares(size);
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

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
	public void squareMarked(final GameEvent event) {
		onSquareMarked(event);
	}

	@Override
	public void squareUnmarked(final GameEvent event) {
		onSquareUnmarked(event);
	}
}
