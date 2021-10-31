package aoop.asteroids.model.Networking;

import java.net.InetAddress;

/**
 * Class for the ConnectionDetails
 */
public class ConnectionDetails {
    private final InetAddress ipAddress;
    private final int port;

    /**
     * Constructor for the connection details, consisting of the ip and port combination
     * @param ipAddress the ip address of the connection
     * @param port the port number of the connection
     */
    public ConnectionDetails(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * Get the ip address of the connection details
     * @return the ip address
     */
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     * Get the port of the connection details
     * @return the port
     */
    public int getPort() {
        return port;
    }
}
