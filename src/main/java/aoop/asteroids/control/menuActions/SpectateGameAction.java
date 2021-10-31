package aoop.asteroids.control.menuActions;

import aoop.asteroids.model.Game;
import aoop.asteroids.model.Networking.Client.Client;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.Integer.parseInt;

/**
 * Class for the action when the Spectate Game button is pressed
 */
public class SpectateGameAction extends AbstractAction {
    private final MultiplayerMenuPanel panel;
    private final AsteroidsFrame frame;
    private final JFormattedTextField textField;
    private InetAddress ip;
    private int port;

    /**
     * Constructor for the action
     * @param panel panel to be used
     * @param frame frame to be used
     * @param textField textfield containing the IP address of the game to be spectated
     */
    public SpectateGameAction(MultiplayerMenuPanel panel, AsteroidsFrame frame, JFormattedTextField textField) {
        super("Spectate Game");
        this.panel = panel;
        this.frame = frame;
        this.textField = textField;
    }

    /**
     * Invoked when an action occurs. Gets the game from the frame, starts it and connects to it as a spectator.
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Game game = this.frame.getGame();
        game.setIsHosting(false);
        game.setIsSingleplayer(false);
        game.start();

        this.frame.setTab(1);
        this.panel.setPage(1);

        String string = textField.getText();
        String[] ipPort = string.split(":");

        connectToGame(ipPort);
    }

    /**
     * Start a new client that will spectate the game
     * @param ipPort the ip port to connect to
     */
    private void connectToGame(String[] ipPort) {
        try {
            this.ip = InetAddress.getByName(ipPort[0]);
            this.port = parseInt(ipPort[1]);
            if (port <= 65353) {
                Thread t = new Thread(new Client(ip, port, frame, true));
                t.start();
            }
            else JOptionPane.showMessageDialog(null, "No valid Port entered");
        } catch (UnknownHostException unknownHostException) {
            JOptionPane.showMessageDialog(null, "No valid IP entered");
        }
    }
}
