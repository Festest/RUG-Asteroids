package aoop.asteroids.control.menuActions;

import aoop.asteroids.model.DataBase;
import aoop.asteroids.view.menu.ScoreboardMenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action to clear the scoreboard
 */
public class ClearScoreboardAction extends AbstractAction {
    private final DataBase data;
    private final ScoreboardMenuPanel panel;

    /**
     * Constructor of the action
     * @param panel the panel to be repainted
     * @param data the database containing the entries to be deleted
     */
    public ClearScoreboardAction(ScoreboardMenuPanel panel, DataBase data) {
        super("Clear Scoreboard");
        this.data = data;
        this.panel = panel;
    }

    /**
     * Invoked when an action occurs. Will delete the entries of the table and repaint the panel
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.data.deleteScores();
        panel.repaint();
    }
}
