package com.incetutku.player.sync.socket;

import com.incetutku.player.sync.common.BaseApp;
import com.incetutku.player.sync.common.Client;
import com.incetutku.player.sync.common.Message;
import com.incetutku.player.sync.common.MessageBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.incetutku.player.sync.util.Constant.PORT_NUMBER;

public class SocketServer extends BaseApp {
    static List<String> clientNames = List.of("initiator", "listener");
    static MessageBox messageBox = new MessageBox();
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static final Map<String, Socket> connections = new HashMap<>();
    private static final Map<String, Client> clients = new HashMap<>();
    int clientCount = 0;

    @Override
    protected void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server started in " + PORT_NUMBER + ", waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket clientSocket) {
        String clientId = clientNames.get(clientCount);
        System.out.println("Client connected: " + clientId);

        connections.put(clientId, clientSocket);
        Client client = new Client(clientId, messageBox);
        clients.put(clientId, client);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Send role to client
            out.println(clientId);
            clientCount++;

            String inputLine;

            // Process client messages
            while ((inputLine = in.readLine()) != null) {
                String[] params = inputLine.split("::");
                if (params.length > 1) {
                    if ("poll".equals(params[0])) {
                        if (client.hasMessage()) {
                            Optional<Message> message = client.receiveMessage();
                            message.ifPresent(m -> {
                                out.println(m.sender() + "::" + m.content());
                                System.out.println(clientId + " received: " + m);
                            });
                        } else if (client.limit()) {
                            out.println("Connection closed by server.");
                            System.out.println(clientId + " reached limit. Closing...");
                            break;
                        }
                    } else if ("send".equals(params[0])) {
                        if (params.length == 3) {
                            String target = params[1];
                            String msg = params[2];
                            messageBox.addMessage(target, new Message(msg, clientId));
                            System.out.println(clientId + " sent to " + target + ": " + msg);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOException from " + clientId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
