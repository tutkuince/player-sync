package com.incetutku.player.sync.socket;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ClientAppIntegrationTest {

    @Test
    void clientApp_worksWithServerSuccessfully() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Start the server (in a thread)
        Future<?> serverFuture = executor.submit(() -> assertDoesNotThrow(SocketServer::new));

        // Short wait to make sure server is listening
        Thread.sleep(800);


        // Start two clients (in threads)
        Future<?> client1Future = executor.submit(() -> assertDoesNotThrow(ClientApp::new));
        Thread.sleep(250);
        Future<?> client2Future = executor.submit(() -> assertDoesNotThrow(ClientApp::new));

        // Wait for all tasks to finish
        serverFuture.get(10, TimeUnit.SECONDS);
        client1Future.get(10, TimeUnit.SECONDS);
        client2Future.get(10, TimeUnit.SECONDS);

        executor.shutdownNow();
        assertTrue(executor.awaitTermination(3, TimeUnit.SECONDS));
    }
}