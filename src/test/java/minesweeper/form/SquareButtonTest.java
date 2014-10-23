package minesweeper.form;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import minesweeper.model.Cell;
import minesweeper.model.SquareButtonState;
import minesweeper.model.event.GameEvent;
import minesweeper.model.event.SquareButtonListener;

import org.junit.Test;

public class SquareButtonTest {

	private static final Cell CELL = new Cell(1, 1);

	@Test
	public void atCreationStateShouldBeHiddenAndMineCountShouldBeZero() {
		SquareButton button = new SquareButton(CELL);

		assertEquals(button.getNeighboorMineCount(), 0);
		assertEquals(button.getState(), SquareButtonState.HIDDEN);
	}

	@Test
	public void settingMineCountBetween0And9ShouldSetIt() {
		SquareButton button = new SquareButton(CELL);
		for (int mines = 0; mines < 9; mines++) {
			button.setNeighboorMineCount(mines);
			assertEquals(button.getNeighboorMineCount(), mines);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void settingNegativeMineCountShouldThrowException() {
		new SquareButton(CELL).setNeighboorMineCount(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void settingMineCountShouldWithCountAboveSevenShouldThrowException() {
		new SquareButton(CELL).setNeighboorMineCount(9);
	}

	@Test
	public void revealShouldChangeStateAndVisibility() {
		SquareButton button = new SquareButton(CELL);
		button.reveal();

		assertFalse(button.isVisible());
		assertEquals(button.getState(), SquareButtonState.REVEALED);
	}

	@Test
	public void cheatShouldChangeStateIfButtonIsMined() {
		SquareButton button = new SquareButton(CELL);
		button.setMine();
		button.cheat();

		assertEquals(button.getState(), SquareButtonState.CHEATED);
	}

	@Test
	public void cheatShouldNotChangeStateIfButtonIsNotMined() {
		SquareButton button = new SquareButton(CELL);
		button.cheat();

		assertEquals(button.getState(), SquareButtonState.HIDDEN);
	}

	@Test
	public void rightClickShouldChangeStateToMarkedAndLaunchEventIfHidden() {
		SquareButton button = new SquareButton(CELL);
		SquareButtonListener listener = mock(SquareButtonListener.class);
		button.addSquareButtonListener(listener);
		button.rightClick();

		assertEquals(button.getState(), SquareButtonState.MARKED);
		verify(listener).squareMarked(any(GameEvent.class));
	}

	@Test
	public void rightClickShouldChangeStateToUnsureAndLauchEventIfMarked() {
		SquareButton button = new SquareButton(CELL);
		SquareButtonListener listener = mock(SquareButtonListener.class);
		button.addSquareButtonListener(listener);
		button.rightClick();
		button.rightClick();

		assertEquals(button.getState(), SquareButtonState.UNSURE);
		verify(listener).squareUnmarked(any(GameEvent.class));
	}

	@Test
	public void rightClickShouldChangeStateToHiddenIfUnsure() {
		SquareButton button = new SquareButton(CELL);
		button.rightClick();
		button.rightClick();
		button.rightClick();

		assertEquals(button.getState(), SquareButtonState.HIDDEN);
	}

	@Test
	public void resetShouldSetStateToHiddenAndRemoveMineAndRemoveFlagAndSetVisibleAndSetNeighboorMineCountToZero() {
		SquareButton button = new SquareButton(CELL);
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
