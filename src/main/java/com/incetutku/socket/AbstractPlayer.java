package com.incetutku.socket;

import com.incetutku.entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * AbstractPlayer is an abstract class that defines the basic behavior of a player in the communication system.
 * It provides common functionalities such as establishing a socket connection, sending and receiving messages,
 * and managing the lifecycle of the connection.
 * Each specific player type (e.g., Initiator or Responder) should extend this class and implement the start method.
 */
public abstract class AbstractPlayer {
    protected String name; // The name of the player (e.g., "Initiator" or "Responder")
    protected Socket socket; // The socket for network communication
    protected ObjectOutputStream out; // Output stream for sending messages
    protected ObjectInputStream in; // Input stream for receiving messages
    protected int counter; // Counter to track the sequence of sent messages

    /**
     * Constructs an AbstractPlayer with a specified name.
     * Initializes the message counter to zero.
     *
     * @param name the name of the player
     */
    public AbstractPlayer(String name) {
        this.name = name;
        this.counter = 0;
    }

    /**
     * Initializes a connection to the specified host and port.
     * Sets up the socket, output stream, and input stream for communication.
     *
     * @param host the host to connect to
     * @param port the port number to connect to
     * @throws IOException if an I/O error occurs while creating the socket or streams
     */
    public void initializeConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        System.out.printf("%s connected to %s:%s\n",name, host, port);
    }

    /**
     * Closes the socket connection and associated streams.
     * This method should be called when the player has completed its communication.
     * If any of the resources fail to close, a descriptive error message is printed.
     */
    public void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            System.err.printf("%s: Failed to close input stream - %s\n",name, e.getMessage());
        }
    }

    /**
     * Sends a message to the connected player with the specified content.
     * Each message includes the sender's name, content, and a sequence number.
     *
     * @param content the content of the message to send
     * @throws IOException if an I/O error occurs while sending the message
     */
    protected void sendMessage(String content) throws IOException {
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
    protected Message receiveMessage() throws IOException, ClassNotFoundException {
        Message message = (Message) in.readObject();
        System.out.printf("%s received: %s\n",name, message);
        return message;
    }

    /**
     * Abstract method to be implemented by subclasses.
     * Defines the specific behavior of each player during the communication process.
     *
     * @throws IOException            if an I/O error occurs during communication
     * @throws ClassNotFoundException if a received message cannot be deserialized
     */
    public abstract void start() throws IOException, ClassNotFoundException;
}
