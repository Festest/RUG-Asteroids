package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.QuitSPGameAction;
import aoop.asteroids.model.Game;
import aoop.asteroids.view.menu.MainMenuPanel;

import javax.swing.*;

/**
 * Class for the button to quit a single player game
 */
public class QuitSPGameButton extends JButton {

    /**
     * Constructor for the button, creates corresponding action and sets button properties
     * @param game the game sent to the action
     * @param mainMenuPanel the panel sent to the action
     */
    public QuitSPGameButton(Game game, MainMenuPanel mainMenuPanel) {
        super(new QuitSPGameAction(game, mainMenuPanel));
        setButtonProperties();
    }

    /**
     * Set button properties
     */
    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
    }

}