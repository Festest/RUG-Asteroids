package aoop.asteroids.control.menuActions;

import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action to go back to the main menu
 */
public class GoBackAction extends AbstractAction {
    private final AsteroidsFrame frame;

    /**
     * Constructor of the action
     * @param frame The AsteroidsFrame to operate on
     */
    public GoBackAction(AsteroidsFrame frame) {
        super("Go Back");
        this.frame = frame;
    }

    /**
     * Invoked when an action occurs. Sets the MenuPage of the frame to 0, so the buttons of the main menu are shown
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.setMenuPage(0);
    }
}
