package com.incetutku.player.sync.socket;

import com.incetutku.player.sync.common.BaseApp;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static com.incetutku.player.sync.util.Constant.*;

/**
 * Application entry point for the out-of-process (socket-based) variant of the player communication system.
 * <p>
 * <b>Responsibilities:</b>
 * <ul>
 *   <li>Listens for two client connections (players) on a configured TCP port.</li>
 *   <li>Assigns each client a role ("initiator" or "listener") in the order they connect.</li>
 *   <li>Acts as a relay: for each turn, it forwards messages from initiator to listener, and vice versa.</li>
 *   <li>Terminates all connections gracefully after the initiator reaches the required send limit.</li>
 * </ul>
 * <b>Usage:</b> Start this class in a terminal, then launch two {@link ClientApp} instances.
 */
public class SocketServer extends BaseApp {

    /**
     * Starts the socket server, handles two client connections, and forwards messages between them.
     */
    @Override
    protected void run() {
        // Try-with-resources for ServerSocket ONLY (because client sockets opened inside may be closed earlier)
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server started at port " + PORT_NUMBER + ", waiting for clients...");

            Socket initiatorSocket = null;
            Socket listenerSocket = null;
            PrintWriter outInitiator = null;
            PrintWriter outListener = null;
            BufferedReader inInitiator = null;
            BufferedReader inListener = null;

            try {
                // Accept the initiator connection (first client)
                initiatorSocket = serverSocket.accept();
                outInitiator = new PrintWriter(initiatorSocket.getOutputStream(), true);
                inInitiator = new BufferedReader(new InputStreamReader(initiatorSocket.getInputStream()));
                outInitiator.println("initiator");
                System.out.println("Initiator connected.");

                // Accept the listener connection (second client)
                listenerSocket = serverSocket.accept();
                outListener = new PrintWriter(listenerSocket.getOutputStream(), true);
                inListener = new BufferedReader(new InputStreamReader(listenerSocket.getInputStream()));
                outListener.println("listener");
                System.out.println("Listener connected.");

                int initiatorToListener = 0;
                while (initiatorToListener < CAPACITY) {
                    String msgFromInit = inInitiator.readLine();
                    if (msgFromInit == null) break;
                    outListener.println(msgFromInit);
                    initiatorToListener++;

                    String replyFromListener = inListener.readLine();
                    if (replyFromListener == null) break;
                    outInitiator.println(replyFromListener);
                }
                // Inform both parties to exit cleanly
                outInitiator.println("BYE");
                outListener.println("BYE");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("Finishing up, closing sockets...");
                // Close all client resources safely
                try { if (outInitiator != null) outInitiator.close(); } catch (Exception ignored) {}
                try { if (outListener != null) outListener.close(); } catch (Exception ignored) {}
                try { if (inInitiator != null) inInitiator.close(); } catch (Exception ignored) {}
                try { if (inListener != null) inListener.close(); } catch (Exception ignored) {}
                try { if (initiatorSocket != null && !initiatorSocket.isClosed()) initiatorSocket.close(); } catch (Exception ignored) {}
                try { if (listenerSocket != null && !listenerSocket.isClosed()) listenerSocket.close(); } catch (Exception ignored) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}