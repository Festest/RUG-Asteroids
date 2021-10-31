package aoop.asteroids.model.Networking.Server;

import aoop.asteroids.model.Game;
import aoop.asteroids.model.Networking.ConnectionDetails;
import aoop.asteroids.model.Networking.Updates;
import aoop.asteroids.model.Networking.Packet;
import aoop.asteroids.model.Networking.PacketHandler;
import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Class for the Thread Handler
 */
public class ThreadHandler extends PacketHandler implements Runnable, PropertyChangeListener {

    private final int threadNr;
    private DatagramSocket ds;
    private final AsteroidsFrame frame;
    private final ConnectionDetails connectionDetails;
    private PropertyChangeEvent evt;
    private final Updates updates;
    private boolean running;
    private boolean fired;
    private final int spaceshipIndex;

    /**
     * Constructor for the thread handler, creates a datagram socket and adds a propertychangelistener to the updates field
     * @param threadNr number of the thread
     * @param connectionDetails connectionDetails of the connection
     * @param frame the AsteroidsFrame
     * @param updates the thread that contains the updates from the game
     */
    public ThreadHandler(int threadNr, ConnectionDetails connectionDetails, AsteroidsFrame frame, Updates updates, int spaceshipIndex) {
        this.spaceshipIndex = spaceshipIndex;
        this.connectionDetails = connectionDetails;
        this.updates = updates;
        this.fired = false;

        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Socket Error");
        }

        this.threadNr = threadNr;
        this.frame = frame;
        this.running = false;
        updates.addListener(this);
    }

    /**
     * Sends the game details to the client and receives the keystrokes from the client
     */
    @Override
    public  void run() {
        boolean first = true;
        running = true;
        while (running) {

            if (first) {
                sendGameDetails();
                first = false;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Sleep of thread was interrupted");
            }

            if (fired) {
                createPacket();
                this.fired = false;
            }
        }
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
            setDetails(packet);
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

    /**
     * Set the details from the data received
     * @param packet the packet to get data from
     */
    private void setDetails(Packet packet) {
        Game game = frame.getGame();

        if (packet.getType() == 5) {
            frame.getGame().getPlayers().get(spaceshipIndex).destroy();
        } else if (packet.getType() == 3) {
            game.getPlayers().remove(spaceshipIndex);
            game.getPlayers().add(spaceshipIndex, packet.getSpaceships().get(spaceshipIndex));
        }
    }

    /**
     * Sends all the current game details one time, so that the other player(s) have the information of the game at start
     */
    private void sendGameDetails() {
        try {
            byte[] data = convert(new Packet(3, spaceshipIndex, updates.getAsteroids(), updates.getBullets(), updates.getPlayers()));
            if (data != null) {
                send(ds, data, connectionDetails);
            } else {
                JOptionPane.showMessageDialog(null, "Unable to send initialisation data");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occured while sending the data");
        }
    }

    /**
     * Create a packet depending on the event and send this packet
     */
    private void createPacket() {
        Packet packet;
        switch ((int) evt.getNewValue()) {
            case 0: packet = new Packet(0, spaceshipIndex, updates.getAsteroids(), null, null);
                break;
            case 1: packet = new Packet(1, spaceshipIndex, null, updates.getBullets(), null);
                break;
            case 2: packet = new Packet(2, spaceshipIndex, null, null, updates.getPlayers());
                break;
            case 3: packet = new Packet(3, spaceshipIndex, updates.getAsteroids(), updates.getBullets(), updates.getPlayers());
                break;
            default: packet = new Packet(4, spaceshipIndex, null, null, null);
        }

        try {
            byte[] data = convert(packet);
            send(ds, data, connectionDetails);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occured while converting or sending the data");
        }
    }

    /**
     * Convert the packet in a byte[] to be send
     * @param packet the packet to be converted
     * @return the byte array of the data
     * @throws IOException Exception thrown when something goes wrong with the Streams
     */
    private byte[] convert(Packet packet) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(packet);
        out.flush();
        byte[] data = bos.toByteArray();

        bos.close();

        return data;
    }

    /**
     * Sets fired to true so the class knows an event has been fired, and sets the evt to the corresponding event
     * @param evt the event that was fired
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.evt = evt;
        this.fired = true;
    }
}
