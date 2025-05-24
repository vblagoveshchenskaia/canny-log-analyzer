package com.vblagoveshchenskaia.canny.log.analyzer.service;

import com.vblagoveshchenskaia.canny.log.analyzer.data.Frame;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class FrameService {

    public Frame add(Frame frame, Frame addition) {
        int minLength = Math.min(frame.getLength(), addition.getLength());
        int maxLength = Math.max(frame.getLength(), addition.getLength());
        long mask = -1L << (64 - 8 * minLength) & frame.getMask() & addition.getMask() & ~(frame.getData() ^ addition.getData());
        frame.setMask(mask);
        frame.setLength(maxLength);
        return frame;
    }

    public Frame compare(Frame base, Frame other) {
        int i = 8 * base.getLength();
        long mask = (base.getData() ^ other.getData()) & (base.getMask() & other.getMask()) | other.getMask() << i - 1 << 1 >>> i;
        return new Frame(other.getData(), mask, other.getLength());
    }

    public Map<String, Map<String, Frame>> getDifferences(String logToCompare, Map<String, Map<String, Frame>> data) {
        var differences = new TreeMap<String, Map<String, Frame>>();
        data
                .entrySet()
                .stream()
                .filter(entry -> !entry.getKey().equals(logToCompare))
                .forEach(entry -> entry.getValue().forEach((id, frame) -> differences.computeIfAbsent(id, notUsed -> new TreeMap<>())
                        .put(entry.getKey(), Optional.ofNullable(data.get(logToCompare).get(id))
                                .map(f -> compare(f, frame)).orElse(frame))));
        return differences;
    }
}
