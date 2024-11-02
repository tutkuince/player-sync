package com.incetutku;

public class Main {
    private static final int MAX_MESSAGE_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        Player initiator = new Player("Initiator");
        Player responder = new Player("Responder");


        Thread sendMessage = new Thread(() -> {
            for (int i = 0; i < MAX_MESSAGE_COUNT; i++) {
                initiator.sendMessage(initiator);
            }
        });

        Thread receiveMessage = new Thread(() -> {
            for (int i = 0; i < MAX_MESSAGE_COUNT; i++) {
                responder.receiveMessage();
            }
        });

        sendMessage.start();
        receiveMessage.start();

        sendMessage.join();
        receiveMessage.join();

        System.out.println("\nCommunication completed. Program shutting down.");
    }
}
