package aoop.asteroids.model.Networking;

import aoop.asteroids.model.Asteroid;
import aoop.asteroids.model.Bullet;
import aoop.asteroids.model.Spaceship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class for the packet that contains the data that is going to be transferred
 */
public class Packet implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * The type of the collection being transferred
     * 0: Asteroids Collection
     * 1: Bullets Collection
     * 2: Spaceship Collection
     * 3: All of the above
     * 4: Game Over
     * 5: isSpectator
     */
    private final int type;
    private final int index;
    private final Collection<Asteroid> asteroids;
    private final Collection<Bullet> bullets;
    private final ArrayList<Spaceship> spaceships;

    /**
     * Creates a new packet
     * @param type the type of the data
     * @param index index of a spaceship
     * @param asteroids a Collection of asteroids
     * @param bullets a Collection of bullets
     * @param spaceships an ArrayList of spaceships
     */
    public Packet(int type, int index, Collection<Asteroid> asteroids, Collection<Bullet> bullets, ArrayList<Spaceship> spaceships) {
        this.type = type;
        this.index = index;
        this.asteroids = asteroids;
        this.bullets = bullets;
        this.spaceships = spaceships;
    }

    /**
     * Get the type of data being transferred
     * @return the type of the data
     */
    public int getType() {
        return type;
    }

    /**
     * Get the collection of Asteroids
     * @return the collection of asteroids
     */
    public Collection<Asteroid> getAsteroids() {
        return asteroids;
    }

    /**
     * Get the collection of bullets
     * @return the collection of bullets
     */
    public Collection<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Get the collection of spaceships
     * @return the collection of spaceships
     */
    public ArrayList<Spaceship> getSpaceships() {
        return spaceships;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }
}
