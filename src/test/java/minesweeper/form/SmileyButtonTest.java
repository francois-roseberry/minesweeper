package minesweeper.form;

import static junit.framework.Assert.*;

import java.awt.Dimension;

import javax.swing.BorderFactory;

import org.junit.Test;

public class SmileyButtonTest {

	@Test
	public void atCreationPreferredSizeAndMaximumSizeShouldBe36By36AndBorderShouldBeRaisedBevel() {
		SmileyButton button = new SmileyButton();
		Dimension size = new Dimension(36, 36);

		assertEquals(button.getPreferredSize(), size);
		assertEquals(button.getMaximumSize(), size);
		assertEquals(button.getBorder(), BorderFactory.createRaisedBevelBorder());
	}
}
