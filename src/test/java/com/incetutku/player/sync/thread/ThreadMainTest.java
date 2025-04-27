package com.incetutku.player.sync.thread;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadMainTest {

    @Test
    void threadMain_completesWithoutError() {
        assertDoesNotThrow(ThreadMain::new);
    }
}