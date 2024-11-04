package com.incetutku.util;

/**
 * Constants is a utility class that holds common constant values used across the application.
 * This includes configuration values like the maximum message count, server port, and server address.
 * <p>
 * The class is designed to be non-instantiable by providing a private constructor that throws an AssertionError.
 * This ensures that the class is used only for accessing its constants.
 */
public class Constants {
    public static final int MAX_MESSAGE_COUNT = 10;
    public static final int PORT = 12345; // The server's port
    public static final String SERVER_ADDRESS = "localhost"; // The server's address

    /**
     * Private constructor to prevent instantiation.
     * Throws an AssertionError if an attempt is made to instantiate the class.
     */
    private Constants() {
        throw new AssertionError();
    }
}
