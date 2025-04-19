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
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadMain extends BaseApp {

    static List<String> clientNames = List.of("client-1", "client-2");
    static String initiatorClient = clientNames.get(0);
    static String targetClient = clientNames.get(1);
    static AtomicBoolean firstMessage = new AtomicBoolean(false);
    static MessageBox messageBox = new MessageBox();
    static AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(clientNames.size());

        List<Future<?>> futures = new ArrayList<>(clientNames.size());

        clientNames.forEach(clientName -> futures.add(executorService.submit(() -> {
            client(clientName, messageBox);
        })));

        executorService.shutdown();

        // make the main thread wait for others to finish
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Exception while getting from future" + e.getMessage());
                e.getMessage();
            }
        }
    }

    private void client(String clientName, MessageBox messageBox) {
        Client client = new Client(clientName, messageBox);

        if (!firstMessage.get() && clientName.equals(initiatorClient)) {
            client.sendMessage(targetClient, "This is the first message");
        }

        boolean limitRaised = false;

        while (!limitRaised) {
            try {
                if (client.hasMessage()) {
                    Optional<Message> optionalMessage = client.receiveMessage();
                    if (optionalMessage.isPresent()) {
                        Message message = optionalMessage.get();
                        int i = counter.incrementAndGet();
                        System.out.println("Message received: " + message + i);
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
                e.printStackTrace();
            }
        }
    }
}
