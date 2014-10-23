package minesweeper.form;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import minesweeper.Loader;
import minesweeper.model.Cell;
import minesweeper.model.GridSize;
import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.GameListener;
import minesweeper.model.event.SquareButtonListener;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
	private ImmutableMap<Cell, SquareButton> squares = ImmutableMap.of();;
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
	// Nombre de cases de la grille.
	private GridSize size;
	// Nombre de mines dans la grille.
	private int mines;
	// Case contenant la mine qui a �t� cliqu�e (qui a caus�
	// la d�faite du joueur).
	private Cell hitCell;

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

	private ImmutableMap<Cell, SquareButton> createSquares(final GridSize size) {
		this.size = size;
		setLayout(new GridLayout(size.rows(), size.columns()));
		ImmutableMap.Builder<Cell, SquareButton> builder = ImmutableMap
				.builder();
		for (Cell cell : size.cells()) {
			SquareButton square = createSquareButton(cell);
			builder.put(cell, square);
			add(square);
		}

		return builder.build();
	}

	private SquareButton createSquareButton(final Cell cell) {
		SquareButton square = provider.create(cell);
		square.addMouseListener(this);
		square.addSquareButtonListener(this);
		square.setBorder(BorderFactory.createRaisedBevelBorder());
		return square;
	}

	private ImmutableList<SquareButton> getSquareButtons() {
		ImmutableList.Builder<SquareButton> builder = ImmutableList.builder();
		for (Cell cell : size.cells()) {
			builder.add(squares.get(cell));
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
	private void placeMines(final Cell cellToAvoid, final int mines) {
		// Cr�er une collection de cases disponibles.
		// (les cases qui peuvent recevoir des mines)
		ImmutableList<Cell> availableCells = getAvailableCellsForMines(cellToAvoid);
		ImmutableList<Cell> placedMines = generateMines(mines, availableCells);

		for (Cell mine : placedMines) {
			squares.get(mine).setMine();
		}
	}

	// à remplacer par le MineGenerator externe
	private ImmutableList<Cell> generateMines(int mines,
			final ImmutableList<Cell> availableCells) {
		List<Cell> openCells = Lists.newArrayList(availableCells);

		ImmutableList.Builder<Cell> builder = ImmutableList.builder();
		Random random = new Random();
		while (mines > 0) {
			// G�n�rer un index al�atoire.
			Cell chosenCell = pickCellAtRandom(openCells, random);
			builder.add(chosenCell);
			// Enlever la case de la liste.
			openCells.remove(chosenCell);
			mines--;
		}
		return builder.build();
	}

	private Cell pickCellAtRandom(final List<Cell> openCells,
			final Random random) {
		int openIndex = random.nextInt(openCells.size());
		Cell chosenCell = openCells.get(openIndex);
		return chosenCell;
	}

	private ImmutableList<Cell> getAvailableCellsForMines(final Cell cellToAvoid) {
		ImmutableList.Builder<Cell> builder = ImmutableList.builder();

		// Remplir le tableau des coordonn�es disponibles.
		for (Cell cell : size.cells()) {
			if (!cellToAvoid.equals(cell)) {
				builder.add(cell);
			}
		}
		return builder.build();
	}

	/*
	 * M�thode permettant de r�v�ler une case, compter les mines alentour et au
	 * besoin, s'appeler r�cursivement pour r�v�ler les cases autour.
	 */
	private void revealNeighbors(final Cell cell) {
		SquareButton square = squares.get(cell);
		if (square.getState() == SquareButtonState.HIDDEN) {
			square.reveal();
			int mines = getNeighboorMineCount(cell);
			square.setNeighboorMineCount(mines);
			if (square.getNeighboorMineCount() == 0) {
				// La case n'a pas de mines avoisinantes.
				// Révéler les voisins, récursivement.
				for (Cell neighboor : cell.neighboors()) {
					if (squares.containsKey(neighboor)) {
						revealNeighbors(neighboor);
					}
				}
			}
		}
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
	private int getNeighboorMineCount(final Cell cell) {
		int mines = 0;
		for (Cell neighboor : cell.neighboors()) {
			SquareButton neighboorSquare = squares.get(neighboor);
			if (neighboorSquare != null && neighboorSquare.isMined()) {
				mines++;
			}
		}

		return mines;
	}

	/*
	 * G�re le clic gauche sur une case.
	 */
	private void squareButton_leftClick(final Cell cell) {
		Preconditions.checkArgument(isGridCreated());
		SquareButton square = squares.get(cell);
		// Premier clique de la partie.
		if (!gameServices.isFirstClicked()) {
			if (square.getState() == SquareButtonState.HIDDEN) {
				placeMines(cell, mines);
				revealNeighbors(cell);
				gameServices.firstClicked();
			}
		} else {
			// Clic normal (pendant la partie).
			if (square.isMined()
					&& square.getState() == SquareButtonState.HIDDEN) {
				// La partie a �t� perdue.
				hitCell = cell;
				finishLostGame();
				square.reveal();
				onGameLost(new GameEvent());
			} else {
				revealNeighbors(cell);
				// Si les cases restantes sont toutes min�es, alors
				// les marquer automatiquement.
				if (areAllHiddenSquaresMined()) {
					markHiddenSquares();
				}

				checkGameWon();
			}
		}
	}

	private void squareButton_rightClick(final Cell cell) {
		if (squares.get(cell).getState() != SquareButtonState.REVEALED
				&& squares.get(cell).getState() != SquareButtonState.CHEATED) {
			squares.get(cell).rightClick();
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
		for (int row = 1; row <= size.rows(); row++) {
			graphics.drawLine(20 * row + 1, 0, 20 * row + 1, getHeight());
		}

		// Dessiner les lignes verticales de la grille.
		for (int column = 1; column <= size.columns(); column++) {
			graphics.drawLine(0, 20 * column + 1, getWidth(), 20 * column + 1);
		}
	}

	private boolean isGridCreated() {
		return !squares.isEmpty();
	}

	private void drawImages(final Graphics graphics) {
		// Dessiner les chiffres.
		for (Cell cell : squares.keySet()) {
			int x = cell.column() - 1;
			int y = cell.row() - 1;
			SquareButton square = squares.get(cell);
			if (square.getState() == SquareButtonState.REVEALED) {
				if (square.isMined()) {
					if (cell.equals(hitCell)) {
						graphics.drawImage(imgMineHit, x * 20 + 2, y * 20 + 2,
								null);
					} else {
						graphics.drawImage(imgMine, x * 20 + 2, y * 20 + 2,
								null);
					}
				} else {
					if (square.wasFlagged()) {
						graphics.drawImage(imgMineWrong, x * 20 + 2,
								y * 20 + 2, null);
					}
				}

				drawNumberImage(graphics, cell);
			} else if (square.getState() == SquareButtonState.CHEATED) {
				graphics.drawImage(imgMineCheated, x * 20 + 2, y * 20 + 1, null);
			}
		}
	}

	private void drawNumberImage(final Graphics graphics, final Cell cell) {
		int x = cell.column() - 1;
		int y = cell.row() - 1;
		switch (squares.get(cell).getNeighboorMineCount()) {
		case 1:
			graphics.drawImage(img1, x * 20 + 1, y * 20 + 1, null);
			break;
		case 2:
			graphics.drawImage(img2, x * 20 + 1, y * 20 + 1, null);
			break;
		case 3:
			graphics.drawImage(img3, x * 20 + 1, y * 20 + 1, null);
			break;
		case 4:
			graphics.drawImage(img4, x * 20 + 1, y * 20 + 1, null);
			break;
		case 5:
			graphics.drawImage(img5, x * 20 + 1, y * 20 + 1, null);
			break;
		case 6:
			graphics.drawImage(img6, x * 20 + 1, y * 20 + 1, null);
			break;
		case 7:
			graphics.drawImage(img7, x * 20 + 1, y * 20 + 1, null);
			break;
		case 8:
			graphics.drawImage(img8, x * 20 + 1, y * 20 + 1, null);
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
			removeAll();
			squares = createSquares(size);
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
			Cell cellClicked = square.getCell();
			if (e.getButton() == MouseEvent.BUTTON1) {
				squareButton_leftClick(cellClicked);
			} else if (e.isPopupTrigger()) {
				squareButton_rightClick(cellClicked);
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
