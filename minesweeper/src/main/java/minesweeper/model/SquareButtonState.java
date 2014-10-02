package minesweeper.model;

/**
 * L'énumération SquareButtonState contient les états
 * possibles d'une case de la grille de jeu.
 * 
 * @author David Maltais
 * @author François Roseberry
 *
 */
public enum SquareButtonState
{
	/**
	 * La case est marquée minée à l'aide d'un drapeau.
	 */
	MARKED,
	/**
	 * La case a été découverte en cliquant dessus.
	 */
	REVEALED,
	/**
	 * La case est marquée "?" pour indiquer un doute.
	 */
	UNSURE,
	/**
	 * La case est cachée, l'état intial.
	 */
	HIDDEN,
	/**
	 * La case est en mode triche, c'est-à-dire qu'elle
	 * révèle au joueur si elle contient une mine.
	 */
	CHEATED
}
