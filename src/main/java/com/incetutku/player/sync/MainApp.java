package com.incetutku.player.sync;

import com.incetutku.player.sync.common.BaseApp;
import com.incetutku.player.sync.socket.ClientApp;
import com.incetutku.player.sync.socket.SocketServer;
import com.incetutku.player.sync.thread.ThreadMain;

public class MainApp {
    public static void main(String[] args) {
//        BaseApp app = new ThreadMain();

        BaseApp app = new SocketServer();
        BaseApp client = new ClientApp();
    }
}
