package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.MultiplayerAction;
import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;

/**
 * Class for the button to go to the MultiPlayer menu
 */
public class MultiplayerButton extends JButton {

    /**
     * Constructor for the button, creates corresponding action and sets button properties
     * @param frame frame sent to action
     */
    public MultiplayerButton(AsteroidsFrame frame) {
        super(new MultiplayerAction(frame));
        setButtonProperties();
    }

    /**
     * Sets the button properties
     */
    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
    }

}