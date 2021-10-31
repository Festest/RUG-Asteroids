package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.HostGameAction;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;

import javax.swing.*;

/**
 * Class for button to host a game
 */
public class HostGameButton extends JButton {

    /**
     * Constructor for button, creates corresponding action and sets button properties
     * @param panel panel sent to action
     * @param frame frame sent to action
     */
    public HostGameButton(MultiplayerMenuPanel panel, AsteroidsFrame frame) {
        super(new HostGameAction(panel, frame));
        setButtonProperties();
    }

    /**
     * Sets the properties of the button
     */
    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
    }
}