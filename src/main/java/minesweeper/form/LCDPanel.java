package minesweeper.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * La classe LCDPanel repr�sente un panneau d'affichage � cristaux liquides (simul� bien-entendu).
 * 
 * @author Fran�ois Roseberry
 * 
 */
@SuppressWarnings("serial")
public class LCDPanel extends JPanel {

	private static final int DIGIT_WIDTH = 20;
	private static final int DIGIT_HEIGHT = 16;
	private static final int HEIGHT = 37;
	private static final int MINIMUM_DIGITS = 1;
	private static final int MAXIMUM_DIGITS = 7;
	private static final Color DEFAULT_LCD_COLOR = Color.RED;

	private static final int FIRST_CRISTAL_FIRST_X = 2;
	private static final int FIRST_CRISTAL_SECOND_X = 6;
	private static final int SECOND_CRISTAL_FIRST_X = 14;
	private static final int SECOND_CRISTAL_SECOND_X = 18;
	private static final int VERTICAL_CRISTAL_FIRST_Y = 2;
	private static final int VERTICAL_CRISTAL_SECOND_Y = 6;
	private static final int VERTICAL_CRISTAL_THIRD_Y = 14;
	private static final int VERTICAL_CRISTAL_FOURTH_Y = 18;

	private static final int MIDDLE_CRISTAL_FIRST_Y = 15;
	private static final int MIDDLE_CRISTAL_SECOND_Y = 18;
	private static final int MIDDLE_CRISTAL_THIRD_Y = 22;

	private static final int LOWER_CRISTAL_FIRST_Y = 31;
	private static final int LOWER_CRISTAL_SECOND_Y = 35;

	private static final int[] NUMS_CRISTAL_DIGIT_0 = { 0, 1, 2, 4, 5, 6 };
	private static final int[] NUMS_CRISTAL_DIGIT_1 = { 2, 5 };
	private static final int[] NUMS_CRISTAL_DIGIT_2 = { 0, 2, 3, 4, 6 };
	private static final int[] NUMS_CRISTAL_DIGIT_3 = { 0, 2, 3, 5, 6 };
	private static final int[] NUMS_CRISTAL_DIGIT_4 = { 1, 2, 3, 5 };
	private static final int[] NUMS_CRISTAL_DIGIT_5 = { 0, 1, 3, 5, 6 };
	private static final int[] NUMS_CRISTAL_DIGIT_6 = { 1, 3, 4, 5, 6 };
	private static final int[] NUMS_CRISTAL_DIGIT_7 = { 0, 2, 5 };
	private static final int[] NUMS_CRISTAL_DIGIT_8 = { 0, 1, 2, 3, 4, 5, 6 };
	private static final int[] NUMS_CRISTAL_DIGIT_9 = { 0, 1, 2, 3, 5 };
	private static final int[] NUMS_CRISTAL_NO_DIGIT = { 3, 4, 5, 6 };

	private static final int NO_DIGIT_VALUE = 10;

	private static final int[][] NUMS_CRISTAL_DIGITS = {
			NUMS_CRISTAL_DIGIT_0, NUMS_CRISTAL_DIGIT_1, NUMS_CRISTAL_DIGIT_2,
			NUMS_CRISTAL_DIGIT_3, NUMS_CRISTAL_DIGIT_4, NUMS_CRISTAL_DIGIT_5,
			NUMS_CRISTAL_DIGIT_6, NUMS_CRISTAL_DIGIT_7, NUMS_CRISTAL_DIGIT_8,
			NUMS_CRISTAL_DIGIT_9, NUMS_CRISTAL_NO_DIGIT };

	// Couleur d'affichage des cristaux.
	private final Color displayColor;
	// Indique si les z�ros � gauche (non-significatifs)
	// doivent �tre affich�s quand m�me.
	private final boolean paddingZeros;
	// Texte � afficher (toujours num�rique).
	private String display = "";
	// Nombre de caract�res num�riques affich�s.
	private final int digits;
	// Image utilis�e pour le double buffering.
	private BufferedImage buffer;
	// Objet Graphics de cette image.
	private Graphics2D gBuf;

