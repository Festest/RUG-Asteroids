package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.StopHostingAction;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;

import javax.swing.*;

/**
 * Class for the button to stop hosting
 */
public class StopHostingButton extends JButton {

    /**
     * Constructor for the button, creates a new action and sets the button properties
     * @param panel the panel sent to the action
     * @param frame the frame sent to the action
     */
    public StopHostingButton(MultiplayerMenuPanel panel, AsteroidsFrame frame) {
        super(new StopHostingAction(panel, frame));
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