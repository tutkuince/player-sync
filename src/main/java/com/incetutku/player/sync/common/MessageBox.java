package com.incetutku.player.sync.common;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.incetutku.player.sync.util.Constant.CAPACITY;

public class MessageBox {

    private final ConcurrentMap<String, BlockingQueue<Message>> messageMap = new ConcurrentHashMap<>();

    public void addMessage(String clientId, Message message) {
        messageMap.computeIfAbsent(clientId, s -> new ArrayBlockingQueue<>(CAPACITY)).add(message);
    }

    public Optional<Message> getMessage(String clientId) {
        BlockingQueue<Message> messages = messageMap.get(clientId);

        if (messages.isEmpty()) {
            System.out.println("No message found for " + clientId);
            return Optional.empty();
        }
        return Optional.ofNullable(messages.poll());
    }

    public boolean hasMessage(String clientId) {
        BlockingQueue<Message> messages = messageMap.get(clientId);
        return messages != null && !messages.isEmpty();
    }

    public boolean isEmpty() {
        return messageMap.values().stream().allMatch(Collection::isEmpty);
    }
}
