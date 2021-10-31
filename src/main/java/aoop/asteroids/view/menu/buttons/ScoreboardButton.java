package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.ScoreboardAction;
import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;

/**
 * Class for the button to go the scoreboard
 */
public class ScoreboardButton extends JButton {

    /**
     * Constructor for the button, creates corresponding action and sets button properties
     * @param frame frame sent to the action
     */
    public ScoreboardButton(AsteroidsFrame frame) {
        super(new ScoreboardAction(frame));
        setButtonProperties();
    }

    /**
     * Set the button properties
     */
    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
    }

}