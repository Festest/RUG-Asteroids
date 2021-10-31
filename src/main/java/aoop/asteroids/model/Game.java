package aoop.asteroids.model;

import aoop.asteroids.control.GameUpdater;
import aoop.asteroids.game_observer.ObservableGame;
import aoop.asteroids.view.AsteroidsFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is the main model for the Asteroids game. It contains all game objects, and has methods to start and stop
 * the game.
 *
 * This is strictly a model class, containing only the state of the game. Updates to the game are done in
 * {@link GameUpdater}, which runs in its own thread, and manages the main game loop and physics updates.
 */
public class Game extends ObservableGame {

	/**
	 * The database containing the scores
	 */
	private final DataBase data;

	/**
	 * True if the current game is a SP game
	 */
	private Boolean isSingleplayer;

	/**
	 * The spaceship object that the player is in control of.
	 */
	private Spaceship hostShip;

	/**
	 * List of colors for MP spaceships
	 */
	private final ArrayList<Color> colors;

	/**
	 * The spaceship to be used by the second player.
	 */
	private ArrayList<Spaceship> shipList;

	/**
	 * The list of all bullets currently active in the game.
	 */
	private Collection<Bullet> bullets;

	/**
	 * The list of all asteroids in the game.
	 */
	private Collection<Asteroid> asteroids;

	/**
	 * Indicates whether or not the game is running. Setting this to false causes the game to exit its loop and quit.
	 */
	private volatile boolean running = false;

	/**
	 * The game updater thread, which is responsible for updating the game's state as time goes on.
	 */
	private Thread gameUpdaterThread;

	/**
	 * Boolean to check if the current instance is hosting or not
	 */
	private boolean isHosting;

	/**
	 * The total number of players
	 */
	private int playerCount;

	private GameUpdater gameUpdater;

	/**
	 * Constructs a new game, with a new spaceship and all other model data in its default starting state.
	 */
	public Game(DataBase data) {
		this.data = data;
		this.isSingleplayer = true;
		this.isHosting = true;
		this.playerCount = 1;

		this.colors = new ArrayList<>();
		colors.add(Color.LIGHT_GRAY);
		colors.add(Color.RED);
		colors.add(Color.GREEN);
		colors.add(Color.BLUE);

		this.initializeGameData();
	}

	/**
	 * Initializes all of the model objects used by the game. Can also be used to reset the game's state back to a
	 * default starting state before beginning a new game.
	 */
	public void initializeGameData() {
		this.bullets = new ArrayList<>();
		this.asteroids = new ArrayList<>();
		this.shipList = new ArrayList<>();
		this.hostShip = new Spaceship();
		shipList.add(hostShip);
		this.hostShip.reset();

		if (!isSingleplayer) {
			hostShip.setLocation(AsteroidsFrame.WINDOW_SIZE.width / 4,AsteroidsFrame.WINDOW_SIZE.height / 2);
			hostShip.setUsername("Player 1");
			hostShip.setSpaceshipColor(colors.get(0));
		}
	}

	/**
	 * @return The game's spaceship.
	 */
	public synchronized Spaceship getSpaceship() {
		return this.hostShip;
	}

	/**
	 * @return The collection of asteroids in the game.
	 */
	public synchronized Collection<Asteroid> getAsteroids() {
		return this.asteroids;
	}

	/**
	 * @return The collection of bullets in the game.
	 */
	public synchronized Collection<Bullet> getBullets ()
	{
		return this.bullets;
	}

	/**
	 * @return The collection of spaceships in the game.
	 */
	public synchronized ArrayList<Spaceship> getPlayers() {
		return this.shipList;
	}

	/**
	 * @param playerCount to be set
	 */
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	/**
	 * @return Whether or not the game is running.
	 */
	public boolean isRunning() {
		return this.running;
	}

