package com.incetutku.player.sync.socket;

import com.incetutku.player.sync.common.BaseApp;
import static com.incetutku.player.sync.util.Constant.*;

import java.io.*;
import java.net.Socket;

public class ClientApp extends BaseApp {

    @Override
    protected void run() {
        try (
                Socket socket = new Socket(HOST_NAME, PORT_NUMBER);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String role = in.readLine(); // "initiator" veya "listener"
            System.out.println("Assigned role: " + role);

            if ("initiator".equals(role)) {
                int counter = 0;
                while (counter < CAPACITY) {
                    String message = "msg" + counter;
                    out.println(message);
                    String reply = in.readLine();
                    if ("BYE".equals(reply) || reply == null) break;
                    System.out.println("Initiator received: " + reply);
                    counter++;
                }
                System.out.println(role + " done.");
            } else if ("listener".equals(role)) {
                int counter = 0;
                while (true) {
                    String incoming = in.readLine();
                    if ("BYE".equals(incoming) || incoming == null) break;
                    System.out.println("Listener received: " + incoming);
                    String response = incoming + "|" + counter;
                    out.println(response);
                    counter++;
                }
                System.out.println(role + " done.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}