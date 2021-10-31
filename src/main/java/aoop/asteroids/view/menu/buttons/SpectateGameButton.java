package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.SpectateGameAction;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;

import javax.swing.*;

/**
 * Class for the button to spectate a game
 */
public class SpectateGameButton extends JButton {

    /**
     * Constructor for the button, creates the corresponding action and sets button properties
     * @param panel panel sent to the action
     * @param frame frame sent to the action
     * @param textField textField where the ip is put
     */
    public SpectateGameButton(MultiplayerMenuPanel panel, AsteroidsFrame frame, JFormattedTextField textField) {
        super(new SpectateGameAction(panel, frame, textField));
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
