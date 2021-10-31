package aoop.asteroids.view.menu.buttons;

import aoop.asteroids.control.menuActions.ClearScoreboardAction;
import aoop.asteroids.model.DataBase;
import aoop.asteroids.view.menu.ScoreboardMenuPanel;

import javax.swing.*;

/**
 * Class for the button to clear the scoreboard
 */
public class ClearScoreboardButton extends JButton{

    /**
     * Constructor for button, creates corresponding action and sets button properties
     * @param panel panel sent to action
     * @param data database sent to action
     */
    public ClearScoreboardButton(ScoreboardMenuPanel panel, DataBase data) {
        super(new ClearScoreboardAction(panel, data));
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
