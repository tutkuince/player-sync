package com.incetutku.player.sync;

import com.incetutku.player.sync.common.BaseApp;
import com.incetutku.player.sync.thread.ThreadMain;

public class MainApp {
    public static void main(String[] args) {
        BaseApp app = new ThreadMain();
    }
}