	/**
	 * @return True if the player's ship has been destroyed, or false otherwise.
	 */
	public boolean isGameOver() {
		if (isSingleplayer) return this.hostShip.isDestroyed();

		int destroyed = 0;
		for (Spaceship ship: shipList) {
			if (ship.isDestroyed()) destroyed++;
		}

		return  (destroyed == shipList.size());
	}

	/**
	 * Using this game's current model, spools up a new game updater thread to begin a game loop and start processing
	 * user input and physics updates. Only if the game isn't currently running, that is.
	 */
	public void start() {
		if (!this.running) {
			this.running = true;
			this.gameUpdater = new GameUpdater(this, data, hostShip);
			this.gameUpdaterThread = new Thread(gameUpdater);
			this.gameUpdaterThread.start();
		}
	}

	/**
	 * Tries to quit the game, if it is running.
	 */
	public void quit() {
		if (this.running) {
			try { // Attempt to wait for the game updater to exit its game loop.
				this.gameUpdaterThread.join(100);
			} catch (InterruptedException exception) {
				System.err.println("Interrupted while waiting for the game updater thread to finish execution.");
			} finally {
				this.running = false;
				this.gameUpdaterThread = null; // Throw away the game updater thread and let the GC remove it.
			}
		}
	}

	/**
	 * Update the spaceship with a username and color
	 * @param username the username for the ship
	 * @param spaceshipColor the color for the ship
	 */
	public void updateSpaceship(String username, Color spaceshipColor) {
		this.hostShip.setUsername(username);
		this.hostShip.setSpaceshipColor(spaceshipColor);
	}

	/**
	 * Get the gameupdate of the game
	 * @return the gameupdater of the game
	 */
	public GameUpdater getGameUpdater() {
		return gameUpdater;
	}

	/**
	 * Check if the game is a singleplayer game
	 * @return boolean value telling if the game is singleplayer
	 */
	public boolean isSingleplayer() {
		return this.isSingleplayer;
	}

	/**
	 * Set a boolean value to tell if the game is singleplayer
	 * @param isSingleplayer the boolean value
	 */
	public void setIsSingleplayer(boolean isSingleplayer) {
		this.isSingleplayer = isSingleplayer;
	}

	/**
	 * Add a player by adding a new spaceship to the list of spaceships
	 */
	public synchronized int addPlayer() {
		Spaceship spaceship = new Spaceship();
		this.shipList.add(spaceship);

		spaceship.reset();
		spaceship.setLocation(AsteroidsFrame.WINDOW_SIZE.width / 4 + (100 * playerCount),AsteroidsFrame.WINDOW_SIZE.height / 2);
		spaceship.setUsername("Player " + (playerCount + 1));
		if (playerCount < 4) {
			spaceship.setSpaceshipColor(colors.get(playerCount));
		}

		this.playerCount += 1;
		return this.shipList.size() - 1;
	}

	/**
	 * Set the shiplist of the game
	 * @param shipList the shiplist
	 */
	public void setShipList(ArrayList<Spaceship> shipList) {
		this.shipList = shipList;
	}

	/**
	 * Set the list of bullets of the game
	 * @param bullets bullet list
	 */
	public void setBullets(Collection<Bullet> bullets) {
		this.bullets = bullets;
	}

	/**
	 * Set the list of asteroids of the game
	 * @param asteroids lists of asteroids
	 */
	public void setAsteroids(Collection<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}

	/**
	 * See if the player is hosting or not
	 * @return boolean value telling whether the player is hosting
	 */
	public boolean isHosting() {
		return this.isHosting;
	}

	/**
	 * Set if the game is being hosted
	 * @param b boolean value telling if the game is being hosted
	 */
	public void setIsHosting(boolean b) {
		this.isHosting = b;
	}

	/**
	 * Defines which ship is from the host
	 * @param index the index of the ship of the host
	 */
	public void setHostShip(int index) {
		this.hostShip = shipList.get(index);
	}
}
