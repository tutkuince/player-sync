package com.incetutku.player.sync.common;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MessageBoxTest {

    @Test
    void addAndGetMessage_deliversMessageToCorrectClient() {
        MessageBox box = new MessageBox();
        Message msg = new Message("Hello!", "sender1");
        box.addMessage("client42", msg);
        assertTrue(box.hasMessage("client42"), "client42 should have a message");
        Optional<Message> optMsg = box.getMessage("client42");
        assertTrue(optMsg.isPresent(), "Message should be present");
        assertEquals(msg, optMsg.get(), "Received message should match the sent message");
    }


    @Test
    void getMessage_returnsEmptyWhenNoMessage() {
        MessageBox box = new MessageBox();
        assertFalse(box.hasMessage("unknown"), "Unknown client should not have any messages");
        assertTrue(box.isEmpty(), "Should be empty if no messages");
    }


    @Test
    void addMessage_createsInboxAutomatically() {
        MessageBox box = new MessageBox();
        Message msg = new Message("Auto-inbox", "senderX");
        box.addMessage("dynamic-client", msg);
        assertTrue(box.hasMessage("dynamic-client"), "Inbox should be created automatically");
    }


    @Test

    void hasMessage_returnsFalseAfterPollingTheOnlyMessage() {
        MessageBox box = new MessageBox();
        Message msg = new Message("One-time", "senderY");
        box.addMessage("clientX", msg);
        assertTrue(box.hasMessage("clientX"));
        box.getMessage("clientX"); // poll
        assertFalse(box.hasMessage("clientX"));
    }


    @Test

    void isEmpty_returnsTrueWhenNoMessages_anyClient() {
        MessageBox box = new MessageBox();
        assertTrue(box.isEmpty(), "Should be empty initially");
        box.addMessage("cA", new Message("hi", "cB"));
        assertFalse(box.isEmpty(), "Should not be empty after adding a message");
        box.getMessage("cA");
        assertTrue(box.isEmpty(), "Should be empty after polling the only message");
    }


    @Test
    void addMessage_respectsCapacity() {
        MessageBox box = new MessageBox();
        int capacity = 10; // Should reflect Constant.CAPACITY, update if you change it!
        for (int i = 0; i < capacity; i++) {
            box.addMessage("capClient", new Message("msg" + i, "sender"));
        }

        // Try to add over capacity -- ArrayBlockingQueue.add throws IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            box.addMessage("capClient", new Message("msgX", "sender"));
        });
    }
}