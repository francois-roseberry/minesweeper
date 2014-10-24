package minesweeper.form;

import static junit.framework.Assert.assertEquals;

import java.awt.Dimension;

import javax.swing.BorderFactory;

import org.junit.Test;

public class SmileyButtonTest {

	@Test
	public void atCreationPreferredSizeAndMaximumSizeShouldBe36By36AndBorderShouldBeRaisedBevel() {
		SmileyButton button = new SmileyButton();
		Dimension size = new Dimension(36, 36);

		assertEquals(size, button.getPreferredSize());
		assertEquals(size, button.getMaximumSize());
		assertEquals(BorderFactory.createRaisedBevelBorder(),
				button.getBorder());
	}
}
