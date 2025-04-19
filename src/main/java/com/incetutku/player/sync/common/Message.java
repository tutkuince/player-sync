package com.incetutku.player.sync.common;

public record Message(
        String content,
        String sender
) {

    @Override
    public String toString() {
        return String.format("Sender: %s, Content: %s", this.sender, this.content);
    }
}
