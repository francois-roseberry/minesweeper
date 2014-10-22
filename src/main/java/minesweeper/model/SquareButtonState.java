package minesweeper.model;

/**
 * L'�num�ration SquareButtonState contient les �tats possibles d'une case de la grille de jeu.
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
public enum SquareButtonState {
	/**
	 * La case est marqu�e min�e � l'aide d'un drapeau.
	 */
	MARKED,
	/**
	 * La case a �t� d�couverte en cliquant dessus.
	 */
	REVEALED,
	/**
	 * La case est marqu�e "?" pour indiquer un doute.
	 */
	UNSURE,
	/**
	 * La case est cach�e, l'�tat intial.
	 */
	HIDDEN,
	/**
	 * La case est en mode triche, c'est-�-dire qu'elle r�v�le au joueur si elle contient une mine.
	 */
	CHEATED
}
