package com.incetutku.socket.server;

import com.incetutku.entity.Message;
import com.incetutku.socket.AbstractPlayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import static com.incetutku.util.Constants.*;

/**
 * PlayerServer represents the Initiator player in the communication system.
 * It functions as the server, listening on a specified port for incoming client connections.
 * Once a connection is established, it starts the communication by sending the first message
 * and then alternates between receiving and sending messages.
 */
public class PlayerServer extends AbstractPlayer {

    /**
     * Constructs a PlayerServer instance with the name "Initiator".
     */
    public PlayerServer() {
        super("Initiator");
    }

    /**
     * Starts the PlayerServer, listening for incoming connections, and initiates message exchange.
     * This method continuously sends and receives messages until the predefined message count is reached.
     *
     * @throws IOException            if an I/O error occurs while establishing or using the connection
     * @throws ClassNotFoundException if a received message cannot be deserialized
     */
    @Override
    public void start() throws IOException, ClassNotFoundException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.printf("%s started on port %s\n", name, PORT);

            // Accepts a connection from the client (Responder)
            socket = serverSocket.accept();
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.printf("Client connected to %s\n", name);

            // Send the first message to initiate communication
            sendMessage("Hello");

            // Receive and respond to messages until the counter reaches 10
            while (counter < MAX_MESSAGE_COUNT) {
                Message receivedMessage = receiveMessage();

                // Send a response message with the updated counter value
                sendMessage("Hello " + counter);
            }
        } finally {
            // Ensures the connection is closed after communication completes
            closeConnection();
        }
    }
}