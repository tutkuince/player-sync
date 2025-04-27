package com.incetutku.player.sync.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.incetutku.player.sync.util.Constant.CAPACITY;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private MessageBox messageBox;

    @BeforeEach
    void setUp() {
        messageBox = new MessageBox();
    }

    @Test
    void sendMessage_incrementsSentCount_andDelivers() {
        Client sender = new Client("alice", messageBox);
        Client receiver = new Client("bob", messageBox);

        assertTrue(sender.sendMessage("bob", "Hello Bob!"));
        assertEquals(1, sender.getSentMessageCount());
        assertTrue(receiver.hasMessage());

        Optional<Message> optMsg = receiver.receiveMessage();
        assertTrue(optMsg.isPresent());

        Message msg = optMsg.get();
        assertEquals("Hello Bob!", msg.content());
        assertEquals("alice", msg.sender());
    }


    @Test
    void receiveMessage_incrementsReceivedCount() {
        Client sender = new Client("alice", messageBox);
        Client receiver = new Client("bob", messageBox);

        sender.sendMessage("bob", "1st");
        sender.sendMessage("bob", "2nd");

        assertEquals(0, receiver.getReceivedMessageCount());

        receiver.receiveMessage();
        assertEquals(1, receiver.getReceivedMessageCount());

        receiver.receiveMessage();
        assertEquals(2, receiver.getReceivedMessageCount());
    }


    @Test

    void sendMessage_returnsFalse_whenExceedingCapacity() {
        Client sender = new Client("alice", messageBox);
        Client receiver = new Client("bob", messageBox);

        // Send up to capacity
        for (int i = 0; i < CAPACITY; i++) {
            assertTrue(sender.sendMessage("bob", "msg" + i));
        }

        // Exceed capacity
        assertFalse(sender.sendMessage("bob", "overflow"));
        assertEquals(CAPACITY, sender.getSentMessageCount());
    }


    @Test
    void limit_returnsTrue_onlyWhenBothCountersAreMaxed() {
        Client sender = new Client("alice", messageBox);

        // Initially, neither count is at limit
        assertFalse(sender.limit());

        // Simulate both counters reaching capacity
        for (int i = 0; i < CAPACITY; i++) {
            sender.sendMessage("bob", "msg" + i);
        }

        for (int i = 0; i < CAPACITY; i++) {
            // fake messages in alice's inbox
            messageBox.addMessage("alice", new Message("content", "bob"));
            sender.receiveMessage();
        }
        assertTrue(sender.limit());
    }


    @Test
    void hasMessage_reflectsInboxState() {
        Client sender = new Client("alice", messageBox);
        Client receiver = new Client("bob", messageBox);
        assertFalse(receiver.hasMessage());
        sender.sendMessage("bob", "test");
        assertTrue(receiver.hasMessage());
        receiver.receiveMessage();
        assertFalse(receiver.hasMessage());
    }
}