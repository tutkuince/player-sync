package com.incetutku.player.sync.socket;

import com.incetutku.player.sync.common.BaseApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.incetutku.player.sync.util.Constant.HOST_NAME;
import static com.incetutku.player.sync.util.Constant.PORT_NUMBER;

public class ClientApp extends BaseApp {
    private static final int POLL_INTERVAL_MS = 1000;

    @Override
    protected void run() {
        try (Socket socket = new Socket(HOST_NAME, PORT_NUMBER);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String clientRole = in.readLine();  // initiator ya da listener
            System.out.println("Assigned role: " + clientRole);

            int messageCount = 0;
            boolean messageSent = false;

            while (true) {
                // initiator ilk mesajı yalnızca bir kere yollar
                if (clientRole.equals("initiator") && !messageSent) {
                    out.println("send::listener::This is message 1");
                    messageSent = true;
                    System.out.println("Sent initial message to listener");
                }

                // Tüm clientler sürekli poll eder
                out.println("poll::");

                String serverResponse = in.readLine();
                if (serverResponse != null) {
                    if (serverResponse.contains("Connection closed")) {
                        System.out.println("Server closed the connection.");
                        break;
                    }
                    System.out.println("Received: " + serverResponse);

                    // Eğer initiator cevap aldıysa yeni mesaj yollayabilir
                    if (clientRole.equals("initiator") && messageCount < 9) {
                        messageCount++;
                        String msg = "This is message " + (messageCount + 1);
                        out.println("send::listener::" + msg);
                        System.out.println("Sent: " + msg);
                    }
                }

                Thread.sleep(1000); // Çok hızlı poll yapmasın
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