	/*
	 * Num�ros des cristaux :
	 * 
	 *   0         ____
	 * 1   2      |    |
	 *   3        |____|
	 * 4   5      |    |
	 *   6        |____|
	 */

	/*
	 * Constructeur priv� (�vite l'instanciation directe).
	 * Cr�e un panneau d'affichage � cristaux liquides.
	 * 
	 * @param digits		Le nombres de chiffres qui sera affich�.
	 * @param displayColor	La couleur d'affichage des cristaux liquides.
	 * @param paddingZeros	Indique si les z�ros � gauche (non-significatifs)
	 * 						doivent �tre dessin�s.
	 * 
	 */
	private LCDPanel(final int digits, final Color displayColor, final boolean paddingZeros) {
		super();

		this.digits = digits;
		this.displayColor = displayColor;
		this.paddingZeros = paddingZeros;

		for (int i = 0; i < digits; i++) {
			display = display + "0";
		}

		setPreferredSize(new Dimension(LCDPanel.DIGIT_WIDTH * digits, LCDPanel.HEIGHT));
		setMaximumSize(getPreferredSize());
		buffer = new BufferedImage((int) this.getPreferredSize().getWidth(), (int) this.getPreferredSize().getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		gBuf = buffer.createGraphics();
	}

	/**
	 * Cr�e un panneau d'affichage � cristaux liquides.
	 * 
	 * @param digits
	 *            Le nombre de caract�res qui sera affich�. Doit �tre > 0.
	 * 
	 * @return Un panneau d'affichages � cristaux liquides.
	 */
	public static LCDPanel createLCDPanel(final int digits) {
		LCDPanel lcd = null;
		if (digits >= LCDPanel.MINIMUM_DIGITS &&
				digits <= LCDPanel.MAXIMUM_DIGITS) {
			lcd = new LCDPanel(digits, LCDPanel.DEFAULT_LCD_COLOR, true);
		}
		return lcd;
	}

	/**
	 * Cr�e un panneau d'affichage � cristaux liquides.
	 * 
	 * @param digits
	 *            Le nombre de caract�res qui sera affich�. Doit �tre > 0.
	 * 
	 * @param displayColor
	 *            La couleur d'affichage. Doit �tre diff�rent de 0.
	 * 
	 * @return
	 */
	public static LCDPanel createLCDPanel(final int digits, final Color displayColor) {
		LCDPanel lcd = null;
		if (digits >= LCDPanel.MINIMUM_DIGITS &&
				digits <= LCDPanel.MAXIMUM_DIGITS &&
				!displayColor.equals(Color.BLACK)) {
			lcd = new LCDPanel(digits, displayColor, true);
		}
		return lcd;
	}

	public static LCDPanel createLCDPanel(final int digits, final Color displayColor, final boolean paddingZeros) {
		LCDPanel lcd = null;
		if (digits >= LCDPanel.MINIMUM_DIGITS &&
				digits <= LCDPanel.MAXIMUM_DIGITS &&
				!displayColor.equals(Color.BLACK)) {
			lcd = new LCDPanel(digits, displayColor, paddingZeros);
		}
		return lcd;
	}

	public void displayNumber(final int number) {
		display = "" + number;
		while (display.length() < digits) {
			display = "0" + display;
		}
		repaint();
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);

		gBuf.setColor(Color.BLACK);
		gBuf.fillRect(0, 0, this.getWidth(), this.getHeight());
		gBuf.setColor(this.displayColor);
		drawDigits(this.gBuf);

		g.drawImage(this.buffer, 0, 0, null);
	}

	private void drawDigits(final Graphics g) {
		for (int i = 0; i < this.digits; i++) {
			drawDigit(i, g);
		}
	}

