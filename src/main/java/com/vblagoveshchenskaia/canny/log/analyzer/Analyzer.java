package com.vblagoveshchenskaia.canny.log.analyzer;

import com.vblagoveshchenskaia.canny.log.analyzer.report.ReportService;
import com.vblagoveshchenskaia.canny.log.analyzer.service.FrameParser;
import com.vblagoveshchenskaia.canny.log.analyzer.service.FrameService;
import com.vblagoveshchenskaia.canny.log.analyzer.ui.Launcher;

public class Analyzer {
    public static void main(String[] args) {
        new Launcher(new FrameParser(), new FrameService(), new ReportService()).launch();
    }
}
