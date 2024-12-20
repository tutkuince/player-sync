package com.incetutku.entity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Player class represents an entity that can send and receive messages in a synchronized manner.
 * This class demonstrates inter-thread communication where two players (instances of this class)
 * communicate in a sequential, turn-based manner.
 * <p>
 * Each player instance alternates between sending and receiving messages in a way that:
 * - The sender must wait until the message has been received and acknowledged before sending the next message.
 * - The receiver waits for a message to be available before it can process it.
 * <p>
 * Synchronization between threads is achieved using Lock and Condition objects to control access and ensure
 * sequential operation. A BlockingQueue is used to facilitate the message exchange.
 */
public class Player {
    private final String name;

    private int sentMessageCounter = 0;
    private int receivedMessageCounter = 0;
    private static boolean messagePresent = false;

    private static final Lock lock = new ReentrantLock();
    private static final Condition messageAvailable = lock.newCondition();
    private static final Condition messageConsumed = lock.newCondition();

    private static final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

    /**
     * Constructor for creating a Player instance with a given name.
     *
     * @param name The name of the player, used in messages to identify the sender.
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the player.
     *
     * @return The name of this player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of messages sent by this player.
     *
     * @return int
     */
    public int getSentMessageCounter() {
        return sentMessageCounter;
    }

    /**
     * Returns the number of messages received by this player.
     *
     * @return int
     */
    public int getReceivedMessageCounter() {
        return receivedMessageCounter;
    }

    /**
     * Sends a message from this player to the receiver.
     * This method is designed to be used in a synchronized manner, waiting until
     * the previous message has been consumed by the receiver before sending a new one.
     *
     * @param sender The Player instance that is sending the message.
     */
    public void sendMessage(Player sender) {
        lock.lock();
        try {
            while (messagePresent) {
                messageConsumed.await();
            }
            Message message = Message.builder()
                    .sender(sender.getName())
                    .content("Hello")
                    .sequence(++sentMessageCounter)
                    .build();

            messageQueue.put(message);
            System.out.println(message);

            messagePresent = true;
            messageAvailable.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Receives a message from the queue.
     * This method waits until a message is available to be received, processes the message,
     * and then notifies the sender that the message has been consumed so the sender can proceed.
     */
    public void receiveMessage() {
        lock.lock();
        try {
            while (!messagePresent) {
                messageAvailable.await();
            }

            Message receivedMessage = messageQueue.remove();

            System.out.printf("Responder received: %s \n", receivedMessage);

            receivedMessageCounter++;

            messagePresent = false;
            messageConsumed.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}