	private void drawDigit(final int numDigit, final Graphics g) {
		char ch = display.charAt(numDigit);
		int value = LCDPanel.NO_DIGIT_VALUE;
		try {
			value = Integer.valueOf("" + ch);
		} catch (NumberFormatException ex) {}
		if (value == 0) {
			if ((paddingZeros) ||
					(!paddingZeros && !isLeftZero(numDigit))) {
				for (int n : LCDPanel.NUMS_CRISTAL_DIGIT_0) {
					drawCristal(n, numDigit, g);
				}
			}
		} else {
			for (int n : LCDPanel.NUMS_CRISTAL_DIGITS[value]) {
				drawCristal(n, numDigit, g);
			}
		}
	}

	private void drawCristal(final int numCristal, final int numDigit, final Graphics g) {
		switch (numCristal) {
			case 0:
				this.drawUpperCristal(numDigit, g);
				break;
			case 1:
				this.drawLeftCristal(0, numDigit, g);
				break;
			case 2:
				this.drawRightCristal(0, numDigit, g);
				break;
			case 3:
				this.drawMiddleCristal(numDigit, g);
				break;
			case 4:
				this.drawLeftCristal(1, numDigit, g);
				break;
			case 5:
				this.drawRightCristal(1, numDigit, g);
				break;
			case 6:
				this.drawLowerCristal(numDigit, g);
				break;
		}
	}

	/*
	 * Un cristal de gauche a cette forme :
	 * (avec dimensions)
	 * 
	 * |\                |
	 * | \               | 3 pixels
	 * |  \              |
	 * |   |         |
	 * |   |         |
	 * |   |         | 8 pixels
	 * |   |         |
	 * |   |         |
	 * |   |         |
	 * |  /              |
	 * | /               | 3 pixels
	 * |/                |
	 * 
	 * 
	 * <-->
	 *  4 pixels
	 */
	private void drawLeftCristal(final int numCristal, final int numDigit, final Graphics g) {
		// Valeurs possibles pour numCristal : 0 et 1
		Polygon poly = new Polygon();
		poly.addPoint(LCDPanel.FIRST_CRISTAL_FIRST_X + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.VERTICAL_CRISTAL_FIRST_Y
				+ LCDPanel.DIGIT_HEIGHT * numCristal);
		poly.addPoint(LCDPanel.FIRST_CRISTAL_SECOND_X + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.VERTICAL_CRISTAL_SECOND_Y
				+ LCDPanel.DIGIT_HEIGHT * numCristal);
		poly.addPoint(LCDPanel.FIRST_CRISTAL_SECOND_X + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.VERTICAL_CRISTAL_THIRD_Y
				+ LCDPanel.DIGIT_HEIGHT * numCristal);
		poly.addPoint(LCDPanel.FIRST_CRISTAL_FIRST_X + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.VERTICAL_CRISTAL_FOURTH_Y
				+ LCDPanel.DIGIT_HEIGHT * numCristal);
		g.fillPolygon(poly);
	}

	/*
	 * ______________________________
	 * \                            /                |
	 *  \                          /                 | 4 pixels
	 *   \________________________/                  |
	 * 
	 * 
	 * <->
	 *  3 pixels
	 *  
	 *   <----------------------->
	 *           8 pixels
	 * 
	 */
	private void drawUpperCristal(final int numDigit, final Graphics g) {
		Polygon poly = new Polygon();
		poly.addPoint(3 + LCDPanel.DIGIT_WIDTH * numDigit, 2);
		poly.addPoint(6 + LCDPanel.DIGIT_WIDTH * numDigit, 6);
		poly.addPoint(13 + LCDPanel.DIGIT_WIDTH * numDigit, 6);
		poly.addPoint(16 + LCDPanel.DIGIT_WIDTH * numDigit, 2);
		g.fillPolygon(poly);
	}

