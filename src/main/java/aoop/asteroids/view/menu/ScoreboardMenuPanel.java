package aoop.asteroids.view.menu;

import aoop.asteroids.model.DataBase;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.buttons.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that takes care of painting the Scoreboard Panel
 */
public class ScoreboardMenuPanel extends MenuPanel {
    private final ClearScoreboardButton clearScoreboardButton;
    private final GoBackButton goBackButton;
    private final DataBase data;
    private final int BUTTON_X = 340;
    private final int BUTTON_WIDTH = 150;
    private final int BUTTON_HEIGHT = 35;

    /**
     * Constructor for the scoreboard menu panel, creates a new button to clear the scoreboard and go back to previous menu and adds these to the panel
     * @param frame The AsteroidsFrame to send to the GoBackButton
     * @param data The database used to select the top 10 from
     */
    public ScoreboardMenuPanel(AsteroidsFrame frame, DataBase data) {
        this.clearScoreboardButton = new ClearScoreboardButton(this, data);
        this.goBackButton = new GoBackButton(frame);
        this.data = data;
        this.add(clearScoreboardButton);
        this.add(goBackButton);
    }

    /**
     * Override method to paint the panel. First the background and title are put, then the scoreboard itself is drawn, lastly the entries of the database are drawn on the scoreboard. Also sets the bounds for the buttons
     * @param graphics graphics used for drawing
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        putImage(graphics);
        drawSB(graphics);
        printSB(graphics);

        clearScoreboardButton.setBounds(BUTTON_X,450,BUTTON_WIDTH,BUTTON_HEIGHT);
        goBackButton.setBounds(BUTTON_X,500,BUTTON_WIDTH,BUTTON_HEIGHT);
    }

    /**
     * Draw the scoreboard by creating a 50% transparent rectangle and putting an image over it containing the player name and score title and a dividing line in the middle
     * @param graphics The graphics used for drawing
     */
    private void drawSB(Graphics graphics) {
        int alpha = 127; // 50% transparent
        Color myColour = new Color(105, 105, 105, alpha);
        graphics.setColor(myColour);
        graphics.fillRect(310,175,200,250);
        graphics.setColor(Color.WHITE);

        try {
            BufferedImage background = ImageIO.read(new File("./src/main/java/aoop/asteroids/view/menu/files/sb2.png"));
            graphics.drawImage(background,310,175,null);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Image was not found");
        }
    }

    /**
     * Retrieve the ResultSet containing the top 10 entries of the database and print those on the screen
     * @param graphics the graphics used for painting
     */

    private void printSB(Graphics graphics) {
        ResultSet topTen = data.selectTop();
        int i = 1;
        try {
            while (topTen.next()) {
                String p_name = topTen.getString("player_name");
                int score = topTen.getInt("score");
                graphics.drawString(i + ":     " + p_name, 315, (200 + i * 20));
                graphics.drawString(String.valueOf(score), 450, (200 + i * 20));
                i++;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to put an image on the panel, both the code for the background and the scoreboard title are here
     * @param graphics the graphics used for painting
     */
    private void putImage(Graphics graphics) {
        // Background
        try {
            BufferedImage background = ImageIO.read(new File("./src/main/java/aoop/asteroids/view/menu/files/background.jpg"));
            graphics.drawImage(background,0,0,null);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Background image was not found");
        }
        // Scoreboard
        try {
            BufferedImage background = ImageIO.read(new File("./src/main/java/aoop/asteroids/view/menu/files/sb.png"));
            graphics.drawImage(background,50,20,null);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Image was not found");
        }
    }
}
