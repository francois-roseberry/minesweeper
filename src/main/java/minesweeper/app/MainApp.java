package minesweeper.app;

import minesweeper.form.AppFrame;

/**
 * Classe principale qui d�clenche l'ex�cution du programme.
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */

public class MainApp {

	public MainApp() {
		AppFrame frame = AppFrame.getInstance();
		frame.setVisible(true);
	}

	/**
	 * Point d'entr�e du programme.
	 * 
	 * @param args
	 *            Arguments de ligne de commande.
	 */
	public static void main(final String[] args) {
		new MainApp();
	}
}
