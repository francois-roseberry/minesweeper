package minesweeper.form.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import minesweeper.form.GameGrid;
import minesweeper.model.CustomGridObject;
import minesweeper.model.event.ValidationEvent;
import minesweeper.model.event.ValidationListener;


public class CustomGridDialog extends JDialog implements ActionListener
{
	private static final String TITLE = "Grille personnalisée";
	private static final String BTN_OK_ACTION_COMMAND = "OK";
	private static final String BTN_CANCEL_ACTION_COMMAND = "Annuler";
	
	private static final String COLUMN_PROMPT = "Hauteur :";
	private static final String ROW_PROMPT = "Largeur : ";
	private static final String MINES_PROMPT = "Mines : ";
	
	// Liste des écouteurs d'évènement Validation.
	private ArrayList<ValidationListener> listeners = new ArrayList<ValidationListener>();
	
	private JSpinner spnRows = new JSpinner(new SpinnerNumberModel(15, GameGrid.MIN_SQUARES_IN_A_LINE, GameGrid.MAX_SQUARES_IN_A_LINE, 1));
	private JSpinner spnColumns = new JSpinner(new SpinnerNumberModel(15, GameGrid.MIN_SQUARES_IN_A_LINE, GameGrid.MAX_SQUARES_IN_A_LINE, 1));
	private JSpinner spnMines = new JSpinner(new SpinnerNumberModel(20, GameGrid.MIN_MINES, GameGrid.MAX_MINES, 1));
	
	/**
	 * Constructeur.
	 * 
	 * @param listener	L'écouteur d'évènements Validation.
	 */
	public CustomGridDialog(ValidationListener listener)
	{
		super();
		this.setTitle(CustomGridDialog.TITLE);
		this.setResizable(false);
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addValidationListener(listener);
		this.initializeComponent();
		this.pack();
		this.centerWindow();
	}
	
	/*
	 * Centre la boîte de dialogue.
	 * 
	 */
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
		
		hbMain.add(Box.createHorizontalStrut(10));
		hbMain.add(vbMain);
		hbMain.add(Box.createHorizontalStrut(10));
		
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(this.initializeInputPanel());
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(this.initializeButtonPanel());
		vbMain.add(Box.createVerticalStrut(10));
		
		this.add(hbMain);
	}
	
	private JPanel initializeInputPanel()
	{
		JPanel pan = new JPanel();
		Box hbMain = Box.createHorizontalBox();
		JPanel panText = new JPanel(new GridLayout(3, 1));
		JPanel panControl = new JPanel(new GridLayout(3, 1));
		
		hbMain.add(panText);
		hbMain.add(Box.createHorizontalStrut(20));
		hbMain.add(panControl);
		
		panText.add(new JLabel(CustomGridDialog.COLUMN_PROMPT));
		panText.add(new JLabel(CustomGridDialog.ROW_PROMPT));
		panText.add(new JLabel(CustomGridDialog.MINES_PROMPT));
		
		panControl.add(this.spnColumns);
		panControl.add(this.spnRows);
		panControl.add(this.spnMines);
		
		pan.add(hbMain);
		return pan;
	}
	
	private JPanel initializeButtonPanel()
	{
		JPanel pan = new JPanel(new BorderLayout());
		Box hbMain = Box.createHorizontalBox();
		JButton btnOK = new JButton(CustomGridDialog.BTN_OK_ACTION_COMMAND);
		btnOK.setActionCommand(CustomGridDialog.BTN_OK_ACTION_COMMAND);
		btnOK.addActionListener(this);
		JButton btnCancel = new JButton(CustomGridDialog.BTN_CANCEL_ACTION_COMMAND);
		btnCancel.setActionCommand(CustomGridDialog.BTN_CANCEL_ACTION_COMMAND);
		btnCancel.addActionListener(this);
		
		hbMain.add(Box.createHorizontalGlue());
		hbMain.add(btnOK);
		hbMain.add(Box.createHorizontalStrut(10));
		hbMain.add(btnCancel);
		hbMain.add(Box.createHorizontalGlue());
		
		pan.add(hbMain);
		return pan;
	}
	
	private void onValidated(ValidationEvent e)
	{
		for (ValidationListener listener : this.listeners)
		{
			listener.validated(e);
		}
	}
	
	/**
	 * Enregistre un écouteur d'évènement Validation.
	 * 
	 * @param listener	L'écouteur d'évènement. Ne doit pas être null.
	 */
	public void addValidationListener(ValidationListener listener)
	{
		if (listener != null)
		{
			this.listeners.add(listener);
		}
	}
	
	/**
	 * Désenregistre un écouteur d'évènement Validation.
	 * 
	 * @param listener	L'écouteur d'évènement. Ne doit pas être null.
	 */
	public void removeValidationListener(ValidationListener listener)
	{
		if (listener != null)
		{
			for (ValidationListener l : this.listeners)
			{
				if (l.equals(listener))
				{
					this.listeners.remove(l);
				}
			}
		}
	}
	
	/**
	 * Affiche la boîte de dialogue de saisie du code de triche,
	 * et enregistre le listener.
	 * 
	 * @param listener	L'écouteur d'évènement Validation. Ne
	 * 					doit pas être null.
	 */
	public static void showDialog(ValidationListener listener)
	{
		CustomGridDialog dlg = new CustomGridDialog(listener);
		dlg.setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(CustomGridDialog.BTN_OK_ACTION_COMMAND))
		{ this.ok_Clicked(); }
		else if (e.getActionCommand().equals(CustomGridDialog.BTN_CANCEL_ACTION_COMMAND))
		{ this.dispose(); }
	}
	
	private void ok_Clicked()
	{
		int rows = Integer.parseInt(this.spnRows.getValue().toString());
		int columns = Integer.parseInt(this.spnColumns.getValue().toString());
		int mines = Integer.parseInt(this.spnMines.getValue().toString());
		System.out.println("Valeurs sélectionnés :");
		System.out.println(rows + " cases par rangée, " + columns + " cases par colonne, " + mines + " mines");
		CustomGridObject cgo = CustomGridObject.createCustomGridObject(rows, columns, mines);
		this.onValidated(new ValidationEvent(this.getClass(), cgo));
		this.dispose();
	}
}
