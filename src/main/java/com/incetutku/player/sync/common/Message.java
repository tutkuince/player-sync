package com.incetutku.player.sync.common;

/**
 * Immutable value object representing a message sent between clients/players.
 * <p>
 * Each Message encapsulates:
 * <ul>
 *     <li><b>content</b>: The textual content of the message</li>
 *     <li><b>sender</b>: The unique ID of the client who sent the message</li>
 * </ul>
 * This record is intended to be used as the unit of exchange via MessageBox or across sockets
 * to facilitate communication.
 * </p>
 */
public record Message(
        String content,
        String sender
) {

    /**
     * Returns a string representation of the message in the format:
     * "Sender: senderId, Content: content"
     * @return Readable string describing who sent what.
     */
    @Override
    public String toString() {
        return String.format("Sender: %s, Content: %s", this.sender, this.content);
    }
}
