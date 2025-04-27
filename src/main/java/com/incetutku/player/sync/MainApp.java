package com.incetutku.player.sync;

import com.incetutku.player.sync.common.BaseApp;
import com.incetutku.player.sync.socket.ClientApp;
import com.incetutku.player.sync.socket.SocketServer;
import com.incetutku.player.sync.thread.ThreadMain;

public class MainApp {
    public static void main(String[] args) {
        if ("--thread".equals(args[0])) {
            BaseApp app = new ThreadMain();
        } else if ("--client".equals(args[0])) {
            BaseApp app = new ClientApp();
        } else if ("--server".equals(args[0])) {
            BaseApp app = new SocketServer();
        }
    }
}
