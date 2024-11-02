package com.incetutku;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    private Player initiator;
    private Player responder;

    @BeforeEach
    void setUp() {
        initiator = new Player("Initiator");
        responder = new Player("Responder");
    }

    @Test
    void givenInitiator_whenSendsMessage_thenResponderReceivesMessageInOrder() throws InterruptedException {
        // Given an initiator and a responder
        int expectedCount = 3;
        Thread initiatorThread = new Thread(() -> {
            for (int i = 0; i < expectedCount; i++) {
                initiator.sendMessage(initiator);
            }
        });

        Thread responderThread = new Thread(() -> {
            for (int i = 0; i < expectedCount; i++) {
                responder.receiveMessage();
            }
        });

        // When the initiator sends messages sequentially
        initiatorThread.start();
        responderThread.start();

        initiatorThread.join();
        responderThread.join();


        assertEquals(expectedCount, initiator.getSentMessageCounter());
        assertEquals(expectedCount, responder.getReceivedMessageCounter());
    }
}