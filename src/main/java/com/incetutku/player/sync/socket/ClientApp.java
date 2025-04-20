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

    @Override
    protected void run() {
        try (Socket socket = new Socket(HOST_NAME, PORT_NUMBER);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("Hello from Client!");

            String fromServer;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
            }
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
