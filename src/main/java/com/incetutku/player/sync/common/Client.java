package com.incetutku.player.sync.common;

import java.util.Optional;

import static com.incetutku.player.sync.util.Constant.CAPACITY;

/**
 * Represents a player/client entity that can send and receive messages via a shared MessageBox.
 * <p>
 * Each Client instance tracks the number of messages it has sent and received. Sending or receiving is performed via
 * the {@link MessageBox}, which acts as the message delivery medium between clients.
 * </p>
 * <ul>
 *     <li><b>sendMessage</b>: sends a message to a specific target client</li>
 *     <li><b>receiveMessage</b>: polls for messages sent to this client</li>
 *     <li>When a client receives a message, it is expected to send a reply message that appends its own counter.</li>
 *     <li>A client will stop sending/receiving after reaching the set capacity.</li>
 * </ul>
 */
public class Client {
    private final String clientId;
    private final MessageBox messageBox;
    private int sentMessageCount = 0;
    private int receivedMessageCount = 0;

    /**
     * Constructs a new Client with a unique ID and a shared MessageBox.
     * @param clientId    The unique identifier for this client
     * @param messageBox  The message bus/shared box used for communication
     */
    public Client(String clientId, MessageBox messageBox) {
        this.clientId = clientId;
        this.messageBox = messageBox;
    }

    /**
     * @return The total count of messages this client has sent so far.
     */
    public int getSentMessageCount() {
        return sentMessageCount;
    }

    /**
     * @return The total count of messages this client has received so far.
     */
    public int getReceivedMessageCount() {
        return receivedMessageCount;
    }

    /**
     * Sends a message to a target client if not exceeded message capacity.
     * @param target  The receiving client's unique ID
     * @param message The string content of the message to send
     * @return        {@code true} if the message was sent, {@code false} if the capacity has been reached
     */
    public boolean sendMessage(String target, String message) {
        if (sentMessageCount >= CAPACITY) {
            return false;
        }
        messageBox.addMessage(target, new Message(message, clientId));
        sentMessageCount++;
        return true;
    }

    /**
     * Attempts to receive the next queued message for this client (if any).
     * Increments the received message count if a message is found.
     * @return An {@link Optional} containing the incoming Message, or empty if none found
     */
    public Optional<Message> receiveMessage() {
        Optional<Message> message = messageBox.getMessage(clientId);
        if (message.isPresent()) {
            receivedMessageCount++;
        }
        return message;
    }

    /**
     * @return {@code true} if there is at least one unread message for this client, {@code false} otherwise.
     */
    public boolean hasMessage() {
        return messageBox.hasMessage(clientId);
    }

    /**
     * Checks whether the stop condition has been reached (sent and received message counts meet or exceed capacity).
     * @return {@code true} if both sent and received limits are reached, {@code false} otherwise.
     */
    public boolean limit() {
        return sentMessageCount >= CAPACITY && receivedMessageCount >= CAPACITY;
    }
}
