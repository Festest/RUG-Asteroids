package aoop.asteroids.control;

import aoop.asteroids.model.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A runnable object which, when started in a thread, runs the main game loop and periodically updates the game's model
 * as time goes on. This class can be thought of as the 'Game Engine', because it is solely responsible for all changes
 * to the game model as a result of user input, and this class also defines the very important game loop itself.
 */
public class GameUpdater implements Runnable {
	/**
	 * The database where we insert the scores after the game ends
	 */
	private final DataBase data;

	/**
	 * The spaceship whose score is to be added to the database
	 */
	private final Spaceship spaceship;

	/**
	 * The refresh rate of the display, in frames per second. Increasing this number makes the game look smoother, up to
	 * a certain point where it's no longer noticeable.
	 */
	private static final int DISPLAY_FPS = 120;

	/**
	 * The rate at which the game ticks (how often physics updates are applied), in frames per second. Increasing this
	 * number speeds up everything in the game. Ships react faster to input, bullets fly faster, etc.
	 */
	private static final int PHYSICS_FPS = 30;

	/**
	 * The number of milliseconds in a game tick.
	 */
	public static final double MILLISECONDS_PER_TICK = 1000.0 / PHYSICS_FPS;

	/**
	 * The default maximum number of asteroids that may be present in the game when starting.
	 */
	private static final int ASTEROIDS_LIMIT_DEFAULT = 7;

	/**
	 * Set this to true to allow asteroids to collide with each other, potentially causing chain reactions of asteroid
	 * collisions.
	 */
	private static final boolean KESSLER_SYNDROME = false;

	/**
	 * The game that this updater works for.
	 */
	private final Game game;

	/**
	 * Counts the number of times the game has updated.
	 */
	private int updateCounter;

	/**
	 * The limit to the number of asteroids that may be present. If the current number of asteroids exceeds this amount,
	 * no new asteroids will spawn.
	 */
	private int asteroidsLimit;

	private final PropertyChangeSupport propertyChangeSupport;

