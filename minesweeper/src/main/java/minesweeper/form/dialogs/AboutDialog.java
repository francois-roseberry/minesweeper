package minesweeper.form.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class AboutDialog extends JDialog implements ActionListener
{
	private static final String TITLE = "À propos de Démineur";
	
	private static final String NAME = "Démineur";
	private static final String DESCRIPTION = "Jeu de démineur";
	private static final String AUTHOR = "Développé par David Maltais et François Roseberry";
	private static final String VERSION = "Version 1.0";
	
	private static final String BTN_CLOSE_ACTION_COMMAND = "Fermer";
	
	public AboutDialog()
	{
		super();
		this.setTitle(AboutDialog.TITLE);
		this.setResizable(false);
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.initializeComponent();
		this.pack();
		this.centerWindow();
	}
	
	private void centerWindow()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
	}
	
	/*
	 * Crée et dispose les composants.
	 * 
	 */
	private void initializeComponent()
	{
		Box hbMain = Box.createHorizontalBox();
		Box vbMain = Box.createVerticalBox();
		JPanel pan = new JPanel(new BorderLayout());
		Box hbButton = Box.createHorizontalBox();
		
		hbMain.add(Box.createHorizontalStrut(10));
		hbMain.add(vbMain);
		hbMain.add(Box.createHorizontalStrut(10));
		
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(new JLabel(AboutDialog.NAME));
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(new JLabel(AboutDialog.DESCRIPTION));
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(new JLabel(AboutDialog.VERSION));
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(new JLabel(AboutDialog.AUTHOR));
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(pan);
		vbMain.add(Box.createVerticalStrut(10));
		
		pan.add(hbButton);
		
		JButton btnOK = new JButton(AboutDialog.BTN_CLOSE_ACTION_COMMAND);
		btnOK.addActionListener(this);
		
		hbButton.add(Box.createHorizontalGlue());
		hbButton.add(btnOK);
		hbButton.add(Box.createHorizontalGlue());
		
		this.add(hbMain);
	}
	
	public static void showDialog()
	{
		new AboutDialog().setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		// Un seul bouton donc pas besoin de tester l'ActionCommand.
		this.dispose();
	}
}
