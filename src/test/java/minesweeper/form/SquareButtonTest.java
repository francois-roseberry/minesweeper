package minesweeper.form;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.SquareButtonListener;

import org.junit.Test;

public class SquareButtonTest {

	@Test
	public void atCreationStateShouldBeHiddenAndMineCountShouldBeZero() {
		SquareButton button = new SquareButton(0, 0);

		assertEquals(button.getNeighboorMineCount(), 0);
		assertEquals(button.getState(), SquareButtonState.HIDDEN);
	}

	@Test
	public void settingMineCountBetween0And9ShouldSetIt() {
		SquareButton button = new SquareButton(0, 0);
		for (int mines = 0; mines < 9; mines++) {
			button.setNeighboorMineCount(mines);
			assertEquals(button.getNeighboorMineCount(), mines);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void settingNegativeMineCountShouldThrowException() {
		new SquareButton(0, 0).setNeighboorMineCount(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void settingMineCountShouldWithCountAboveSevenShouldThrowException() {
		new SquareButton(0, 0).setNeighboorMineCount(9);
	}

	@Test
	public void revealShouldChangeStateAndVisibility() {
		SquareButton button = new SquareButton(0, 0);
		button.reveal();

		assertFalse(button.isVisible());
		assertEquals(button.getState(), SquareButtonState.REVEALED);
	}

	@Test
	public void cheatShouldChangeStateIfButtonIsMined() {
		SquareButton button = new SquareButton(0, 0);
		button.setMine();
		button.cheat();

		assertEquals(button.getState(), SquareButtonState.CHEATED);
	}

	@Test
	public void cheatShoulNotChangeStateIfButtonIsNotMined() {
		SquareButton button = new SquareButton(0, 0);
		button.cheat();

		assertEquals(button.getState(), SquareButtonState.HIDDEN);
	}

	@Test
	public void rightClickShouldChangeStateToMarkedAndLaunchEventIfHidden() {
		SquareButton button = new SquareButton(0, 0);
		SquareButtonListener listener = mock(SquareButtonListener.class);
		button.addSquareButtonListener(listener);
		button.rightClick();

		assertEquals(button.getState(), SquareButtonState.MARKED);
		verify(listener).squareMarked(any(GameEvent.class));
	}

	@Test
	public void rightClickShouldChangeStateToUnsureAndLauchEventIfMarked() {
		SquareButton button = new SquareButton(0, 0);
		SquareButtonListener listener = mock(SquareButtonListener.class);
		button.addSquareButtonListener(listener);
		button.rightClick();
		button.rightClick();

		assertEquals(button.getState(), SquareButtonState.UNSURE);
		verify(listener).squareUnmarked(any(GameEvent.class));
	}

	@Test
	public void rightClickShouldChangeStateToHiddenIfUnsure() {
		SquareButton button = new SquareButton(0, 0);
		button.rightClick();
		button.rightClick();
		button.rightClick();

		assertEquals(button.getState(), SquareButtonState.HIDDEN);
	}

	@Test
	public void equalCoordsShouldReturnFalseIfButtonIsNull() {
		SquareButton button = new SquareButton(0, 0);

		assertFalse(button.equalCoords(null));
	}

	@Test
	public void equalCoordsShouldReturnTrueIfButtonHasSameCoords() {
		SquareButton button = new SquareButton(0, 0);
		SquareButton button2 = new SquareButton(0, 0);

		assertTrue(button.equalCoords(button2));
	}

	@Test
	public void equalCoordsShouldReturnFalseIfButtonHasDifferentCoords() {
		SquareButton button = new SquareButton(0, 0);
		SquareButton button2 = new SquareButton(0, 1);

		assertFalse(button.equalCoords(button2));
	}

	@Test
	public void equalCoordsShouldReturnTrueIfCoordsAreTheSame() {
		SquareButton button = new SquareButton(0, 0);

		assertTrue(button.equalCoords(0, 0));
	}

	@Test
	public void equalCoordsShouldReturnFalseIfCoordsAreDifferent() {
		SquareButton button = new SquareButton(0, 0);

		assertFalse(button.equalCoords(0, 1));
	}

	@Test
	public void resetShouldSetStateToHiddenAndRemoveMineAndRemoveFlagAndSetVisibleAndSetNeighboorMineCountToZero() {
		SquareButton button = new SquareButton(0, 0);
		button.reveal();
		button.setMine();
		button.setWasFlagged();
		button.setNeighboorMineCount(4);
		button.reset();

		assertEquals(button.getState(), SquareButtonState.HIDDEN);
		assertFalse(button.isMined());
		assertFalse(button.wasFlagged());
		assertTrue(button.isVisible());
		assertEquals(button.getNeighboorMineCount(), 0);
	}
}
