package aoop.asteroids.model.Networking;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Class that handles the operations on datagram packets
 */
public abstract class PacketHandler extends Thread {

    private final int MAX_SIZE = 4096;

    /**
     * Send a packet
     * @param ds datagram socket that is going to send the packet
     * @param data the data to be send
     * @param connectionDetails the connection details where the packet will be send to
     */
    public void send(DatagramSocket ds, byte[] data, ConnectionDetails connectionDetails) {

        try {
            ds.send(new DatagramPacket(data, data.length, connectionDetails.getIpAddress(), connectionDetails.getPort()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error in sending data");
        }
    }

    /**
     * Receive a packet
     * @param ds datagram socket that receives the packet
     * @return the received datagram packet
     */
    public DatagramPacket receive(DatagramSocket ds) {
        byte[] data = new byte[MAX_SIZE];

        DatagramPacket receivePacket = new DatagramPacket(data, data.length);

        try {
            ds.receive(receivePacket);
            return receivePacket;
        } catch (IOException e) {
            return null;
        }
    }
}
