package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.JoinGameAction;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;

import javax.swing.*;

/**
 * Class for button to join a game
 */
public class JoinGameButton extends JButton {

    /**
     * Constructor for button, creates corresponding action and sets button properties
     * @param panel panel sent to action
     * @param frame frame sent to action
     * @param textField textfield sent to action, containing the ip
     */
    public JoinGameButton(MultiplayerMenuPanel panel, AsteroidsFrame frame, JFormattedTextField textField) {
        super(new JoinGameAction(panel, frame, textField));
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