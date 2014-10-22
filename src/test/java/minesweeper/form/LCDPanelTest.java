package minesweeper.form;

import static junit.framework.Assert.*;

import org.junit.Test;

public class LCDPanelTest {

	@Test(expected = IllegalArgumentException.class)
	public void creatingWithNotEnoughDigitsShouldThrowException() {
		LCDPanel.create(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void creatingWithTooManyDigitsShouldThrowException() {
		LCDPanel.create(8);
	}

	@Test
	public void creatingWithSufficientDigitsShouldReturnAnInstanceOfLCDPanel() {
		assertTrue(LCDPanel.create(2) != null);
	}
}
