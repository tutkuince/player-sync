package com.incetutku;

import com.incetutku.socket.client.PlayerClient;
import com.incetutku.socket.server.PlayerServer;

import java.io.IOException;

/**
 * SocketMain is the main entry point of the application.
 * It starts the PlayerServer and PlayerClient in separate threads,
 * allowing them to communicate with each other in a simulated network environment.
 * The PlayerServer and PlayerClient run concurrently, each in its own thread,
 * enabling message exchange while maintaining separation.
 */
public class SocketMain {
    public static void main(String[] args) {
        // Start PlayerServer in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                PlayerServer server = new PlayerServer();
                server.start();
            } catch (IOException | ClassNotFoundException e) {
                System.err.printf("Error occurred in PlayerServer: %s\n", e.getMessage());
                e.printStackTrace();
            }
        });

        // Start PlayerClient in a separate thread
        Thread clientThread = new Thread(() -> {
            try {
                // Delay the client start to allow server initialization
                Thread.sleep(2000);
                PlayerClient client = new PlayerClient();
                client.start();
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                System.err.printf("Error occurred in PlayerClient: %s\n", e.getMessage());
                e.printStackTrace();
            }
        });

        // Start both threads
        serverThread.start();
        clientThread.start();

        // Wait for both threads to finish
        try {
            serverThread.join();
            clientThread.join();
        } catch (InterruptedException e) {
            System.err.printf("Main thread was interrupted: %s\n", e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Both PlayerServer and PlayerClient have completed.");
    }
}
