package com.vblagoveshchenskaia.canny.log.analyzer.ui;

import com.vblagoveshchenskaia.canny.log.analyzer.data.Frame;
import com.vblagoveshchenskaia.canny.log.analyzer.report.ReportService;
import com.vblagoveshchenskaia.canny.log.analyzer.service.FrameParser;
import com.vblagoveshchenskaia.canny.log.analyzer.service.FrameService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Launcher {

    private final FrameParser frameParser;
    private final FrameService frameService;
    private final ReportService reportService;

    @SneakyThrows({ClassNotFoundException.class,
            InstantiationException.class,
            IllegalAccessException.class,
            UnsupportedLookAndFeelException.class})
    public void launch() {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                SwingUtilities.invokeLater(() -> displayError(throwable)));
        SwingUtilities.invokeLater(() -> {
            var mainForm = new MainForm(this);
            mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainForm.setVisible(true);
        });
    }

    public void launchAnalysis(JFrame jFrame, JTextArea jTextArea) {
        var fileDialog = new FileDialog(jFrame, "Open (more then one for logs comparison)");
        fileDialog.setMultipleMode(true);
        fileDialog.setVisible(true);
        var filesArray = fileDialog.getFiles();
        if (filesArray.length == 0) {
            return;
        }
        var files = Stream.of(filesArray)
                .collect(Collectors.toMap(File::getName, Function.identity(), throwingMerger(), TreeMap::new));
        var logToCompare = files.size() > 1 ? new FileCompareDialog(jFrame).selectFile(files.keySet()) : null;
        jTextArea.setText(null);
        var data = files.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey, entry -> parse(entry.getValue()), throwingMerger(), TreeMap::new));
        jTextArea.append(reportService.printMainReport(data));
        if (logToCompare != null) {
            jTextArea.append("\n");
            jTextArea.append(reportService.printDifferencesReport(data, frameService.getDifferences(logToCompare, data), logToCompare));
        }
    }

    @SneakyThrows(IOException.class)
    public void saveResult(JFrame jFrame, JTextArea jTextArea) {
        var fileDialog = new FileDialog(jFrame, "Save", FileDialog.SAVE);
        fileDialog.setVisible(true);
        var files = fileDialog.getFiles();
        if (files.length == 0) {
            return;
        }
        Files.writeString(files[0].toPath(), jTextArea.getText());
    }

    @SneakyThrows(IOException.class)
    private Map<String, Frame> parse(File file) {
        return frameParser.parse(Files.newInputStream(file.toPath()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().reduce(frameService::add).orElseThrow(),
                        throwingMerger(), TreeMap::new));
    }

    private void displayError(Throwable throwable) {
        JOptionPane.showMessageDialog(null,
                throwable.toString(),
                "An error occurred",
                JOptionPane.ERROR_MESSAGE);
    }

    private <T> BinaryOperator<T> throwingMerger() {
        return (one, two) -> {
            throw new IllegalStateException();
        };
    }
}
