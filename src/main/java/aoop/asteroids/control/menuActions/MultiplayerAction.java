package aoop.asteroids.control.menuActions;

import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action to set the frame such that it displays the multiplayer options
 */
public class MultiplayerAction extends AbstractAction {
    private final AsteroidsFrame frame;

    /**
     * Constructor of the action
     * @param frame the AsteroidsFrame to operate on
     */
    public MultiplayerAction(AsteroidsFrame frame) {
        super("Multiplayer");
        this.frame = frame;
    }

    /**
     * Invoked when an action occurs. Sets the Menu Page of the frame to 1, such that it displays the multiplayer options
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.setMenuPage(1);
    }
}
