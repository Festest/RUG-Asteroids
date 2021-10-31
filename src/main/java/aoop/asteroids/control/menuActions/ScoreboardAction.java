package aoop.asteroids.control.menuActions;

import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class for the Scoreboard Action
 */
public class ScoreboardAction extends AbstractAction {
    private final AsteroidsFrame frame;

    /**
     * Constructor for the action
     * @param frame the AsteroidsFrame to operate on
     */
    public ScoreboardAction(AsteroidsFrame frame) {
        super("Show Scoreboard");
        this.frame = frame;
    }

    /**
     * Invoked when an action occurs. Sets the MenuPage of the frame to 2, such that the scoreboard buttons will be shown
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.setMenuPage(2);
    }
}