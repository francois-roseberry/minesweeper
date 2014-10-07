package minesweeper.form.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;

import minesweeper.model.event.ValidationEvent;
import minesweeper.model.event.ValidationListener;

import com.google.common.collect.Lists;

@SuppressWarnings("serial")
public class CheatDialog extends JDialog implements ActionListener, KeyListener {

	// Titre de la bo�te de dialogue.
	private static final String TITLE = "Triche";
	// Texte inscrit dans la bo�te de dialogue.
	private static final String DIRECTIVE = "Entrez le code de triche ci-dessous :";
	private static final String OK_ACTION_COMMAND = "OK";
	private static final String CANCEL_ACTION_COMMAND = "Annuler";
	// Message qui indique que le mot de passe entr� est le bon.
	private static final String RIGHT_PASSWORD_MESSAGE = "Vous avez entr� le bon mot de passe !";
	// Message qui indique que le mot de passe entr� est incorrect.
	private static final String WRONG_PASSWORD_MESSAGE = "Code invalide. R�essayez.";

	/**
	 * Nombre maximum de caract�res pouvant �tre contenus dans un mot de passe.
	 */
	public static final int MAX_CHARS_PASSWORD = 15;

	// Champ de texte de type Mot de passe.
	private final JPasswordField txtPass = new JPasswordField(CheatDialog.MAX_CHARS_PASSWORD);
	// Mot de passe � v�rifier.
	private char[] correctPassword;
	// Liste des �couteurs d'�v�nement Validation.
	private final List<ValidationListener> listeners = Lists.newArrayList();

	/*
	 * Constructeur.
	 * 
	 */
	private CheatDialog(final ValidationListener listener, final char[] password) {
		super();

		setTitle(CheatDialog.TITLE);
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addValidationListener(listener);
		initializeComponent();
		pack();
		correctPassword = password;
		centerWindow();
	}

	/*
	 * Centre la bo�te de dialogue.
	 * 
	 */
	private void centerWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
	}

	/*
	 * Cr�e et dispose les composants.
	 * 
	 */
	private void initializeComponent() {
		txtPass.addKeyListener(this);
		Box hbMain = Box.createHorizontalBox();
		Box vbMain = Box.createVerticalBox();
		JPanel panButtons = new JPanel(new BorderLayout());
		JPanel panText = new JPanel(new BorderLayout());
		JButton btnOK = new JButton(CheatDialog.OK_ACTION_COMMAND);
		btnOK.setActionCommand(CheatDialog.OK_ACTION_COMMAND);
		btnOK.addActionListener(this);
		JButton btnCancel = new JButton(CheatDialog.CANCEL_ACTION_COMMAND);
		btnCancel.setActionCommand(CheatDialog.CANCEL_ACTION_COMMAND);
		btnCancel.addActionListener(this);
		Box hbButton = Box.createHorizontalBox();
		Box hbText = Box.createHorizontalBox();

		hbMain.add(Box.createHorizontalStrut(10));
		hbMain.add(vbMain);
		hbMain.add(Box.createHorizontalStrut(10));

		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(panText);
		vbMain.add(Box.createVerticalStrut(5));
		vbMain.add(this.txtPass);
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(panButtons);
		vbMain.add(Box.createVerticalStrut(10));

		panText.add(hbText);
		panButtons.add(hbButton);

		hbText.add(Box.createHorizontalGlue());
		hbText.add(new JLabel(CheatDialog.DIRECTIVE));
		hbText.add(Box.createHorizontalGlue());

		hbButton.add(Box.createHorizontalGlue());
		hbButton.add(btnOK);
		hbButton.add(Box.createHorizontalGlue());
		hbButton.add(btnCancel);
		hbButton.add(Box.createHorizontalGlue());

		add(hbMain);
	}

	private void onValidated(final ValidationEvent e) {
		for (ValidationListener listener : this.listeners) {
			listener.validated(e);
		}
	}

	/**
	 * Enregistre un �couteur d'�v�nement Validation.
	 * 
	 * @param listener
	 *            L'�couteur d'�v�nement. Ne doit pas �tre null.
	 */
	public void addValidationListener(final ValidationListener listener) {
		listeners.add(listener);
	}

	/**
	 * D�senregistre un �couteur d'�v�nement Validation.
	 * 
	 * @param listener
	 *            L'�couteur d'�v�nement. Ne doit pas �tre null.
	 */
	public void removeValidationListener(final ValidationListener listener) {
		if (listener != null) {
			for (ValidationListener l : this.listeners) {
				if (l.equals(listener)) {
					listeners.remove(l);
				}
			}
		}
	}

	/**
	 * Affiche la bo�te de dialogue de saisie du code de triche, et enregistre le listener.
	 * 
	 * @param password
	 *            Le mot de passe, sous forme de tableau de caract�res. Doit contenir entre 0 et MAX_CHARS_PASSWORD caract�res.
	 * @param listener
	 *            L'�couteur d'�v�nement Validation. Ne doit pas �tre null.
	 */
	public static void showDialog(final char[] password, final ValidationListener listener) {
		if (password.length <= CheatDialog.MAX_CHARS_PASSWORD) {
			CheatDialog dialog = new CheatDialog(listener, password);
			dialog.setVisible(true);
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals(CheatDialog.OK_ACTION_COMMAND)) {
			ok_Clicked();
		} else if (e.getActionCommand().equals(CheatDialog.CANCEL_ACTION_COMMAND)) {
			cancel_Clicked();
		}
	}

	private void ok_Clicked() {
		if (Arrays.equals(this.txtPass.getPassword(), this.correctPassword)) {
			txtPass.setText("");
			correctPassword = null;
			JOptionPane.showMessageDialog(null, CheatDialog.RIGHT_PASSWORD_MESSAGE);
			onValidated(new ValidationEvent(getClass()));
			dispose();
		} else {
			JOptionPane.showMessageDialog(null, CheatDialog.WRONG_PASSWORD_MESSAGE);
		}
	}

	private void cancel_Clicked() {
		dispose();
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			ok_Clicked();
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {}

	@Override
	public void keyTyped(final KeyEvent e) {}
}
