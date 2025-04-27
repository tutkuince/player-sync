package com.incetutku.player.sync.common;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.incetutku.player.sync.util.Constant.CAPACITY;

/**
 * Thread-safe message dispatcher providing in-memory inboxes for each client.
 *
 * <p>
 * MessageBox is responsible for storing and delivering messages between clients (players)
 * in the same Java process. Each client has a private messaging queue (inbox) identified by its unique client ID.
 * Clients can add messages to the inbox of other clients and retrieve (poll) messages from their own inbox.
 * Internally, the class uses a ConcurrentHashMap from client ID to a bounded BlockingQueue of {@link Message}s,
 * ensuring efficient concurrent access and avoiding race conditions.
 * </p>
 *
 * <ul>
 *   <li><b>addMessage(String clientId, Message message):</b> Adds a message to the target client's queue.</li>
 *   <li><b>getMessage(String clientId):</b> Polls the next message from the specified client's queue, if any.</li>
 *   <li><b>hasMessage(String clientId):</b> Checks if there is at least one message for the given client.</li>
 *   <li><b>isEmpty():</b> Checks whether all inbox queues are empty.</li>
 * </ul>
 *
 */
public class MessageBox {

    private final ConcurrentMap<String, BlockingQueue<Message>> messageMap = new ConcurrentHashMap<>();

    /**
     * Adds a Message to the specified client's queue.
     * If the client's queue does not exist, it will be created automatically (capacity limited).
     * @param clientId the unique ID of the target receiving client
     * @param message  the Message to deliver
     */
    public void addMessage(String clientId, Message message) {
        messageMap.computeIfAbsent(clientId, s -> new ArrayBlockingQueue<>(CAPACITY)).add(message);
    }

    /**
     * Polls and removes the next Message for the given client, if any.
     *
     * @param clientId the unique client ID to check for messages
     * @return an Optional containing the next Message if available, or empty if the queue is null or empty
     */
    public Optional<Message> getMessage(String clientId) {
        BlockingQueue<Message> messages = messageMap.get(clientId);

        if (messages.isEmpty()) {
            System.out.println("No message found for " + clientId);
            return Optional.empty();
        }
        return Optional.ofNullable(messages.poll());
    }

    /**
     * Checks if the client's inbox queue has at least one pending message.
     * @param clientId the client ID whose queue is queried
     * @return true if there is at least one message ready for this client
     */
    public boolean hasMessage(String clientId) {
        BlockingQueue<Message> messages = messageMap.get(clientId);
        return messages != null && !messages.isEmpty();
    }

    /**
     * @return true if all message inboxes are currently empty; false otherwise.
     */
    public boolean isEmpty() {
        return messageMap.values().stream().allMatch(Collection::isEmpty);
    }
}
