package util;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public abstract class ImageUtil
{
	public static ImageIcon getImageIcon(Object obj, String path)
	{
		return new ImageIcon(obj.getClass().getResource("resource/" + path));
	}
	
	public static BufferedImage getImage(Object obj, String path)
	{
		try
		{
			return ImageIO.read(obj.getClass().getResource("resource/" + path));
		}
		catch (Exception ex)
		{
			System.out.println("L'image n'a pas pu être chargée : " + ex.getMessage());
			return null;
		}
	}

	/**
	 * les fichiers ressources sont stockés dans un dossier séparé
	 * ces fichiers ne font pas partie des fichiers de classes
	 * Chaque classe a la charge d'y accéder et d'interpréter les données de la ressource.
	 * Où se trouvent les ressources???
	 * 1- Chargeur de classes -> this.getClass() sait comment trouver le .class
	 *                           charge la classe qui doit accèder à la ressource
	 * 2- Chargeur de ressources -> getRessource() interprète le path relatif
	 */ 
}