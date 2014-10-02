package minesweeper;

import minesweeper.form.AppFrame;

/**
 * Classe principale qui déclenche l'exécution du
 * programme.
 * 
 * @author David Maltais
 * @author François Roseberry
 * 
 */

public class MainApp
{
	public MainApp()
	{
		AppFrame frame = AppFrame.getInstance();
		frame.setVisible(true);
	}
	
	/**
	 * Point d'entrée du programme.
	 * 
	 * @param args	Arguments de ligne de commande.
	 */
	public static void main(String[] args)
	{
		new MainApp();
	}
}
