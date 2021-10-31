package aoop.asteroids.model.Networking;

import aoop.asteroids.model.Game;
import aoop.asteroids.view.AsteroidsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests the capabilities of Updates to extract the data from the game correctly
 */
public class UpdatesTest {

    /**
     * Tests if the retrieved data is new and correct
     */
    @Test
    public void testUpdates() {
        AsteroidsFrame frame = new AsteroidsFrame();
        Game game = frame.getGame();
        game.initializeGameData();
        game.start();

        Updates updates = new Updates(frame);

        assertEquals(0, game.getBullets().size());
        assertEquals(0, updates.getBullets().size());


        game.getSpaceship().setIsFiring(true);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.err.println("sleep interrupted");
        }

        assertNotEquals(0, game.getBullets().size());
        assertNotEquals(0, updates.getBullets().size());
        assertEquals(game.getBullets(),updates.getBullets());
    }
}
