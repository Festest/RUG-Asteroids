package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.view.menu.MenuPanel;
import aoop.asteroids.control.menuActions.NewGameAction;
import aoop.asteroids.model.Game;
import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;

/**
 * Class for button that creates a new game
 */
public class NewGameButton extends JButton {

    /**
     * Constructor for the button, creates the corresponding action and sets the button properties
     * @param game the game sent to the action
     * @param frame frame sent to the action
     * @param panel panel sent to the action
     * @param username username sent to the action
     */
    public NewGameButton(Game game, AsteroidsFrame frame, MenuPanel panel, JFormattedTextField username) {
        super(new NewGameAction(game, frame, panel, username));
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
