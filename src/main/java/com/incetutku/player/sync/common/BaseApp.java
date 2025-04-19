package com.incetutku.player.sync.common;

public abstract class BaseApp {

    public BaseApp() {
        this.run();
    }

    protected abstract void run();
}
