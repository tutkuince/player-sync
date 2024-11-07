package com.incetutku.socket.server;

import com.incetutku.entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.incetutku.util.Constants.*;

/**
 * PlayerServer represents the Initiator player in the communication system.
 * It functions as the server, listening on a specified port for incoming client connections.
 * Once a connection is established, it starts the communication by sending the first message
 * and then alternates between receiving and sending messages.
 */
public class PlayerServer {
    private final String name;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int counter;

    /**
     * Constructs a PlayerServer instance with the name "Initiator".
     */
    public PlayerServer() {
        this.name = "Initiator";
        this.counter = 0;
    }

    /**
     * Starts the PlayerServer, listening for incoming connections, and initiates message exchange.
     * This method continuously sends and receives messages until the predefined message count is reached.
     *
     * @throws IOException            if an I/O error occurs while establishing or using the connection
     * @throws ClassNotFoundException if a received message cannot be deserialized
     */
    public void start() throws IOException, ClassNotFoundException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.printf("%s started on port %s\n", name, PORT);

            socket = serverSocket.accept();
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.printf("Client connected to %s\n", name);

            sendMessage("Hello");

            while (counter < MAX_MESSAGE_COUNT) {
                Message receivedMessage = receiveMessage();

                sendMessage("Hello " + counter);
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