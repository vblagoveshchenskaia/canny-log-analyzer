package com.vblagoveshchenskaia.canny.log.analyzer.service;

import com.vblagoveshchenskaia.canny.log.analyzer.data.Frame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FrameParserTest {

    private FrameParser frameParser;

    @BeforeEach
    void beforeEach() {
        frameParser = new FrameParser();
    }

    @Test
    void parse() throws IOException {
        assertEquals(Map.of("0x57f", List.of(
                        new Frame(0x909c100c0000000L, 0xffffffffff000000L, 5),
                        new Frame(0x909c100c1000000L, 0xffffffffff000000L, 5))),
                frameParser.parse(new ByteArrayInputStream("""
                             0x57f	0x09	0x09	0xc1	0x00	0xc0				  318338	........
                             0x57f	0x09	0x09	0xc1	0x00	0xc1				  318338	........
                        """
                        .getBytes(StandardCharsets.UTF_8))));
    }
}