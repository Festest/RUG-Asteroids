package aoop.asteroids.model.Networking;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for testing on the ConnectionDetails
 */
public class ConnectionDetailsTest {

    /**
     * Test the constructor by generating an ip and port, creating an instance of
     * ConnectionDetails with these variables and checking if they are set correctly.
     */
    @Test
    void testConstructor() {
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName("www.google.com");
        } catch (UnknownHostException e) {
            System.out.println("Host Unknown");
        }

        int port = 1234;

        if (ip != null) {
            ConnectionDetails connectionDetails = new ConnectionDetails(ip, port);
            assertEquals(ip, connectionDetails.getIpAddress());
            assertEquals(1234, connectionDetails.getPort());
        }
    }
}
