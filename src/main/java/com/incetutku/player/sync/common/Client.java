package com.incetutku.player.sync.common;

import java.util.Optional;

import static com.incetutku.player.sync.util.Constant.CAPACITY;

public class Client {
    private final String clientId;
    private final MessageBox messageBox;
    private int sentMessageCount = 0;
    private int receivedMessageCount = 0;

    public Client(String clientId, MessageBox messageBox) {
        this.clientId = clientId;
        this.messageBox = messageBox;
    }

    public boolean sendMessage(String  target, String message) {
        if (sentMessageCount >= CAPACITY) {
            return false;
        }
        messageBox.addMessage(target, new Message(message, clientId));
        sentMessageCount++;
        return true;
    }

    public Optional<Message> receiveMessage() {
        Optional<Message> message = messageBox.getMessage(clientId);
        if (message.isPresent()) {
            receivedMessageCount++;
        }
        return message;
    }

    public boolean hasMessage() {
        return messageBox.hasMessage(clientId);
    }

    public boolean limit() {
        return sentMessageCount >= CAPACITY && receivedMessageCount >= CAPACITY;
    }
}
