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

import static com.incetutku.player.sync.util.Constant.PORT;

public class SocketServer extends BaseApp {
    static List<String> clientNames = List.of("client-1", "client-2");
    static MessageBox messageBox = new MessageBox();
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    Map<String, Socket> connections = new HashMap<>();
    int clientCount = 0;

    @Override
    protected void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
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
        connections.put(clientId, clientSocket);
        Client client = new Client(clientId, messageBox);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                String[] params = inputLine.split("::");
                if (params.length > 1) {
                    if ("poll".equals(params[0])) {
                        if (client.hasMessage()) {
                            Optional<Message> message = client.receiveMessage();
                            if (message.isPresent()) {
                                Message m = message.get();
                                out.println(m.content());
                            }
                        } else if (client.limit()) {
                            out.println("closeConnection");
                        }
                    } else if ("send".equals(params[0])) {
                        if (params.length == 3) {
                            messageBox.addMessage(params[1], new Message(params[2], clientId));
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
