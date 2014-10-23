package minesweeper.form;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.MouseEvent;

import minesweeper.model.GridSize;
import minesweeper.model.SquareButtonState;

import org.junit.Before;
import org.junit.Test;

public class GameGridTest {

	private static final GridSize GRID_SIZE_1X1 = GridSize.create(1, 1);
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
		SquareButton button = new SquareButton(0, 0);
		when(providerMock.create(anyInt(), anyInt())).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		assertEquals(SquareButtonState.HIDDEN, button.getState());
	}

	@Test
	public void afterRevealingSquareItsStateShouldBeRevealed() {
		SquareButton button = new SquareButton(0, 0);
		when(providerMock.create(anyInt(), anyInt())).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(button);
		when(eventMock.getButton()).thenReturn(MouseEvent.BUTTON1);
		grid.mouseReleased(eventMock);

		assertEquals(SquareButtonState.REVEALED, button.getState());
	}

	@Test
	public void afterFirstClickShouldCallGameServices() {
		SquareButton button = new SquareButton(0, 0);
		when(providerMock.create(anyInt(), anyInt())).thenReturn(button);
		grid.startGame(GRID_SIZE_1X1, 0);

		MouseEvent eventMock = mock(MouseEvent.class);
		when(eventMock.getSource()).thenReturn(button);
		when(eventMock.getButton()).thenReturn(MouseEvent.BUTTON1);
		grid.mouseReleased(eventMock);

		verify(gameServicesMock).firstClicked();
	}
}