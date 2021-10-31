package aoop.asteroids.view.menu;

import aoop.asteroids.model.Game;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.buttons.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class that takes care of painting the Main Menu Panel
 */
public class MainMenuPanel extends MenuPanel {

    private final JLabel usernameLabel;
    private final JFormattedTextField username;
    private final NewGameButton newGameButton;
    private final MultiplayerButton multiplayerButton;
    private final ScoreboardButton scoreboardButton;
    private final QuitGameButton forceCloseButton;
    private final QuitSPGameButton quitSPGameButton;
    private int page;
    private final int BUTTON_X = 340;
    private final int BUTTON_WIDTH = 150;
    private final int BUTTON_HEIGHT = 35;

    /**
     * Constructor for the Main Menu Panel, creates the needed buttons and sets up the JLabel and JTextfield
     * @param game
     * @param frame
     */
    public MainMenuPanel(Game game, AsteroidsFrame frame) {
        this.usernameLabel = new JLabel("<html><font color='white'>Username</font></html>");
        this.username = setUsernameField();
        this.newGameButton = new NewGameButton(game,frame, this, username);
        this.multiplayerButton = new MultiplayerButton(frame);
        this.scoreboardButton = new ScoreboardButton(frame);
        this.forceCloseButton = new QuitGameButton();
        this.quitSPGameButton = new QuitSPGameButton(game, this);

        this.usernameLabel.setHorizontalAlignment(JLabel.CENTER);
        this.username.setText("Player 1");
        this.username.setHorizontalAlignment(JTextField.CENTER);
        setPage(0);
    }

    /**
     * Will create a new JTextField with a keylistener, makes it so that the username cannot contain more than 10 characters
     * @return the created JTextField
     */
    private JFormattedTextField setUsernameField() {
        JFormattedTextField username = new JFormattedTextField();
        username.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (username.getText().length() >= 10 ) // limit to 10 characters
                    e.consume();
            }
        });
        return username;
    }

    /**
     * Depending on the int given, decide which buttons have to be shown.
     * Case 0: Main page with the options to start a new game, go to multiplayer or view the scoreboard. Also contains a field to put a username.
     * Case 1: Case where someone is in a singleplayer game, has the button to quit that game
     * @param page The int that decides which buttons have to be added
     */
    @Override
    public void setPage(int page) {
        this.page = page;

        switch (this.page) {
            case 0: {
                this.removeAll();
                this.add(usernameLabel);
                this.add(username);
                this.add(newGameButton);
                this.add(multiplayerButton);
                this.add(scoreboardButton);
                this.add(forceCloseButton);
            }
            break;
            case 1: {
                this.removeAll();
                this.add(quitSPGameButton);
            }
        }
    }

    /**
     * Paint the frame by adding the background and setting the bounds for the buttons
     * @param graphics Graphics used for drawing
     */
    @Override
    public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

        try {
            BufferedImage background = ImageIO.read(new File("./src/main/java/aoop/asteroids/view/menu/files/background.jpg"));
            graphics.drawImage(background,0,0,null);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Background image was not found");
        }

        setButtonBounds();

    }

    /**
     * Set all the bounds for the buttons
     */
    private void setButtonBounds() {
        switch (this.page) {
            case 0: {
                usernameLabel.setBounds(BUTTON_X,320,BUTTON_WIDTH,BUTTON_HEIGHT);
                username.setBounds(BUTTON_X,350,BUTTON_WIDTH,BUTTON_HEIGHT);
                newGameButton.setBounds(BUTTON_X,400,BUTTON_WIDTH,BUTTON_HEIGHT);
                multiplayerButton.setBounds(BUTTON_X,450,BUTTON_WIDTH,BUTTON_HEIGHT);
                scoreboardButton.setBounds(BUTTON_X,500,BUTTON_WIDTH,BUTTON_HEIGHT);
                forceCloseButton.setBounds(BUTTON_X,550,BUTTON_WIDTH,BUTTON_HEIGHT);
            }
            break;
            case 1: {
                quitSPGameButton.setBounds(BUTTON_X,350,BUTTON_WIDTH,BUTTON_HEIGHT);
            }
        }
    }
}
