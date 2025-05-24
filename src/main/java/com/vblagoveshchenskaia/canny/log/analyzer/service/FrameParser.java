package com.vblagoveshchenskaia.canny.log.analyzer.service;

import com.vblagoveshchenskaia.canny.log.analyzer.data.Frame;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class FrameParser {

    public Map<String, List<Frame>> parse(InputStream inputStream) throws IOException {
        var frames = new HashMap<String, List<Frame>>();
        try (var csvRecords = CSVParser.parse(inputStream, StandardCharsets.UTF_8, CSVFormat.MYSQL)) {
            csvRecords.forEach(csvRecord ->
                    frames.computeIfAbsent(csvRecord.get(0).trim(), notUsed -> new ArrayList<>())
                            .add(parseFrameData(csvRecord.values())));
        }
        return frames;
    }

    private Frame parseFrameData(String[] values) {
        var preparedValues = new ArrayList<String>();
        int length = 8;
        for (var i = 1; i <= 8; i++) {
            if (!"".equals(values[i])) {
                preparedValues.add(values[i].substring(2));
            } else {
                length = i - 1;
                IntStream.rangeClosed(i, 8).mapToObj(notUsed -> "00").forEach(preparedValues::add);
                break;
            }
        }
        long mask = -1L << (64 - 8 * length);
        return new Frame(Long.parseUnsignedLong(String.join("", preparedValues), 16), mask, length);
    }
}
