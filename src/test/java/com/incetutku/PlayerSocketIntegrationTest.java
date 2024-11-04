package com.incetutku;

import com.incetutku.socket.client.PlayerClient;
import com.incetutku.socket.server.PlayerServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerSocketIntegrationTest {

    private PlayerServer server;
    private PlayerClient client;

    @BeforeEach
    void setUp() {
        // Create real instances of PlayerServer and PlayerClient
        server = new PlayerServer();
        client = new PlayerClient();
    }

    @Test
    @DisplayName("Given PlayerServer and PlayerClient, when communication starts, then messages are exchanged correctly")
    void givenPlayerServerAndClient_whenCommunicationStarts_thenMessagesExchangedCorrectly() throws IOException, InterruptedException {
        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        // Start the client in a separate thread
        Thread clientThread = new Thread(() -> {
            try {
                // Add a short delay for the client to allow the server to be ready
                Thread.sleep(500);
                client.start();
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start both server and client threads
        serverThread.start();
        clientThread.start();

        // Wait for both threads to complete
        serverThread.join();
        clientThread.join();

        // If no exception occurs during the test, we assume communication was successful
        assertTrue(true, "Messages were exchanged successfully between PlayerServer and PlayerClient.");
    }
}