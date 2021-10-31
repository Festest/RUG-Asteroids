package aoop.asteroids.control.menuActions;

import aoop.asteroids.model.Game;
import aoop.asteroids.view.menu.MainMenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class for the action when a single player game is quit
 */
public class QuitSPGameAction extends AbstractAction {
    private final MainMenuPanel mainMenuPanel;
    private final Game game;

    /**
     * Constructor for the action
     * @param game the game to be quit
     * @param mainMenuPanel the panel to return to
     */
    public QuitSPGameAction(Game game, MainMenuPanel mainMenuPanel) {
        super("Quit Game");
        this.mainMenuPanel = mainMenuPanel;
        this.game = game;
    }

    /**
     * Invoked when an action occurs. Quits and resets the game and goes back to the Main Menu
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.game.quit();
        this.game.initializeGameData();
        this.mainMenuPanel.setPage(0);
    }
}