package com.vblagoveshchenskaia.canny.log.analyzer.service;

import com.vblagoveshchenskaia.canny.log.analyzer.data.Frame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FrameServiceTest {

    private FrameService frameService;

    @BeforeEach
    void beforeEach() {
        frameService = new FrameService();
    }

    @Test
    void add() {
        assertEquals(new Frame(0x9c1c10000000000L, 0xffff000000000000L, 3),
                frameService.add(
                        new Frame(0x9c1c10000000000L, 0xffffff0000000000L, 3),
                        new Frame(0x9c1000000000000L, 0xffff000000000000L, 2)
                ));
    }

    @Test
    void getDifferences() {
        assertEquals(Map.of("0x57f", Map.of("2.txt", new Frame(0x807200005fe400abL, 0xe400ffL, 8))),
                frameService.getDifferences("1.txt", Map.of(
                        "1.txt", Map.of("0x57f", new Frame(0x807200005f000000L, 0xff8dffffffffff00L, 7)),
                        "2.txt", Map.of("0x57f", new Frame(0x807200005fe400abL, 0xe400ffL, 8)))));
    }

    @Test
    void compare() {
        assertEquals(new Frame(0x807200005fe400abL, 0xe400ffL, 8),
                frameService.compare(new Frame(0x807200005f000000L, 0xff8dffffffffff00L, 7),
                        new Frame(0x807200005fe400abL, -1L, 8)));
    }
}