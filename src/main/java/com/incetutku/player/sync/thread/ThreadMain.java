package com.incetutku.player.sync.thread;

import com.incetutku.player.sync.common.BaseApp;
import com.incetutku.player.sync.common.Client;
import com.incetutku.player.sync.common.Message;
import com.incetutku.player.sync.common.MessageBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Application entry point for the in-process, thread-based variant of the player communication system.
 * <p>
 * <b>Responsibilities:</b>
 * <ul>
 *   <li>Creates two client threads (players) within the same Java process.</li>
 *   <li>The first client acts as the initiator, sending the initial message to the second client (listener).</li>
 *   <li>Each client runs logic to send and receive messages via a shared, thread-safe {@link MessageBox} object.</li>
 *   <li>The program terminates gracefully once the initiator has sent and received the required number of messages.</li>
 * </ul>
 * <p>
 * <b>How it works:</b><br>
 * <ol>
 *   <li>Spawns two threads: one for each client</li>
 *   <li>The initiator sends the first message</li>
 *   <li>Each client listens for messages; upon receiving one, they reply, appending their own message counter</li>
 *   <li>Threads exit when the defined send/receive limit (capacity) is reached</li>
 * </ol>
 * </p>
 */
public class ThreadMain extends BaseApp {

    static List<String> clientNames = List.of("client-1", "client-2");
    static String initiatorClient = clientNames.get(0);
    static String targetClient = clientNames.get(1);
    static AtomicBoolean firstMessage = new AtomicBoolean(false);
    static MessageBox messageBox = new MessageBox();

    /**
     * Starts two client threads, waits for both to finish, and ensures graceful application exit.
     */
    @Override
    protected void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(clientNames.size());
        List<Future<?>> futures = new ArrayList<>(clientNames.size());

        clientNames.forEach(clientName -> futures.add(executorService.submit(() -> {
            client(clientName, messageBox);
        })));

        executorService.shutdown();

        // Wait for all client threads to finish before exiting
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Exception while getting from future" + e.getMessage());
            }
        }
    }

    /**
     * Client thread logic: sends and receives messages using the shared MessageBox.
     * <ul>
     *   <li>The initiator sends the first message if not already sent.</li>
     *   <li>Each client listens for messages addressed to it; when a message arrives, a reply is sent.</li>
     *   <li>The conversation continues until each client's send/receive counter reaches the defined capacity.</li>
     * </ul>
     * @param clientName The name/ID for this logical client/player thread
     * @param messageBox The shared message delivery system
     */
    private void client(String clientName, MessageBox messageBox) {
        Client client = new Client(clientName, messageBox);

        if (!firstMessage.get() && clientName.equals(initiatorClient)) {
            client.sendMessage(targetClient, "This is the first message");
            firstMessage.set(true);
        }

        boolean limitRaised = false;

        while (!limitRaised) {
            try {
                if (client.hasMessage()) {
                    Optional<Message> optionalMessage = client.receiveMessage();
                    if (optionalMessage.isPresent()) {
                        Message message = optionalMessage.get();
                        System.out.println(clientName + " received: " + message + " (receivedCount=" + client.getReceivedMessageCount() + ")");
                        boolean result = client.sendMessage(message.sender(), message.content());

                        if (!result) {
                            limitRaised = true;
                        }
                    }
                } else if (client.limit()) {
                    limitRaised = true;
                }
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted for client: " + clientName);
                break;
            }
        }
        System.out.println(clientName + " has finished its messaging loop.");
    }
}
