package com.incetutku.socket.client;

import com.incetutku.entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.incetutku.util.Constants.*;

/**
 * PlayerClient represents the Responder player in the communication system.
 * It connects to a specified server (Initiator) on a defined host and port,
 * continuously receives messages, and sends back responses.
 */
public class PlayerClient {
    private final String name;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int counter;

    /**
     * Constructs a PlayerClient instance with the name "Responder".
     */
    public PlayerClient() {
        this.name = "Responder";
        this.counter = 0;
    }

    /**
     * Starts the PlayerClient by establishing a connection to the server, then continuously
     * receiving messages and responding back until 10 messages have been sent.
     *
     * @throws IOException            if an I/O error occurs while establishing or using the connection
     * @throws ClassNotFoundException if a received message cannot be deserialized
     */
    public void start() throws IOException, ClassNotFoundException {
        try {
            initializeConnection(SERVER_ADDRESS, PORT);

            while (counter < MAX_MESSAGE_COUNT) {
                Message receivedMessage = receiveMessage();

                sendMessage("Hello back " + counter);
            }
        } finally {
            closeConnection();
        }
    }

    /**
     * Initializes a connection to the specified host and port.
     * Sets up the socket, output stream, and input stream for communication.
     *
     * @param host the host to connect to
     * @param port the port number to connect to
     * @throws IOException if an I/O error occurs while creating the socket or streams
     */
    private void initializeConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        System.out.printf("%s connected to %s:%s\n", name, host, port);
    }

    /**
     * Closes the socket connection and associated streams.
     * This method should be called when the player has completed its communication.
     * If any of the resources fail to close, a descriptive error message is printed.
     */
    private void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            System.err.printf("%s: Failed to close input stream - %s\n", name, e.getMessage());
        }
    }

    /**
     * Sends a message to the connected player with the specified content.
     * Each message includes the sender's name, content, and a sequence number.
     *
     * @param content the content of the message to send
     * @throws IOException if an I/O error occurs while sending the message
     */
    private void sendMessage(String content) throws IOException {
        Message message = Message.builder()
                .sender(name)
                .content(content)
                .sequence(++counter)
                .build();
        System.out.println(message);  // toString() method from Message class will be used
        out.writeObject(message);
    }

    /**
     * Receives a message from the connected player.
     * The received message is printed to the console.
     *
     * @return the received Message object
     * @throws IOException            if an I/O error occurs while reading the message
     * @throws ClassNotFoundException if the message cannot be deserialized into a Message object
     */
    private Message receiveMessage() throws IOException, ClassNotFoundException {
        Message message = (Message) in.readObject();
        System.out.printf("%s received: %s\n", name, message);
        return message;
    }
}
