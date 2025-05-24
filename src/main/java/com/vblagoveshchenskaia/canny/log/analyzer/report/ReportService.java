package com.vblagoveshchenskaia.canny.log.analyzer.report;

import com.vblagoveshchenskaia.canny.log.analyzer.data.Frame;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ReportService {

    private static final String FORMAT = "%16s\t%s";
    private static final String NOT_PRESENT = "not present";

    public String printMainReport(Map<String, Map<String, Frame>> data) {
        var stringBuilder = new StringBuilder();
        data.forEach((fileName, frames) -> {
            stringBuilder.append(fileName).append("\n");
            frames
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getMask() != 0)
                    .map(entry -> String.format(FORMAT, entry.getKey(), print(entry.getValue()) + "\n"))
                    .forEach(stringBuilder::append);
        });
        return stringBuilder.toString();
    }

    public String printDifferencesReport(Map<String, Map<String, Frame>> data,
                                         Map<String, Map<String, Frame>> differences,
                                         String logToCompare) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Comparing ").append(logToCompare).append("\n");
        data
                .values()
                .stream()
                .map(Map::keySet)
                .flatMap(Set::stream)
                .distinct()
                .forEach(id -> {
                    var frame = data.get(logToCompare).get(id);
                    var frameString = String.format(FORMAT, id,
                            Optional.ofNullable(frame).map(this::print).orElse(NOT_PRESENT));
                    var comparisonStrings = new ArrayList<String>();
                    data
                            .keySet()
                            .stream()
                            .filter(log -> !log.equals(logToCompare))
                            .forEach(log -> {
                                var diff = differences.getOrDefault(id, Map.of()).get(log);
                                if (diff == null || frame == null
                                    || (diff.getMask() != 0 && frame.getMask() != 0)
                                    || frame.getLength() != diff.getLength()) {
                                    comparisonStrings.add(String.format(FORMAT, log,
                                            Optional.ofNullable(diff).map(this::print).orElse(NOT_PRESENT)));
                                }
                            });
                    if (!comparisonStrings.isEmpty()) {
                        stringBuilder.append(frameString);
                        stringBuilder.append("\n");
                        stringBuilder.append(String.join("\n", comparisonStrings));
                        stringBuilder.append("\n\n");
                    }
                });
        return stringBuilder.toString();
    }

    public String print(Frame frame) {
        var stringBuilder = new StringBuilder();
        var numberOfBits = 8 * frame.getLength();
        for (var i = 0; i < numberOfBits; i++) {
            if (i % 8 == 0 && i > 0) {
                stringBuilder.append("\t");
            }
            long searchMask = Long.MIN_VALUE >>> i;
            stringBuilder.append((frame.getMask() & searchMask) != 0 ? ((frame.getData() & searchMask) >>> (64 - 1 - i)) : "_");
        }
        return stringBuilder.toString();
    }
}
