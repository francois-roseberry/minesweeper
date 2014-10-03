package minesweeper.form.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import minesweeper.model.event.ValidationEvent;
import minesweeper.model.event.ValidationListener;

@SuppressWarnings("serial")
public class HighScoreDialog extends JDialog implements ActionListener {

	private static final String TITLE = "Vous avez fait un meilleur temps";
	private static final String BTN_OK_ACTION_COMMAND = "OK";
	private static final String BTN_CANCEL_ACTION_COMMAND = "Annuler";

	// Liste des �couteurs d'�v�nement Validation.
	private List<ValidationListener> listeners = new ArrayList<ValidationListener>();

	private JTextField txtName = new JTextField(15);

	/**
	 * Constructeur.
	 * 
	 * @param listener
	 *            �couteur d'�v�nements Validation.
	 */
	public HighScoreDialog(final ValidationListener listener) {
		super();
		this.setTitle(HighScoreDialog.TITLE);
		this.setResizable(false);
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addValidationListener(listener);
		this.initializeComponent();
		this.pack();
		this.centerWindow();
	}

	/*
	 * Centre la bo�te de dialogue.
	 * 
	 */
	private void centerWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
	}

	/*
	 * Cr�e et dispose les composants.
	 * 
	 */
	private void initializeComponent() {
		// C'est ici que tu dois coder le Layout.
		//
		// Attention t'as un Label, un TextBox et
		// deux boutons (OK et Cancel) � placer.
		Box vbMain = Box.createVerticalBox();
		Box hbLabel = Box.createHorizontalBox();
		Box hbText = Box.createHorizontalBox();
		Box hbButtons = Box.createHorizontalBox();

		JButton btnOK = new JButton(HighScoreDialog.BTN_OK_ACTION_COMMAND);
		btnOK.addActionListener(this);
		btnOK.setActionCommand(HighScoreDialog.BTN_OK_ACTION_COMMAND);

		JButton btnCancel = new JButton(HighScoreDialog.BTN_CANCEL_ACTION_COMMAND);
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand(HighScoreDialog.BTN_CANCEL_ACTION_COMMAND);

		this.setLayout(new BorderLayout());

		hbLabel.add(Box.createHorizontalGlue());
		hbLabel.add(new JLabel("Entrez votre nom :"));
		hbLabel.add(Box.createHorizontalGlue());

		hbText.add(Box.createHorizontalStrut(50));
		hbText.add(Box.createHorizontalGlue());
		hbText.add(this.txtName);
		hbText.add(Box.createHorizontalGlue());
		hbText.add(Box.createHorizontalStrut(50));

		hbButtons.add(Box.createHorizontalGlue());
		hbButtons.add(btnOK);
		hbButtons.add(Box.createHorizontalGlue());
		hbButtons.add(btnCancel);
		hbButtons.add(Box.createHorizontalGlue());

		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(hbLabel);
		vbMain.add(Box.createVerticalStrut(5));
		vbMain.add(hbText);
		vbMain.add(Box.createVerticalStrut(5));
		vbMain.add(hbButtons);
		vbMain.add(Box.createVerticalStrut(10));

		this.add(vbMain);

		//this.add(new JLabel("Entrez votre nom :"), BorderLayout.NORTH);
		//this.add(this.txtName, BorderLayout.CENTER);
		//this.add(btnOK, BorderLayout.SOUTH);
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
		if (listener != null) {
			this.listeners.add(listener);
		}
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
					this.listeners.remove(l);
				}
			}
		}
	}

	/**
	 * Affiche la bo�te de dialogue de saisie du code de triche, et enregistre le listener.
	 * 
	 * @param listener
	 *            L'�couteur d'�v�nement Validation. Ne doit pas �tre null.
	 */
	public static void showDialog(final ValidationListener listener) {
		HighScoreDialog dlg = new HighScoreDialog(listener);
		dlg.setVisible(true);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals(HighScoreDialog.BTN_OK_ACTION_COMMAND)) {
			this.ok_Clicked();
		} else if (e.getActionCommand().equals(HighScoreDialog.BTN_CANCEL_ACTION_COMMAND)) {
			this.dispose();
		}
	}

	private void ok_Clicked() {
		this.onValidated(new ValidationEvent(this.getClass(), new String(this.txtName.getText())));
		this.dispose();
	}
}