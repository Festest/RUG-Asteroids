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
 * Action to join a game, given an ip
 */
public class JoinGameAction extends AbstractAction {
    private final MultiplayerMenuPanel panel;
    private final AsteroidsFrame frame;
    private final JFormattedTextField textField;
    private InetAddress ip;
    private int port;

    /**
     * Constructor of the action
     * @param panel the panel to operate on
     * @param frame the frame containing the game and to operate on
     * @param textField the textfield containing the ip of the server to connect to
     */
    public JoinGameAction(MultiplayerMenuPanel panel, AsteroidsFrame frame, JFormattedTextField textField) {
        super("Join Game");
        this.panel = panel;
        this.frame = frame;
        this.textField = textField;
    }

    /**
     * Invoked when an action occurs. Gets the game from the frame, starts it and connects to it as a player
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Game game = this.frame.getGame();

        String string = textField.getText();
        String[] ipPort = string.split(":");
        if (ipPort.length != 2) {
            JOptionPane.showMessageDialog(null, "No valid IP or Port entered");
            return;
        }

        game.setIsSingleplayer(false);
        game.setIsHosting(false);
        game.start();

        connectToGame(ipPort);

        this.panel.setPage(1);
        this.frame.setTab(1);
    }

    /**
     * Start a new client that will connect to the game
     * @param ipPort the ip port to connect to
     */
    private void connectToGame(String[] ipPort) {
        try {
            this.ip = InetAddress.getByName(ipPort[0]);
            this.port = parseInt(ipPort[1]);
            if (port <= 65353) {
                Thread t = new Thread(new Client(ip, port, frame, false));
                t.start();
            }
            else JOptionPane.showMessageDialog(null, "No valid Port entered");
        } catch (UnknownHostException unknownHostException) {
            JOptionPane.showMessageDialog(null, "No valid IP entered");
            return;
        }
    }
}