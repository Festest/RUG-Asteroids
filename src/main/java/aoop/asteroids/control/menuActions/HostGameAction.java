package aoop.asteroids.control.menuActions;

import aoop.asteroids.model.Game;
import aoop.asteroids.model.Networking.Server.Server;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action to start hosting a game
 */
public class HostGameAction extends AbstractAction {
    private final Game mpGame;
    private final AsteroidsFrame frame;
    private final MultiplayerMenuPanel panel;

    /**
     * Constructor of the action, gets the game of the frame
     * @param panel the panel to operate on
     * @param frame the frame containing the game and to operate on
     */
    public HostGameAction(MultiplayerMenuPanel panel, AsteroidsFrame frame) {
        super("Host Game");
        this.mpGame = frame.getGame();
        this.frame = frame;
        this.panel = panel;
    }

    /**
     * Invoked when an action occurs. Sets, gets and starts the server. Initialise and start the multiplayer game
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.panel.setNewServer();
        Server server = panel.getServer();
        this.frame.setGame(mpGame);
        this.mpGame.quit();
        this.mpGame.setIsHosting(true);
        this.mpGame.setIsSingleplayer(false);
        this.mpGame.setPlayerCount(1);
        this.mpGame.initializeGameData();
        this.mpGame.start();

        server.start();

        this.panel.setPage(2);
        this.frame.setTab(1);
    }
}
