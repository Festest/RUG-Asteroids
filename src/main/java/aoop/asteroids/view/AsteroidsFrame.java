package aoop.asteroids.view;

import aoop.asteroids.model.DataBase;
import aoop.asteroids.model.Game;
import aoop.asteroids.view.menu.MainMenuPanel;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;
import aoop.asteroids.view.menu.ScoreboardMenuPanel;

import javax.swing.*;
import java.awt.*;

/**
 * The main window that's used for displaying the game.
 */
public class AsteroidsFrame extends JFrame {
	/**
	 * The title which appears in the upper border of the window.
	 */
	private static final String WINDOW_TITLE = "Asteroids";

	/**
	 * The size that the window should be.
	 */
	public static final Dimension WINDOW_SIZE = new Dimension(800, 800);

	/** The game model. */
	private Game game;

	private JPanel menuPanel;
	private AsteroidsPanel asteroidsPanel;
	private JTabbedPane tabbedPane;
	private final DataBase data;

	/**
	 * Constructs the game's main window.
	 */
	public AsteroidsFrame () {
		this.data = new DataBase();
		this.game = new Game(data);
		this.initSwingUI();
	}

	/**
	 * A helper method to do the tedious task of initializing the Swing UI components.
	 */
	private void initSwingUI() {
		// Basic frame properties.
		this.setTitle(WINDOW_TITLE);
		this.setSize(WINDOW_SIZE);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add the custom panel that the game will be drawn to.
		this.asteroidsPanel = new AsteroidsPanel(this.game);
		this.asteroidsPanel.requestFocus();

		// Create the Tabs
		this.tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Menu", menuPanel);
		tabbedPane.addTab("Game", asteroidsPanel);

		this.add(tabbedPane, BorderLayout.CENTER);

		// Start Interactive Menu
		setMenuPage(0);

		// Initialize
		this.setVisible(true);
	}

	/**
	 * Choose which tab should be selected and have the focus
	 * @param tab integer representing the tab to be chosen
	 */
	public void setTab(int tab) {
		tabbedPane.setSelectedIndex(tab);
		tabbedPane.getComponentAt(tab).requestFocus();
	}

	/**
	 * Sets the options to be displayed in the menu
	 * 0: Main Menu
	 * 1: Multiplayer Host Menu
	 * 2: InGame Singleplayer Menu
	 * 3: InGame Multiplayer Client Menu
	 * 4: Scoreboard
	 * @param page the page number
	 */
	public void setMenuPage(int page) {
		this.tabbedPane.remove(menuPanel);

		switch (page) {
			case 0: {
				this.menuPanel = new MainMenuPanel(game, this);
			}
			break;
			case 1: {
				this.menuPanel = new MultiplayerMenuPanel(this);
			}
			break;
			case 2: {
				this.menuPanel = new ScoreboardMenuPanel(this, this.data);
			}
		}

		this.tabbedPane.insertTab("Menu", null, menuPanel, null, 0);
		tabbedPane.setSelectedIndex(0);
	}

	/**
	 * Set the game of the frame
	 * @param game the game to be used
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * @return the current game of the frame
	 */
	public Game getGame() {
		return this.game;
	}
}