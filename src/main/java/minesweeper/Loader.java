package minesweeper;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public abstract class Loader {

	public static ImageIcon getImageIcon(final String path) {
		return new ImageIcon(Loader.class.getResource(path));
	}

	public static BufferedImage getImage(final String path) {
		try {
			return ImageIO.read(Loader.class.getResource(path));
		} catch (Exception ex) {
			throw new RuntimeException("Image could not be loaded : " + ex.getMessage());
		}
	}

	/**
	 * les fichiers ressources sont stock�s dans un dossier s�par� ces fichiers ne font pas partie des fichiers de classes Chaque classe a
	 * la charge d'y acc�der et d'interpr�ter les donn�es de la ressource. O� se trouvent les ressources??? 1- Chargeur de classes ->
	 * this.getClass() sait comment trouver le .class charge la classe qui doit acc�der � la ressource 2- Chargeur de ressources ->
	 * getRessource() interpr�te le path relatif
	 */
}