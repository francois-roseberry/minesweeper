package minesweeper.form;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.MouseEvent;

import minesweeper.model.Cell;
import minesweeper.model.GridSize;
import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.GameListener;

import org.junit.Before;
import org.junit.Test;

public class GameGridTest {

	private static final GridSize GRID_SIZE_1X1 = GridSize.create(1, 1);
	private static final Cell CELL_1X1 = new Cell(1, 1);
	private GameGrid grid;
	private SquareButtonProvider providerMock;
	private GameServices gameServicesMock;

	@Before
	public void setup() {
		providerMock = mock(SquareButtonProvider.class);
		gameServicesMock = mock(GameServices.class);
		when(gameServicesMock.isInGame()).thenReturn(true);
		grid = new GameGrid(gameServicesMock, providerMock);
	}

	@Test
	public void atGameStartAllButtonsShouldBeHidden() {
		SquareButton button = new SquareButton(CELL_1X1);
		when(providerMock.create(any(Cell.class))).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		assertEquals(SquareButtonState.HIDDEN, button.getState());
	}

	@Test
	public void afterRevealingSquareItsStateShouldBeRevealed() {
		SquareButton button = new SquareButton(CELL_1X1);
		when(providerMock.create(any(Cell.class))).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(button);
		when(eventMock.getButton()).thenReturn(MouseEvent.BUTTON1);
		grid.mouseReleased(eventMock);

		assertEquals(SquareButtonState.REVEALED, button.getState());
	}

	@Test
	public void afterFirstClickShouldCallGameServices() {
		SquareButton button = new SquareButton(CELL_1X1);
		when(providerMock.create(any(Cell.class))).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(button);
		when(eventMock.getButton()).thenReturn(MouseEvent.BUTTON1);
		grid.mouseReleased(eventMock);

		verify(gameServicesMock).firstClicked();
	}

	@Test
	public void afterRevealingAllSquaresInGridThenGameWonEventShouldBeFired() {
		when(gameServicesMock.isFirstClicked()).thenReturn(true);
		SquareButton button = new SquareButton(CELL_1X1);
		when(providerMock.create(any(Cell.class))).thenReturn(button);
		GameListener listener = mock(GameListener.class);
		grid.addGameListener(listener);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(button);
		when(eventMock.getButton()).thenReturn(MouseEvent.BUTTON1);
		grid.mouseReleased(eventMock);

		verify(listener).gameWon(any(GameEvent.class));
	}

	@Test
	public void afterMarkingSquareItsStateShouldBeMarked() {
		SquareButton button = new SquareButton(CELL_1X1);
		when(providerMock.create(any(Cell.class))).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(button);
		when(eventMock.getButton()).thenReturn(MouseEvent.BUTTON2);
		when(eventMock.isPopupTrigger()).thenReturn(true);
		grid.mouseReleased(eventMock);

		assertEquals(SquareButtonState.MARKED, button.getState());
	}

	@Test
	public void afterMarkingSquareTwiceItsStateShouldBeUnsure() {
		SquareButton button = new SquareButton(CELL_1X1);
		when(providerMock.create(any(Cell.class))).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(button);
		when(eventMock.getButton()).thenReturn(MouseEvent.BUTTON2);
		when(eventMock.isPopupTrigger()).thenReturn(true);
		grid.mouseReleased(eventMock);
		grid.mouseReleased(eventMock);

		assertEquals(SquareButtonState.UNSURE, button.getState());
	}

	@Test
	public void afterMarkingSquareThriceItsStateShouldBeHidden() {
		SquareButton button = new SquareButton(CELL_1X1);
		when(providerMock.create(any(Cell.class))).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(button);
		when(eventMock.getButton()).thenReturn(MouseEvent.BUTTON2);
		when(eventMock.isPopupTrigger()).thenReturn(true);
		grid.mouseReleased(eventMock);
		grid.mouseReleased(eventMock);
		grid.mouseReleased(eventMock);

		assertEquals(SquareButtonState.HIDDEN, button.getState());
	}

	@Test
	public void whenPressingMouseShouldIndicateToGameServices() {
		SquareButton button = new SquareButton(mock(Cell.class));
		when(providerMock.create(any(Cell.class))).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(mock(SquareButton.class));
		grid.mousePressed(eventMock);

		verify(gameServicesMock).indicateMousePressed();
	}

	@Test
	public void whenReleasingMouseShouldIndicateToGameServices() {
		SquareButton button = new SquareButton(mock(Cell.class));
		when(providerMock.create(any(Cell.class))).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(mock(SquareButton.class));
		grid.mouseReleased(eventMock);

		verify(gameServicesMock).indicateMouseReleased();
	}
}