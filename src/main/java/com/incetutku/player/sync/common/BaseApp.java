package com.incetutku.player.sync.common;

/**
 * Abstract base class for application entry points.
 * <p>
 * Any "executable" sub-class must implement the {@link #run()} method, which will be called
 * immediately when the sub-class is instantiated. Used to ensure a consistent start-up pattern
 * for all high-level modules (such as thread-based, socket server or client apps).
 * </p>
 */
public abstract class BaseApp {

    /**
     * Constructor that automatically calls the {@code run()} method.
     * Ensures initialization logic is executed upon creation of a subclass instance.
     */
    public BaseApp() {
        this.run();
    }

    /**
     * Main run logic for the application.
     * <p>This method should contain the core logic for the sub-class app (thread or socket logic).</p>
     */
    protected abstract void run();
}
