package aoop.asteroids.model.Networking;

import aoop.asteroids.model.Asteroid;
import aoop.asteroids.model.Bullet;
import aoop.asteroids.model.Game;
import aoop.asteroids.model.Spaceship;
import aoop.asteroids.view.AsteroidsFrame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class containing updates from the game to be send to the players
 */
public class Updates extends Thread implements PropertyChangeListener {

    private boolean running;
    private boolean fired;
    private final Game game;
    private final AsteroidsFrame frame;
    private final PropertyChangeSupport propertyChangeSupport;
    private PropertyChangeEvent evt;
    private Collection<Asteroid> asteroids;
    private Collection<Bullet> bullets;
    private ArrayList<Spaceship> players;

    /**
     * Constructor of the class, sets the parameters matching to those of the game and adds a PropertyChangeSupport and a listener
     * @param frame the frame containing the game
     */
    public Updates(AsteroidsFrame frame) {
        this.fired = false;
        this.running = false;

        this.game = frame.getGame();
        this.frame = frame;
        this.asteroids = game.getAsteroids();
        this.bullets = game.getBullets();
        this.players = game.getPlayers();

        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.game.getGameUpdater().addListener(this);
    }

    /**
     * Method that gets the updates from the game when something changes
     */
    @Override
    public void run() {
        running = true;

        while (running) {
            if (fired) {
                update(evt);
                fired = false;
            }
        }
    }

    /**
     * Method that gets notified when a change in the game has been made.
     * Makes the run method get the changes from game and notifies the threads connected.
     * @param evt the fired event
     */
    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        this.evt = evt;
        this.fired = true;
        propertyChangeSupport.firePropertyChange(evt);
    }

    /**
     * Gets the updates from the game according to the event that has been fired
     * @param evt the fired event
     */
    private synchronized void update(PropertyChangeEvent evt) {
        switch ((int) evt.getNewValue()) {
            case 0: this.asteroids = game.getAsteroids();
            break;
            case 1: this.bullets = game.getBullets();
            break;
            case 2: this.players = game.getPlayers();
            break;
            default: {
                this.asteroids = game.getAsteroids();
                this.bullets = game.getBullets();
                this.players = game.getPlayers();
            }
        }
    }

    /**
     * @return the list of players
     */
    public synchronized ArrayList<Spaceship> getPlayers() {
        return players;
    }

    /**
     * @return the collection of bullets
     */
    public synchronized Collection<Bullet> getBullets() {
        return bullets;
    }

    /**
     * @return the collection of asteroids
     */
    public synchronized Collection<Asteroid> getAsteroids() {
        return asteroids;
    }

    /**
     * Adds a property change listener
     * @param listener the listener to be added
     */
    public synchronized void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
}
