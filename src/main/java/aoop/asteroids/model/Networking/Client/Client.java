package aoop.asteroids.model.Networking.Client;

import aoop.asteroids.model.Game;
import aoop.asteroids.model.Networking.ConnectionDetails;
import aoop.asteroids.model.Networking.Packet;
import aoop.asteroids.model.Networking.PacketHandler;
import aoop.asteroids.view.AsteroidsFrame;

import javax.swing.*;
import java.io.*;
import java.net.*;

/**
 * Class for the client side
 */
public class Client extends PacketHandler {

    private ConnectionDetails connectionDetails;
    private final AsteroidsFrame frame;
    private final boolean isSpectating;
    private boolean running;
    private DatagramSocket ds;

    /**
     * Constructor for the client, creates a new Datagram Socket
     * @param ip ip of the server
     * @param port port of the server
     * @param frame frame containing the game
     * @param isSpectating boolean telling whether the client is a spectator or not
     */
    public Client(InetAddress ip, int port, AsteroidsFrame frame, boolean isSpectating) {
        this.connectionDetails = new ConnectionDetails(ip, port);
        this.frame = frame;
        this.isSpectating = isSpectating;
        this.running = false;

        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Could not create DatagramSocket");
        }
    }

    /**
     * Initalise the connection by sending an empty packet to the server, then update the connection details with the details of the server
     */
    public void initialiseConnection() {
        Packet packet;

        if (isSpectating) {
            packet = new Packet(5,-1,null,null,null);
        }
        else packet = new Packet(6,-1,null,null,null);


        try {
            byte[] data = convert(packet);
            send(ds, data, connectionDetails);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occured while converting or sending the data");
        }

        // To send packets back, update connectionDetails from received package
        DatagramPacket dp = receive(ds);
        this.connectionDetails = new ConnectionDetails(dp.getAddress(), dp.getPort());
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
     * First initialise the connection, then while running the game details will be received and some details will be send
     */
    @Override
    public void run() {
        initialiseConnection();

        running = true;
        while (running) {
            // Receive the Game & Send Key Strokes
            try {
                ds.setSoTimeout(5000);
                DatagramPacket dp = receive(ds);

                if (dp == null) {
                    JOptionPane.showMessageDialog(null, "Connection with server is lost. Quitting Game...");
                    this.frame.getGame().quit();
                    this.frame.setMenuPage(1);
                    this.frame.setTab(0);
                    this.running = false;
                    this.interrupt();
                    return;
                }

                byte[] data = dp.getData();
                updateGame(data);
            } catch (SocketException e) {
                JOptionPane.showMessageDialog(null, "Socket Error");
            }
        }

    }

    /**
     * Update the game with the retrieved data
     * @param data the byte containing the data
     */
    private void updateGame(byte[] data) {
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
     * Sets the details from the received DatagramPacket. Depending on the type of the Packet, the game details will be updated
     * @param packet the packet that contains the game details
     */
    private void setDetails(Packet packet) {
        Game game = frame.getGame();

        switch (packet.getType()) {
            case 0: {
                game.setAsteroids(packet.getAsteroids());
            }
            break;
            case 1: {
                game.setBullets(packet.getBullets());
            }
            break;
            case 2: {
                game.setShipList(packet.getSpaceships());
            }
            break;
            case 3: {
                game.setAsteroids(packet.getAsteroids());
                game.setBullets(packet.getBullets());
                game.setShipList(packet.getSpaceships());
            }
            break;
            case 4: {
                game.quit();
                JOptionPane.showMessageDialog(null, "Game Over");
                this.frame.setMenuPage(1);
                this.frame.setTab(0);
                this.running = false;
                this.interrupt();
            }
        }

        if (packet.getIndex() != -1) game.setHostShip(packet.getIndex());
    }
}