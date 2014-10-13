package minesweeper.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import minesweeper.form.dialogs.AboutDialog;
import minesweeper.form.dialogs.BestTimesDialog;
import minesweeper.form.dialogs.CheatDialog;
import minesweeper.form.dialogs.CustomGridDialog;
import minesweeper.model.CustomGridObject;
import minesweeper.model.DifficultyLevel;
import minesweeper.model.event.ValidationEvent;
import minesweeper.model.event.ValidationListener;

/**
 * La classe AppMenu repr�sente la barre de menus de l'application.
 * 
 * @author David Maltais
 * @author Fran�ois Roseberry
 * 
 */
@SuppressWarnings("serial")
public class AppMenu extends JMenuBar implements ActionListener, ValidationListener {

	private static final String GAME_MENU_TITLE = "Partie";
	private static final char GAME_MNEMONIC = 'P';
	private static final String NEW_GAME_ACTION_COMMAND = "Nouveau";
	private static final char NEW_GAME_MNEMONIC = 'N';
	private static final String BEGINNER_ACTION_COMMAND = "D�butant";
	private static final char BEGINNER_MNEMONIC = 'D';
	private static final String MEDIUM_ACTION_COMMAND = "Interm�diaire";
	private static final char MEDIUM_MNEMONIC = 'I';
	private static final String EXPERT_ACTION_COMMAND = "Expert";
	private static final char EXPERT_MNEMONIC = 'E';
	private static final String CUSTOM_ACTION_COMMAND = "Personnalis�...";
	private static final char CUSTOM_MNEMONIC = 'P';
	private static final String CHEAT_ACTION_COMMAND = "Tricher...";
	private static final char CHEAT_MNEMONIC = 'T';
	private static final String BEST_SCORES_ACTION_COMMAND = "Meilleurs temps...";
	private static final char BEST_SCORES_MNEMONIC = 'M';
	private static final String EXIT_ACTION_COMMAND = "Quitter";
	private static final char EXIT_MNEMONIC = 'Q';
	private static final String HELP_MENU_TITLE = "?";
	private static final char HELP_MENU_MNEMONIC = '?';
	private static final String HELP_ACTION_COMMAND = "Aide...";
	private static final char HELP_MNEMONIC = 'A';
	private static final String ABOUT_ACTION_COMMAND = "� propos...";
	private static final char ABOUT_MNEMONIC = 'p';
	private static final String HELP_TITLE = "Aide du d�mineur";
	private static final String HELP_TEXT = "Le but du jeu est de ...\nc'est l'�quivalent du d�mineur de Windows.\nPas besoin de r�-expliquer les r�gles ...";

	private boolean levelBeginnerChosen = true;
	private boolean levelMediumChosen = false;
	private boolean levelExpertChosen = false;

	/**
	 * Constructeur.
	 * 
	 */
	public AppMenu() {
		super();

		initializeComponent();
	}

