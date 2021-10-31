package aoop.asteroids.control.menuActions;

import aoop.asteroids.model.Networking.Server.Server;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class for the action when a user stops hosting a game
 */
public class StopHostingAction extends AbstractAction {
    private final MultiplayerMenuPanel panel;
    private final AsteroidsFrame frame;

    /**
     * Constructor of the action
     * @param panel panel to be used
     * @param frame frame to be used
     */
    public StopHostingAction(MultiplayerMenuPanel panel, AsteroidsFrame frame) {
        super("Stop Hosting");
        this.panel = panel;
        this.frame = frame;
    }

    /**
     * Invoked when an action occurs. Terminates the server and sets the page of the panel and the tab of the frame
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
        this.panel.setPage(1);
        this.frame.setTab(1);
    }
}