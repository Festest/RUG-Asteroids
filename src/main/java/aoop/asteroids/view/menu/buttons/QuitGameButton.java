package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.ForceCloseAction;

import javax.swing.*;

/**
 * Class for button to quit the game
 */
public class QuitGameButton extends JButton {

    /**
     * Constructor for button, creates corresponding action and sets button properties
     */
    public QuitGameButton() {
        super(new ForceCloseAction());
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