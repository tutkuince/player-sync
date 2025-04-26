package com.incetutku.player.sync.socket;

import com.incetutku.player.sync.common.BaseApp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static com.incetutku.player.sync.util.Constant.*;

public class SocketServer extends BaseApp {

    @Override
    protected void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server started at port " + PORT_NUMBER + ", waiting for clients...");
            Socket initiatorSocket = serverSocket.accept();
            PrintWriter outInitiator = new PrintWriter(initiatorSocket.getOutputStream(), true);
            BufferedReader inInitiator = new BufferedReader(new InputStreamReader(initiatorSocket.getInputStream()));
            outInitiator.println("initiator");
            System.out.println("Initiator connected.");

            Socket listenerSocket = serverSocket.accept();
            PrintWriter outListener = new PrintWriter(listenerSocket.getOutputStream(), true);
            BufferedReader inListener = new BufferedReader(new InputStreamReader(listenerSocket.getInputStream()));
            outListener.println("listener");
            System.out.println("Listener connected.");

            int initiator2listener = 0, listener2initiator = 0;
            while (initiator2listener < CAPACITY) {
                // Initiator'dan mesaj al
                String msgFromInit = inInitiator.readLine();
                if (msgFromInit == null) break;

                // Listener'a ilet
                outListener.println(msgFromInit);
                initiator2listener++;

                // Listener'dan cevap al
                String replyFromListener = inListener.readLine();
                if (replyFromListener == null) break;
                // Initiator'a gönder
                outInitiator.println(replyFromListener);
                listener2initiator++;
            }
            outInitiator.println("BYE");
            outListener.println("BYE");
            System.out.println("Finishing up, closing sockets...");
            initiatorSocket.close();
            listenerSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}