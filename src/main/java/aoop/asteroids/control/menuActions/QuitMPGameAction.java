package aoop.asteroids.control.menuActions;

import aoop.asteroids.model.Game;
import aoop.asteroids.model.Networking.Server.Server;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class for the action to quit a multiplayer game
 */
public class QuitMPGameAction extends AbstractAction {
    private final MultiplayerMenuPanel panel;
    private final AsteroidsFrame frame;
    private final Game game;

    /**
     * Constructor for the action, gets the game from the frame
     * @param panel the MultiPlayer panel to operate on
     * @param frame the AsteroidsFrame to operate on
     */
    public QuitMPGameAction(MultiplayerMenuPanel panel, AsteroidsFrame frame) {
        super("Quit Game");
        this.panel = panel;
        this.frame = frame;
        this.game = frame.getGame();
    }

    /**
     * Invoked when an action occurs. Terminates the server, quits and resets the game. Set the frame and panel such that the player is returned to the main menu.
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Server server = panel.getServer();

        server.terminate();
        try {
            Thread.sleep(100);
        } catch (InterruptedException interruptedException) {
            JOptionPane.showMessageDialog(null, "Unable to sleep");
        }
        this.game.quit();
        this.game.setIsSingleplayer(true);
        this.game.initializeGameData();
        this.panel.setPage(0);
        this.frame.setTab(0);
    }
}
