package com.incetutku.entity;

public class Message {
    private final String content;   // Content of the message (e.g., "Hello").
    private final String sender;    // Name of the sender.
    private final int sequence;     // Sequence number of the message.

    /**
     * Private constructor to enforce the use of the Builder pattern.
     * @param builder The Builder object containing the fields to initialize the Message.
     */
    private Message(Builder builder) {
        this.content = builder.content;
        this.sender = builder.sender;
        this.sequence = builder.sequence;
    }

    /**
     * Static factory method to initiate the Builder for creating a Message instance.
     * @return A new Builder instance for chaining.
     */
    public static Builder builder() {
        return new Builder();
    }

    // Getters for accessing message properties.
    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public int getSequence() {
        return sequence;
    }

    /**
     * Custom toString method to represent the Message in a formatted string.
     * @return A formatted string representing the message with sender, content, and sequence.
     */
    @Override
    public String toString() {
        return String.format("Message from %s: '%s' (Sequence: %s)", sender, content, sequence);
    }

    /**
     * Builder class for creating a Message instance with a flexible and readable pattern.
     */
    public static class Builder {
        private String content; // Temporary storage for content during building.
        private String sender;  // Temporary storage for sender during building.
        private int sequence;   // Temporary storage for sequence number during building.

        // Private constructor for Builder to limit instantiation through the Message class only.
        private Builder() {
        }

        /**
         * Sets the content of the message.
         *
         * @param content The text content of the message.
         * @return This Builder instance to allow chaining.
         */
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * Sets the sender of the message.
         * @param sender The name of the sender.
         * @return This Builder instance to allow chaining.
         */
        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        /**
         * Sets the sequence number of the message.
         * @param sequence The sequence number for the message.
         * @return This Builder instance to allow chaining.
         */
        public Builder sequence(int sequence) {
            this.sequence = sequence;
            return this;
        }

        /**
         * Builds and returns a Message instance using the specified attributes.
         * @return A new Message instance with the fields set by the builder.
         */
        public Message build() {
            return new Message(this);
        }
    }

}
