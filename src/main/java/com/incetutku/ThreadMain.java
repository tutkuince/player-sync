package com.incetutku;

import com.incetutku.entity.Player;

import static com.incetutku.util.Constants.MAX_MESSAGE_COUNT;

/**
 * ThreadMain is the main entry point for running the Player communication example using threads.
 * It creates two Player instances: one as the Initiator (sender) and the other as the Responder (receiver).
 * Two separate threads are used to simulate concurrent message sending and receiving.
 * The program completes after both threads finish exchanging the predefined number of messages.
 */
public class ThreadMain {

    /**
     * Main method to initialize and start the communication threads.
     *
     * @param args command-line arguments (not used in this program)
     * @throws InterruptedException if the main thread is interrupted while waiting for other threads to finish
     */
    public static void main(String[] args) throws InterruptedException {
        // Create the initiator and responder players
        Player initiator = new Player("Initiator");
        Player responder = new Player("Responder");

        // Thread for the initiator to send messages
        Thread sendMessage = new Thread(() -> {
            for (int i = 0; i < MAX_MESSAGE_COUNT; i++) {
                initiator.sendMessage(initiator);
            }
        });

        // Thread for the responder to receive messages
        Thread receiveMessage = new Thread(() -> {
            for (int i = 0; i < MAX_MESSAGE_COUNT; i++) {
                responder.receiveMessage();
            }
        });

        // Start both threads
        sendMessage.start();
        receiveMessage.start();

        // Wait for both threads to complete
        sendMessage.join();
        receiveMessage.join();

        // Indicate that the communication has finished
        System.out.println("\nCommunication completed. Program shutting down.");
    }
}
