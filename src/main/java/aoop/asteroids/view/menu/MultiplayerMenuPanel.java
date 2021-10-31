package aoop.asteroids.view.menu;

import aoop.asteroids.model.Networking.Server.Server;
import aoop.asteroids.view.AsteroidsFrame;
import aoop.asteroids.view.menu.buttons.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class that takes care of painting the Multiplayer Panel
 */
public class MultiplayerMenuPanel extends MenuPanel {
    private final JoinGameButton joinGameButton;
    private final JLabel IPLabel;
    private final SpectateGameButton spectateGameButton;
    private final JFormattedTextField textFieldIP;
    private final HostGameButton hostGameButton;
    private final GoBackButton goBackButton;
    private final StopHostingButton stopHostingButton;
    private final QuitMPGameButton quitMPGameButton;
    private final AsteroidsFrame frame;
    private int page;
    private Server server;
    private final int BUTTON_X = 340;
    private final int BUTTON_WIDTH = 150;
    private final int BUTTON_HEIGHT = 35;

    /**
     * Constructor for the Multiplayer Menu Panel. Creates the necessary buttons and takes care of the setup for the JLabel and Textfield
     * @param frame AsteroidsFrame used for the buttons
     */
    public MultiplayerMenuPanel(AsteroidsFrame frame) {
        this.frame = frame;
        setNewServer();
        this.IPLabel = new JLabel("<html><font color='white'>IP and Port Number</font></html>");
        this.textFieldIP = new JFormattedTextField();
        this.joinGameButton = new JoinGameButton(this, frame, textFieldIP);
        this.spectateGameButton = new SpectateGameButton(this, frame, textFieldIP);
        this.hostGameButton = new HostGameButton(this, frame);
        this.goBackButton = new GoBackButton(frame);
        this.stopHostingButton = new StopHostingButton(this, frame);
        this.quitMPGameButton = new QuitMPGameButton(this, frame);

        this.IPLabel.setHorizontalAlignment(JLabel.CENTER);
        this.textFieldIP.setText("localhost:");
        this.textFieldIP.setHorizontalAlignment(JTextField.CENTER);
        setPage(0);
    }

    /**
     * Depending on the int given, decide which buttons have to be shown.
     * Case 0: Main page with the options to Host, Join or Spectate a game.
     * Case 1: Case where someone has joined or spectated a MP game, this page contains the button to quit this game
     * Case 2: Case where someone is hosting a game, options to quit the game or stop hosting
     * @param page The int that decides which buttons have to be added
     */
    public void setPage(int page) {
        this.page = page;

        switch (this.page) {
            case 0: {
                this.removeAll();
                this.add(joinGameButton);
                this.add(IPLabel);
                this.add(spectateGameButton);
                this.add(textFieldIP);
                this.add(hostGameButton);
                this.add(goBackButton);
            }
            break;
            case 1: {
                this.removeAll();
                this.add(quitMPGameButton);
            }
            break;
            case 2: {
                this.removeAll();
                this.add(quitMPGameButton);
                this.add(stopHostingButton);
            }
        }
    }

    /**
     * Paint the panel by adding the background and setting the bounds for the buttons
     * @param graphics Graphics used for drawing
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        try {
            BufferedImage background = ImageIO.read(new File("./src/main/java/aoop/asteroids/view/menu/files/background.jpg"));
            graphics.drawImage(background, 0, 0, null);
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
                joinGameButton.setBounds(255, 350, 150, 35);
                IPLabel.setBounds(435, 350, 150, 35);
                spectateGameButton.setBounds(255, 400, 150, 35);
                textFieldIP.setBounds(435, 377, 150, 30);
                hostGameButton.setBounds(BUTTON_X,450,BUTTON_WIDTH,BUTTON_HEIGHT);
                goBackButton.setBounds(BUTTON_X,500,BUTTON_WIDTH,BUTTON_HEIGHT);
            }
            break;
            case 1: {
                quitMPGameButton.setBounds(BUTTON_X,350,BUTTON_WIDTH,BUTTON_HEIGHT);
            }
            break;
            case 2: {
                quitMPGameButton.setBounds(BUTTON_X,350,BUTTON_WIDTH,BUTTON_HEIGHT);
                stopHostingButton.setBounds(BUTTON_X,400,BUTTON_WIDTH,BUTTON_HEIGHT);
            }
            break;
        }
    }

    /**
     * Start a new server with the current frame as argument
     */
    public void setNewServer() {
        this.server = new Server(this.frame);
    }

    /**
     * @return the server of the class
     */
    public Server getServer() {
        return server;
    }
}
