package aoop.asteroids.view.menu;

import javax.swing.*;

/**
 * Class that the other MenuPanels extend from, so they will extend JPanel and have the method to set their page
 */
public abstract class MenuPanel extends JPanel {

    /**
     * Adds the components to the frame, depending on the int given
     * @param page the int deciding what is shown on the frame
     */
    public void setPage(int page) {}
}
