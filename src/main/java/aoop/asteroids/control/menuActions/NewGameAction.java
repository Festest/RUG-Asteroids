package aoop.asteroids.control.menuActions;

import aoop.asteroids.model.Game;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This action represents when a user wants to quit the current game and start a new one.
 */
public class NewGameAction extends AbstractAction {
	/**
	 * A reference to the game that should be reset/initialized when the user does this action.
	 */
	private final Game game;
	private final AsteroidsFrame frame;
	private final MenuPanel panel;
	private final JFormattedTextField username;

	/**
	 * Constructs the action. Calls the parent constructor to set the name of this action.
	 * @param frame The frame that contains the game model that will be used.
	 * @param username The username to be displayed above the spaceship.
	 */
	public NewGameAction(Game game, AsteroidsFrame frame, MenuPanel panel, JFormattedTextField username) {
		super("New Game");
		this.game = game;
		this.frame = frame;
		this.panel = panel;
		this.username = username;
	}

	/**
	 * Invoked when an action occurs.
	 *
	 * @param event The event to be processed. In this case, no information from the actual event is needed. Simply the
	 *              knowledge that it occurred is enough.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		this.frame.setGame(game);
		this.game.setIsHosting(true);
		this.game.setIsSingleplayer(true);

		this.game.quit(); // Try to stop the game if it's currently running.
		this.game.setPlayerCount(1);
		this.game.initializeGameData(); // Resets the game's objects to their default state.
		this.game.updateSpaceship(username.getText(), JColorChooser.showDialog(null, "Choose you spaceship color", Color.LIGHT_GRAY));
		this.game.start(); // Spools up the game's engine and starts the main game loop.

		this.panel.setPage(1);
		this.frame.setTab(1);
	}
}