	private void initializeComponent() {
		// Menu Partie.
		JMenu mnuGame = new JMenu(AppMenu.GAME_MENU_TITLE);
		mnuGame.setMnemonic(AppMenu.GAME_MNEMONIC);
		add(mnuGame);

		// Item de menu Partie/Nouveau.
		JMenuItem mniNewGame = new JMenuItem(AppMenu.NEW_GAME_ACTION_COMMAND, AppMenu.NEW_GAME_MNEMONIC);
		mniNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		mniNewGame.addActionListener(this);
		mnuGame.add(mniNewGame);
		mnuGame.addSeparator();

		ButtonGroup btgLevels = new ButtonGroup();
		// Item de menu Partie/D�butant.
		JRadioButtonMenuItem mrbBeginner = new JRadioButtonMenuItem(AppMenu.BEGINNER_ACTION_COMMAND);
		mrbBeginner.setMnemonic(AppMenu.BEGINNER_MNEMONIC);
		mrbBeginner.setSelected(true);
		mrbBeginner.addActionListener(this);
		mrbBeginner.setActionCommand(AppMenu.BEGINNER_ACTION_COMMAND);

		// Item de menu Partie/Nouveau/Interm�diaire.
		JRadioButtonMenuItem mrbMedium = new JRadioButtonMenuItem(AppMenu.MEDIUM_ACTION_COMMAND);
		mrbMedium.setMnemonic(AppMenu.MEDIUM_MNEMONIC);
		mrbMedium.addActionListener(this);
		mrbMedium.setActionCommand(AppMenu.MEDIUM_ACTION_COMMAND);

		// Item de menu Partie/Nouveau/Expert.
		JRadioButtonMenuItem mrbExpert = new JRadioButtonMenuItem(AppMenu.EXPERT_ACTION_COMMAND);
		mrbExpert.setMnemonic(AppMenu.EXPERT_MNEMONIC);
		mrbExpert.addActionListener(this);
		mrbExpert.setActionCommand(AppMenu.EXPERT_ACTION_COMMAND);

		// Item de menu Partie/Personnalis�.
		JRadioButtonMenuItem mrbCustom = new JRadioButtonMenuItem(AppMenu.CUSTOM_ACTION_COMMAND);
		mrbCustom.setMnemonic(AppMenu.CUSTOM_MNEMONIC);
		mrbCustom.setActionCommand(AppMenu.CUSTOM_ACTION_COMMAND);
		mrbCustom.addActionListener(this);

		btgLevels.add(mrbBeginner);
		btgLevels.add(mrbMedium);
		btgLevels.add(mrbExpert);
		btgLevels.add(mrbCustom);

		mnuGame.add(mrbBeginner);
		mnuGame.add(mrbMedium);
		mnuGame.add(mrbExpert);
		mnuGame.add(mrbCustom);

		mnuGame.addSeparator();

		// Item de menu Partie/Tricher.
		JMenuItem mniCheat = new JMenuItem(AppMenu.CHEAT_ACTION_COMMAND, AppMenu.CHEAT_MNEMONIC);
		mniCheat.addActionListener(this);
		mniCheat.setActionCommand(AppMenu.CHEAT_ACTION_COMMAND);
		mnuGame.add(mniCheat);
		mnuGame.addSeparator();

		// Item de menu Partie/Meilleurs Temps.
		JMenuItem mniBestScores = new JMenuItem(AppMenu.BEST_SCORES_ACTION_COMMAND, AppMenu.BEST_SCORES_MNEMONIC);
		mniBestScores.addActionListener(this);
		mniBestScores.setActionCommand(AppMenu.BEST_SCORES_ACTION_COMMAND);
		mnuGame.add(mniBestScores);
		mnuGame.addSeparator();

		// Item de menu Partie/Quitter.
		JMenuItem mniClose = new JMenuItem(AppMenu.EXIT_ACTION_COMMAND, AppMenu.EXIT_MNEMONIC);
		mniClose.addActionListener(this);
		mniClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mniClose.setActionCommand(AppMenu.EXIT_ACTION_COMMAND);
		mnuGame.add(mniClose);

		// Menu "?".
		JMenu mnuHelp = new JMenu(AppMenu.HELP_MENU_TITLE);
		mnuHelp.setMnemonic(AppMenu.HELP_MENU_MNEMONIC);
		add(mnuHelp);

		// Item de menu Aide.
		JMenuItem mniHelp = new JMenuItem(AppMenu.HELP_ACTION_COMMAND, AppMenu.HELP_MNEMONIC);
		mniHelp.setActionCommand(AppMenu.HELP_ACTION_COMMAND);
		mniHelp.addActionListener(this);
		mnuHelp.add(mniHelp);

		mnuHelp.addSeparator();
		// Item de menu � Propos.
		JMenuItem mniAbout = new JMenuItem(AppMenu.ABOUT_ACTION_COMMAND, AppMenu.ABOUT_MNEMONIC);
		mniAbout.setActionCommand(AppMenu.ABOUT_ACTION_COMMAND);
		mniAbout.addActionListener(this);
		mnuHelp.add(mniAbout);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals(AppMenu.EXIT_ACTION_COMMAND)) {
			exit_Clicked();
		} else if (e.getActionCommand().equals(AppMenu.ABOUT_ACTION_COMMAND)) {
			about_Clicked();
		} else if (e.getActionCommand().equals(AppMenu.NEW_GAME_ACTION_COMMAND)) {
			newGame_Clicked();
		} else if (e.getActionCommand().equals(AppMenu.BEGINNER_ACTION_COMMAND)) {
			beginner_Clicked();
		} else if (e.getActionCommand().equals(AppMenu.MEDIUM_ACTION_COMMAND)) {
			medium_Clicked();
		} else if (e.getActionCommand().equals(AppMenu.EXPERT_ACTION_COMMAND)) {
			expert_Clicked();
		} else if (e.getActionCommand().equals(AppMenu.CUSTOM_ACTION_COMMAND)) {
			custom_Clicked();
		} else if (e.getActionCommand().equals(AppMenu.CHEAT_ACTION_COMMAND)) {
			cheat_Clicked();
		} else if (e.getActionCommand().equals(AppMenu.BEST_SCORES_ACTION_COMMAND)) {
			bestTimes_Clicked();
		} else if (e.getActionCommand().equals(AppMenu.HELP_ACTION_COMMAND)) {
			help_Clicked();
		}
	}

	public DifficultyLevel getSelectedGameLevel() {
		if (levelBeginnerChosen) {
			return DifficultyLevel.BEGINNER;
		}

		if (levelMediumChosen) {
			return DifficultyLevel.MEDIUM;
		}

		if (levelExpertChosen) {
			return DifficultyLevel.EXPERT;
		}

		return DifficultyLevel.CUSTOM;
	}

	private void help_Clicked() {
		JOptionPane.showMessageDialog(null, AppMenu.HELP_TEXT, AppMenu.HELP_TITLE, JOptionPane.PLAIN_MESSAGE);
	}

	private void bestTimes_Clicked() {
		BestTimesDialog.showDialog();
	}

	/*
	 * G�re la fermeture du jeu.
	 * 
	 */
	private void exit_Clicked() {
		AppFrame.getInstance().askExitQuestion();
	}

	/*
	 * Affiche le dialogue "� Propos"
	 * 
	 */
	private void about_Clicked() {
		AboutDialog.showDialog();
	}

	/*
	 * Affiche le dialogue permettant de saisir le mot de
	 * passe de triche.
	 * 
	 */
	private void cheat_Clicked() {
		CheatDialog.showDialog(GameBoard.getPassword(), this);
	}

	/*
	 * G�re le clic sur "Nouvelle Partie".
	 * 
	 */
	private void newGame_Clicked() {
		// Demande � la fen�tre de jeu de commencer une nouvelle partie.
		AppFrame.getInstance().startGame();
	}

	private void beginner_Clicked() {
		levelBeginnerChosen = true;
		levelMediumChosen = false;
		levelExpertChosen = false;
		newGame_Clicked();
	}

	private void medium_Clicked() {
		levelBeginnerChosen = false;
		levelMediumChosen = true;
		levelExpertChosen = false;
		newGame_Clicked();
	}

	private void expert_Clicked() {
		levelBeginnerChosen = false;
		levelMediumChosen = false;
		levelExpertChosen = true;
		newGame_Clicked();
	}

	private void custom_Clicked() {
		levelBeginnerChosen = false;
		levelMediumChosen = false;
		levelExpertChosen = false;
		CustomGridDialog.showDialog(this);
	}

	@Override
	public void validated(final ValidationEvent e) {
		if (e.getValidatedClass() == CheatDialog.class) {
			//System.out.println("Triche activ�e !");
			AppFrame.getInstance().getGameBoard().cheat();
		} else if (e.getValidatedClass() == CustomGridDialog.class) {
			if (e.getData() != null && e.getData() instanceof CustomGridObject) {
				CustomGridObject cgo = (CustomGridObject) e.getData();
				AppFrame.getInstance().startGame(
						DifficultyLevel.CUSTOM, cgo.getRows(),
						cgo.getColumns(), cgo.getMines());
			}
		}
	}
}
