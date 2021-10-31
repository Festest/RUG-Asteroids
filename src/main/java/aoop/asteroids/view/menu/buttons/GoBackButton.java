package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.GoBackAction;
import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;

/**
 * Class for the button to go back to the Main Menu
 */
public class GoBackButton extends JButton {

    /**
     * Constructor for button, creates corresponding action and sets button properties
     * @param frame frame sent to action
     */
    public GoBackButton(AsteroidsFrame frame) {
        super(new GoBackAction(frame));
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