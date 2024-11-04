package com.incetutku.socket.client;

import com.incetutku.entity.Message;
import com.incetutku.socket.AbstractPlayer;

import java.io.IOException;

import static com.incetutku.util.Constants.*;

/**
 * PlayerClient represents the Responder player in the communication system.
 * It connects to a specified server (Initiator) on a defined host and port,
 * continuously receives messages, and sends back responses.
 */
public class PlayerClient extends AbstractPlayer {

    /**
     * Constructs a PlayerClient instance with the name "Responder".
     */
    public PlayerClient() {
        super("Responder");
    }

    /**
     * Starts the PlayerClient by establishing a connection to the server, then continuously
     * receiving messages and responding back until 10 messages have been sent.
     *
     * @throws IOException if an I/O error occurs while establishing or using the connection
     * @throws ClassNotFoundException if a received message cannot be deserialized
     */
    @Override
    public void start() throws IOException, ClassNotFoundException {
        try {
            // Initialize the connection to the server
            initializeConnection(SERVER_ADDRESS, PORT);

            // Receive and respond to messages until the counter reaches 10
            while (counter < MAX_MESSAGE_COUNT) {
                Message receivedMessage = receiveMessage();

                // Send a response back to the Initiator
                sendMessage("Hello back " + counter);
            }
        } finally {
            // Ensure that the connection is closed after communication is complete
            closeConnection();
        }
    }
}