	/**
	 * Constructs a new game updater with the given game.
	 *
	 * @param game The game that this updater will update when it's running.
	 */
	public GameUpdater(Game game, DataBase data, Spaceship spaceship) {
		this.game = game;
		this.data = data;
		this.spaceship = spaceship;
		this.updateCounter = 0;
		this.asteroidsLimit = ASTEROIDS_LIMIT_DEFAULT;
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	/**
	 * The main game loop.
	 *
	 * Starts the game updater thread. This will run until the quit() method is called on this updater's game object.
	 */
	@Override
	public void run() {
		long previousTime = System.currentTimeMillis();
		long timeSinceLastTick = 0L;
		long timeSinceLastDisplayFrame = 0L;

		final double millisecondsPerDisplayFrame = 1000.0 / DISPLAY_FPS;

		while (this.game.isRunning() && !this.game.isGameOver()) {
			long currentTime = System.currentTimeMillis();
			long elapsedTime = currentTime - previousTime;
			timeSinceLastTick += elapsedTime;
			timeSinceLastDisplayFrame += elapsedTime;

			if (timeSinceLastTick >= MILLISECONDS_PER_TICK) { // Check if enough time has passed to update the physics.
				this.updatePhysics(); // Perform one 'step' in the game.
				timeSinceLastTick = 0L;
			}
			if (timeSinceLastDisplayFrame >= millisecondsPerDisplayFrame) { // Check if enough time has passed to refresh the display.
				this.game.notifyListeners(timeSinceLastTick); // Tell the asteroids panel that it should refresh.
				timeSinceLastDisplayFrame = 0L;
			}

			previousTime = currentTime;
		}

		PropertyChangeEvent listUpdateEvent = new PropertyChangeEvent(this, "list(s) updated", 0, 4);
		propertyChangeSupport.firePropertyChange(listUpdateEvent);

		// When the SP game ends, add the score to the DB
		if (this.game.isSingleplayer()) this.data.insert(spaceship.getUsername(), spaceship.getScore());
	}

	/**
	 * Called every game tick, to update all of the game's model objects.
	 *
	 * First, each object's movement is updated by calling nextStep() on it.
	 * Then, if the player is pressing the key to fire the ship's weapon, a new bullet should spawn.
	 * Then, once all objects' positions are updated, we check for any collisions between them.
	 * And finally, any objects which are destroyed by collisions are removed from the game.
	 *
	 * Also, every 200 game ticks, if possible, a new random asteroid is added to the game.
	 */
	private void updatePhysics()
	{
		Collection<Spaceship> shipList = this.game.getPlayers();
		Collection<Bullet> bullets = this.game.getBullets();
		Collection<Asteroid> asteroids = this.game.getAsteroids();

		asteroids.forEach(GameObject::nextStep);
		bullets.forEach(GameObject::nextStep);

		for (Spaceship ship: shipList) {
			ship.nextStep();

			if (ship.canFireWeapon()) {
				double direction = ship.getDirection();
				bullets.add(
						new Bullet(
								ship.getLocation().getX(),
								ship.getLocation().getY(),
								ship.getVelocity().x + Math.sin(direction) * 15,
								ship.getVelocity().y - Math.cos (direction) * 15,
								ship
						)
				);
				ship.setFired();
				PropertyChangeEvent listUpdateEvent = new PropertyChangeEvent(this, "list(s) updated", 0, 1);
				propertyChangeSupport.firePropertyChange(listUpdateEvent);
			}
		}

		PropertyChangeEvent listUpdateEvent = new PropertyChangeEvent(this, "list(s) updated", 0, 2);
		propertyChangeSupport.firePropertyChange(listUpdateEvent);

		this.checkCollisions();
		this.removeDestroyedObjects();

		// Every 200 game ticks, try and spawn a new asteroid.
		if (this.updateCounter % 200 == 0 && asteroids.size() < this.asteroidsLimit && this.game.isHosting()) {
			this.addRandomAsteroid();
		}
		this.updateCounter++;
	}

	/**
	 * Adds a random asteroid at least 50 pixels away from the player's spaceship.
	 */
	private void addRandomAsteroid() {
		ThreadLocalRandom rng = ThreadLocalRandom.current();
		Point.Double newAsteroidLocation;
		Point.Double shipLocation = this.game.getSpaceship().getLocation();
		double distanceX, distanceY;
		do { // Iterate until a point is found that is far enough away from the player.
			newAsteroidLocation = new Point.Double(rng.nextDouble(0.0, 800.0), rng.nextDouble(0.0, 800.0));
			distanceX = newAsteroidLocation.x - shipLocation.x;
			distanceY = newAsteroidLocation.y - shipLocation.y;
		} while (distanceX * distanceX + distanceY * distanceY < 50 * 50); // Pythagorean theorem for distance between two points.

		double randomChance = rng.nextDouble();
		Point.Double randomVelocity = new Point.Double(rng.nextDouble() * 6 - 3, rng.nextDouble() * 6 - 3);
		AsteroidSize randomSize;
		if (randomChance < 0.333) { // 33% chance of spawning a large asteroid.
			randomSize = AsteroidSize.LARGE;
		} else if (randomChance < 0.666) { // 33% chance of spawning a medium asteroid.
			randomSize = AsteroidSize.MEDIUM;
		} else { // And finally a 33% chance of spawning a small asteroid.
			randomSize = AsteroidSize.SMALL;
		}
		this.game.getAsteroids().add(new Asteroid(newAsteroidLocation, randomVelocity, randomSize));

		PropertyChangeEvent listUpdateEvent = new PropertyChangeEvent(this, "list(s) updated", 0, 0);
		propertyChangeSupport.firePropertyChange(listUpdateEvent);
	}

	/**
	 * Checks all objects for collisions and marks them as destroyed upon collision. All objects can collide with
	 * objects of a different type, but not with objects of the same type. I.e. bullets cannot collide with bullets etc.
	 */
	private void checkCollisions() {
		// First check collisions between bullets and other objects.
		this.game.getBullets().forEach(bullet -> {
			this.game.getAsteroids().forEach(asteroid -> { // Check collision with any of the asteroids.
				if (asteroid.collides(bullet)) {
					asteroid.destroy();
					bullet.destroy();
					increaseScore(bullet.getSpaceship());
				}
			});
			this.game.getPlayers().forEach(spaceship -> { // Check collision with any of the spaceships.
				if (spaceship.collides(bullet)) {
					spaceship.destroy();
					bullet.destroy();
				}
			});
		});
		// Next check for collisions between asteroids and the spaceship.
		this.game.getAsteroids().forEach(asteroid -> {
			this.game.getPlayers().forEach(spaceship -> { // Check collision with any of the spaceships.
				if (spaceship.collides(asteroid)) {
					spaceship.destroy();
					asteroid.destroy();
				}
			});
			if (KESSLER_SYNDROME) { // Only check for asteroid - asteroid collisions if we allow kessler syndrome.
				this.game.getAsteroids().forEach(secondAsteroid -> {
					if (!asteroid.equals(secondAsteroid) && asteroid.collides(secondAsteroid)) {
						asteroid.destroy();
						secondAsteroid.destroy();
					}
				});
			}
		});
		// Check collisions between spaceships.
		this.game.getPlayers().forEach(spaceship1 -> { // Check collision with any of the spaceships.
			this.game.getPlayers().forEach(spaceship2 -> { // Check collision with any of the spaceships.
				if (!spaceship1.equals(spaceship2) && spaceship1.collides(spaceship2) && (!spaceship1.isDestroyed() && !spaceship2.isDestroyed())) {
					spaceship1.destroy();
					spaceship2.destroy();
				}
			});
		});
	}

	/**
	 * Increment the player's score, and for every five score points, the asteroids limit is incremented.
	 */
	private synchronized void increaseScore(Spaceship spaceship) {
		spaceship.increaseScore();

		if (this.game.getSpaceship().getScore() % 5 == 0) {
			this.asteroidsLimit++;
		}
	}

	/**
	 * Removes all destroyed objects (those which have collided with another object).
	 *
	 * When an asteroid is destroyed, it may spawn some smaller successor asteroids, and these are added to the game's
	 * list of asteroids.
	 */
	private synchronized void removeDestroyedObjects ()
	{
		Collection<Asteroid> newAsteroids = new ArrayList<>(this.game.getAsteroids().size() * 2); // Avoid reallocation and assume every asteroid spawns successors.
		this.game.getAsteroids().forEach(asteroid -> {
			if (asteroid.isDestroyed()) {
				newAsteroids.addAll(asteroid.getSuccessors());
			}
		});
		this.game.getAsteroids().addAll(newAsteroids);
		// Remove all asteroids that are destroyed.
		this.game.getAsteroids().removeIf(GameObject::isDestroyed);
		// Remove any bullets that are destroyed.
		this.game.getBullets().removeIf(GameObject::isDestroyed);

		PropertyChangeEvent listUpdateEvent = new PropertyChangeEvent(this, "list(s) updated", 0, 3);
		propertyChangeSupport.firePropertyChange(listUpdateEvent);
	}

	/**
	 * Adds a property change listener
	 * @param listener the listener to be added
	 */
	public synchronized void addListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
}
