package aoop.asteroids.model.Networking.Server;

import aoop.asteroids.model.Networking.ConnectionDetails;
import aoop.asteroids.model.Networking.Packet;
import aoop.asteroids.model.Networking.PacketHandler;
import aoop.asteroids.model.Networking.Updates;
import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Class for the server
 */
public class Server extends PacketHandler {

    private int threadNr;
    private boolean running;
    private final AsteroidsFrame frame;
    private final ArrayList<Thread> threads;
    private int spaceshipIndex;

    /**
     * Constructor of the server, create new ArrayList for the threads and set running to false
     * @param frame the frame for the server
     */
    public Server(AsteroidsFrame frame) {
        this.threads = new ArrayList<>();
        this.threadNr = 1;
        this.running = false;
        this.frame = frame;
    }

    /**
     * Set the new ConnectionDetails when an initialisation packet is received
     * @param ds the socket of the server
     * @return returns the connection details
     */
    private ConnectionDetails connect(DatagramSocket ds) {
        DatagramPacket initial = receive(ds);

        assert initial != null;
        unpack(initial.getData());

        return new ConnectionDetails(initial.getAddress(), initial.getPort());
    }

    /**
     * Create a new DatagramSocket and then await connections. When an incoming connection is found, send an empty data packet back for initialisation. Then, a thread is started
     */
    @Override
    public void run() {
        try (DatagramSocket ds = new DatagramSocket(0)) {
            // This system.out is kept to see the port, so that it is possible to connect to the server
            System.out.println(ds.getLocalPort());
            JOptionPane.showMessageDialog(null, "Server Port: " + ds.getLocalPort());

            running = true;

            Updates updates = new Updates(frame);

            while (running) {
                // Package from client has been received
                ConnectionDetails connectionDetails = connect(ds);
                send(ds, new byte[0], connectionDetails);
                Thread t = new Thread(new ThreadHandler(threadNr, connectionDetails, frame, updates, spaceshipIndex));
                threads.add(t);
                t.start();
                threadNr++;
            }
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Server Socket Error");
        }
    }

    /**
     * Terminate the threads when the host stops the game
     */
    public void terminate() {
        for (Thread t: threads) t.stop();
        this.stop();
    }

    /**
     * Send the spaceship data
     * @param data the data to send
     */
    private void unpack(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Packet packet = (Packet) in.readObject();

            if (packet.getType() == 5) this.spaceshipIndex = -1;
            else this.spaceshipIndex = frame.getGame().addPlayer();

            in.close();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error in reading object");
        } finally {
            try  {
                in.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not close InputStream");
            }
        }
    }
}