	/*
	 * 
	 *   _____________________
	 *  /                     \        | 3 pixels
	 * /                       \       |
	 * \                       /               | 4 pixels
	 *  \_____________________/                |
	 *  
	 *  
	 * <->
	 *  4 pixels
	 *  
	 *   <------------------->
	 *           6 pixels
	 */
	private void drawMiddleCristal(final int numDigit, final Graphics g) {
		Polygon poly = new Polygon();
		poly.addPoint(3 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.MIDDLE_CRISTAL_SECOND_Y);
		poly.addPoint(7 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.MIDDLE_CRISTAL_FIRST_Y);
		poly.addPoint(13 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.MIDDLE_CRISTAL_FIRST_Y);
		poly.addPoint(17 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.MIDDLE_CRISTAL_SECOND_Y);
		poly.addPoint(13 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.MIDDLE_CRISTAL_THIRD_Y);
		poly.addPoint(7 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.MIDDLE_CRISTAL_THIRD_Y);
		g.fillPolygon(poly);
	}

	/*
	 *    ________________________
	 *   /                        \               |
	 *  /                          \              | 4 pixels
	 * /____________________________\             |
	 *   
	 * <->
	 *  4 pixels
	 *  
	 *   <----------------------->
	 *           8 pixels
	 * 
	 */
	private void drawLowerCristal(final int numDigit, final Graphics g) {
		Polygon poly = new Polygon();
		poly.addPoint(3 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.LOWER_CRISTAL_SECOND_Y);
		poly.addPoint(6 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.LOWER_CRISTAL_FIRST_Y);
		poly.addPoint(14 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.LOWER_CRISTAL_FIRST_Y);
		poly.addPoint(17 + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.LOWER_CRISTAL_SECOND_Y);
		g.fillPolygon(poly);
	}

	/*
	 * 
	 *    /|            |
	 *   / |            | 4 pixels
	 *  /  |            |
	 * |   |        |
	 * |   |        |
	 * |   |        | 8 pixels
	 * |   |        |
	 * |   |        |
	 * |   |        |
	 *  \  |            |
	 *   \ |            | 4 pixels
	 *    \|            |
	 *    
	 * 
	 * <-->
	 *  4 pixels
	 * 
	 */
	private void drawRightCristal(final int numCristal, final int numDigit, final Graphics g) {
		// Valeurs possibles pour numCristal : 0 et 1
		Polygon poly = new Polygon();
		poly.addPoint(LCDPanel.SECOND_CRISTAL_SECOND_X + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.VERTICAL_CRISTAL_FIRST_Y
				+ LCDPanel.DIGIT_HEIGHT * numCristal);
		poly.addPoint(LCDPanel.SECOND_CRISTAL_FIRST_X + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.VERTICAL_CRISTAL_SECOND_Y
				+ LCDPanel.DIGIT_HEIGHT * numCristal);
		poly.addPoint(LCDPanel.SECOND_CRISTAL_FIRST_X + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.VERTICAL_CRISTAL_THIRD_Y
				+ LCDPanel.DIGIT_HEIGHT * numCristal);
		poly.addPoint(LCDPanel.SECOND_CRISTAL_SECOND_X + LCDPanel.DIGIT_WIDTH * numDigit, LCDPanel.VERTICAL_CRISTAL_FOURTH_Y
				+ LCDPanel.DIGIT_HEIGHT * numCristal);
		g.fillPolygon(poly);
	}

	private boolean isLeftZero(int numDigit) {
		char ch;
		boolean isLeftZero = true;
		while (numDigit > 0 && isLeftZero) {
			numDigit--;
			// On obtient le caract�re situ� � gauche.
			ch = display.charAt(numDigit);
			// Si celui-ci est un z�ro, faut v�rifier les autres.
			// Sinon, arr�ter de boucler, ce n'est pas un z�ro �
			// gauche.
			if (ch != '0') {
				isLeftZero = false;
			}
		}
		return isLeftZero;
	}
}
