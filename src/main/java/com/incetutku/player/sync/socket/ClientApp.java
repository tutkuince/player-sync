package com.incetutku.player.sync.socket;

import com.incetutku.player.sync.common.BaseApp;
import static com.incetutku.player.sync.util.Constant.*;

import java.io.*;
import java.net.Socket;

/**
 * Application entry point for the out-of-process (socket-based) player client.
 *
 * <p>
 * <b>Responsibilities:</b>
 * <ul>
 *     <li>Connects to the server, receives its assigned role ("initiator" or "listener")</li>
 *     <li>If assigned "initiator", iteratively sends messages and waits for responses</li>
 *     <li>If assigned "listener", receives messages, creates reply messages (by concatenating own counter), and responds</li>
 *     <li>Stops gracefully when a "BYE" is received or the connection is closed</li>
 * </ul>
 */
public class ClientApp extends BaseApp {

    @Override
    protected void run() {
        try (
                Socket socket = new Socket(HOST_NAME, PORT_NUMBER);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String role = in.readLine(); // Receives "initiator" or "listener"
            System.out.println("Assigned role: " + role);

            if ("initiator".equals(role)) {
                int counter = 0;
                while (counter < CAPACITY) {
                    String message = role + "::" + "msg" + counter;
                    out.println(message);
                    String reply = in.readLine();
                    if ("BYE".equals(reply) || reply == null) break;
                    int sep = reply.indexOf("::");
                    if (sep > 0) {
                        String sender = reply.substring(0, sep);
                        String content = reply.substring(sep + 2);
                        System.out.println("[initiator] Received message: " + content + " (from: " + sender + ")");
                    } else {
                        System.out.println("[initiator] Received reply: " + reply);
                    }
                    counter++;
                    Thread.sleep(800); // Sleep 0.8 second per iteration
                }
                System.out.println(role + " done.");
            } else if ("listener".equals(role)) {
                int counter = 0;
                while (true) {
                    String incoming = in.readLine();
                    if ("BYE".equals(incoming) || incoming == null) break;
                    int sep = incoming.indexOf("::");
                    String sender = sep > 0 ? incoming.substring(0, sep) : "unknown";
                    String content = sep > 0 ? incoming.substring(sep + 2) : incoming;
                    System.out.println("[listener] Received message: " + content + " (from: " + sender + ")");
                    String response = role + "::" + content + "|" + counter;
                    out.println(response);
                    counter++;
                    Thread.sleep(1000); // Sleep 1 second per iteration
                }
                System.out.println(role + " done.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}