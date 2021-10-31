package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.QuitMPGameAction;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.MultiplayerMenuPanel;

import javax.swing.*;

/**
 * Class for button to quit a multiplayer game
 */
public class QuitMPGameButton extends JButton {

    /**
     * Constructor for the button, creates corresponding action and sets button properties
     * @param panel panel sent to action
     * @param frame frame sent to action
     */
    public QuitMPGameButton(MultiplayerMenuPanel panel, AsteroidsFrame frame) {
        super(new QuitMPGameAction(panel, frame));
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