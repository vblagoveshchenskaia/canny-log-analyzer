package com.vblagoveshchenskaia.canny.log.analyzer.report;

import com.vblagoveshchenskaia.canny.log.analyzer.data.Frame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportServiceTest {

    private ReportService reportService;

    @BeforeEach
    void beforeEach() {
        reportService = new ReportService();
    }

    @Test
    void print() {
        assertEquals("10000000\t0___00_0\t00000000\t00000000\t01011111\t11100100\t00000000",
                reportService.print(new Frame(0x807200005fe40000L, 0xff8dffffffffff00L, 7)));
    }

    @Test
    void printMainReport() {
        assertEquals("""
                        1.txt
                                   0x0f0	10000000	0___00_0	00000000	00000000	01011111	11100100	00000000
                        2.txt
                                   0x0f0	10000000	01110010	00000000	00000000	01011111	11100100	00000000	10101011
                        """,
                reportService.printMainReport(
                        new TreeMap<>(
                                Map.of("1.txt", Map.of("0x0f0", new Frame(0x807200005fe40000L, 0xff8dffffffffff00L, 7)),
                                        "2.txt", Map.of("0x0f0", new Frame(0x807200005fe400abL, -1L, 8))))));
    }

    @Test
    void printDifferencesReport() {
        assertEquals("""
                        Comparing 1.txt
                                   0x0f0	10000000	0___00_0	00000000	00000000	01011111	00000000	00000000
                                   2.txt	________	________	________	________	________	111__1__	________	10101011
                                                                                                                                                                       
                        """,
                reportService.printDifferencesReport(
                        Map.of("1.txt", Map.of("0x0f0", new Frame(0x807200005f000000L, 0xff8dffffffffff00L, 7)),
                                "2.txt", Map.of("0x0f0", new Frame(0x807200005fe400abL, -1L, 8))),
                        Map.of("0x0f0", Map.of("2.txt", new Frame(0x807200005fe400abL, 0xe400ffL, 8))),
                        "1.txt"));
    }

    @Test
    void printDifferencesReportSkipEqual() {
        assertEquals("""
                        Comparing 1.txt
                        """,
                reportService.printDifferencesReport(
                        Map.of("1.txt", Map.of("0x0f0", new Frame(0x807200005fe400abL, -1L, 8)),
                                "2.txt", Map.of("0x0f0", new Frame(0x807200005fe400abL, -1L, 8))),
                        Map.of("0x0f0", Map.of("2.txt", new Frame(0x807200005fe400abL, 0L, 8))),
                        "1.txt"));
    }

    @Test
    void printDifferencesReportBaseFrameNotPresent() {
        assertEquals("""
                        Comparing 1.txt
                                   0x0f0	not present
                                   2.txt	10000000	01110010	00000000	00000000	01011111	11100100	00000000	10101011
                                                                                                                                                                                                                                     
                        """,
                reportService.printDifferencesReport(
                        Map.of("1.txt", Map.of(),
                                "2.txt", Map.of("0x0f0", new Frame(0x807200005fe400abL, -1L, 8))),
                        Map.of("0x0f0", Map.of("2.txt", new Frame(0x807200005fe400abL, -1L, 8))),
                        "1.txt"));
    }

    @Test
    void printDifferencesReportOtherFrameNotPresent() {
        assertEquals("""
                        Comparing 1.txt
                                   0x0f0	10000000	01110010	00000000	00000000	01011111	00000000	00000000
                                   2.txt	not present
                                                                                                                                                                                                         
                        """,
                reportService.printDifferencesReport(
                        Map.of("1.txt", Map.of("0x0f0", new Frame(0x807200005f000000L, -256L, 7)),
                                "2.txt", Map.of()),
                        Map.of(),
                        "1.txt"));
    }
}