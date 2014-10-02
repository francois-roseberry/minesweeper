package minesweeper.form.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import minesweeper.model.DifficultyLevel;
import minesweeper.model.data.access.Score;
import minesweeper.model.data.access.ScoreManager;


public class BestTimesDialog extends JDialog implements ActionListener
{
	private static final String TITLE = "Meilleurs temps";
	private static final String RESET_ACTION_COMMAND = "Effacer les scores";
	private static final String CLOSE_ACTION_COMMAND = "Fermer";
	
	private ScoreManager scoreMan = new ScoreManager();
	
	private JLabel lblBeginnerScore = new JLabel();
	private JLabel lblMediumScore = new JLabel();
	private JLabel lblExpertScore = new JLabel();
	
	private JLabel lblBeginnerName = new JLabel();
	private JLabel lblMediumName = new JLabel();
	private JLabel lblExpertName = new JLabel();
	
	/**
	 * Constructeur.
	 *
	 */
	public BestTimesDialog()
	{
		super();
		this.setTitle(BestTimesDialog.TITLE);
		this.setResizable(false);
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
		this.updateScores();
		Box hbMain = Box.createHorizontalBox();
		Box vbMain = Box.createVerticalBox();
		
		hbMain.add(Box.createHorizontalStrut(10));
		hbMain.add(vbMain);
		hbMain.add(Box.createHorizontalStrut(10));
		
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(this.initializeScorePanel());
		vbMain.add(Box.createVerticalStrut(10));
		vbMain.add(this.initializeButtonPanel());
		vbMain.add(Box.createVerticalStrut(10));
		
		this.add(hbMain);
	}
	
	private JPanel initializeScorePanel()
	{	
		Box hbMain = Box.createHorizontalBox();
		JPanel panMain = new JPanel();
		
		JPanel panLevels = new JPanel(new GridLayout(3, 1));
		JPanel panScores = new JPanel(new GridLayout(3, 1));
		JPanel panNames = new JPanel(new GridLayout(3, 1));
		
		hbMain.add(panLevels);
		hbMain.add(Box.createHorizontalStrut(50));
		hbMain.add(panScores);
		hbMain.add(Box.createHorizontalStrut(50));
		hbMain.add(panNames);
		
		panMain.add(hbMain);
		
		panLevels.add(new JLabel("Débutant"));
		panLevels.add(new JLabel("Intermédiaire"));
		panLevels.add(new JLabel("Expert"));
		
		panScores.add(this.lblBeginnerScore);
		panScores.add(this.lblMediumScore);
		panScores.add(this.lblExpertScore);
		
		panNames.add(this.lblBeginnerName);
		panNames.add(this.lblMediumName);
		panNames.add(this.lblExpertName);
		
		return panMain;
	}
	
	private JPanel initializeButtonPanel()
	{
		JPanel panMain = new JPanel(new BorderLayout());
		Box hbMain = Box.createHorizontalBox();
		
		JButton btnReset = new JButton(BestTimesDialog.RESET_ACTION_COMMAND);
		btnReset.addActionListener(this);
		btnReset.setActionCommand(BestTimesDialog.RESET_ACTION_COMMAND);
		
		JButton btnClose = new JButton(BestTimesDialog.CLOSE_ACTION_COMMAND);
		btnClose.addActionListener(this);
		btnClose.setActionCommand(BestTimesDialog.CLOSE_ACTION_COMMAND);
		
		hbMain.add(Box.createHorizontalGlue());
		hbMain.add(btnReset);
		hbMain.add(Box.createHorizontalStrut(10));
		hbMain.add(btnClose);
		hbMain.add(Box.createHorizontalGlue());
		
		panMain.add(hbMain);
		
		return panMain;
	}
	
	private void updateScores()
	{
		Score beginnerScore = this.scoreMan.readScore(DifficultyLevel.BEGINNER);
		Score midScore = this.scoreMan.readScore(DifficultyLevel.MEDIUM);
		Score expertScore = this.scoreMan.readScore(DifficultyLevel.EXPERT);
		
		this.lblBeginnerScore.setText("" + beginnerScore.getScore());
		this.lblMediumScore.setText("" + midScore.getScore());
		this.lblExpertScore.setText("" + expertScore.getScore());
		
		this.lblBeginnerName.setText(beginnerScore.getName());
		this.lblMediumName.setText(midScore.getName());
		this.lblExpertName.setText(expertScore.getName());
	}
	
	/**
	 * Affiche la boîte de dialogue de saisie du code de triche,
	 * et enregistre le listener.
	 * 
	 */
	public static void showDialog()
	{
		BestTimesDialog dlg = new BestTimesDialog();
		dlg.setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(BestTimesDialog.RESET_ACTION_COMMAND))
		{ this.reset_Clicked(); }
		else if (e.getActionCommand().equals(BestTimesDialog.CLOSE_ACTION_COMMAND))
		{ this.dispose(); }
	}
	
	private void reset_Clicked()
	{
		this.scoreMan.resetScores();
		this.updateScores();
	}
}
