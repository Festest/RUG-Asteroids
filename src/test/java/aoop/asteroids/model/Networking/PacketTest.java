package aoop.asteroids.model.Networking;


import aoop.asteroids.model.Asteroid;
import aoop.asteroids.model.Bullet;
import aoop.asteroids.model.Spaceship;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the correctness of the packet creation and value retrieval
 */
public class PacketTest {
    /**
     * Makes sure that the data is maintained and that it can be retrieved from the packet correctly
     */
    @Test
    public void testPacket() {
        Collection<Bullet> bullets = new ArrayList<>();
        Collection<Asteroid> asteroids = new ArrayList<>();
        ArrayList<Spaceship> shipList = new ArrayList<>();
        Packet packet = new Packet(1,0,asteroids,bullets,shipList);

        assertEquals(1, packet.getType());
        assertEquals(0, packet.getIndex());
        assertEquals(0, packet.getAsteroids().size());
        assertEquals(0, packet.getBullets().size());
        assertEquals(0, packet.getSpaceships().size());

        Spaceship ship = new Spaceship();
        Bullet bullet = new Bullet(1,1,1,1,ship);
        shipList.add(ship);
        bullets.add(bullet);

        packet = new Packet(2,1,asteroids, bullets,shipList);

        assertEquals(2, packet.getType());
        assertEquals(1, packet.getIndex());
        assertEquals(0, packet.getAsteroids().size());
        assertEquals(1, packet.getBullets().size());
        assertEquals(ship, packet.getSpaceships().get(0));
    }
}
