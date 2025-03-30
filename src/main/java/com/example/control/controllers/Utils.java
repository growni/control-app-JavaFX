package com.example.control.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

    private final TextArea logArea;

    public Utils(TextArea logArea) {
        this.logArea = logArea;
    }

    protected void runCommand(String command, boolean requiresAdmin) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    ProcessBuilder builder;

                    if(requiresAdmin) {
                        String adminCommand = "powershell -Command \"Start-Process cmd -ArgumentList '/c " + command + "' -Verb RunAs\"";
                        builder = new ProcessBuilder("cmd.exe", "/c", adminCommand);
                    } else {
                        builder = new ProcessBuilder("cmd.exe", "/c", command);
                    }

                    builder.redirectErrorStream(true);
                    Process process = builder.start();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String finalLine = line;
                        Platform.runLater(() -> logArea.appendText(finalLine + "\n"));
                    }

                    process.waitFor();
                } catch (IOException | InterruptedException e) {
                    Platform.runLater(() -> logArea.appendText("Error: " + e.getMessage() + "\n"));
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    protected void stopPendingInstallations() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM winget.exe");
            Runtime.getRuntime().exec("taskkill /F /IM msiexec.exe");
            Runtime.getRuntime().exec("taskkill /F /IM setup.exe");
            Runtime.getRuntime().exec("taskkill /F /IM uninstaller.exe");

            Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
            log("Error stopping pending installations: " + e.getMessage());
        }
    }

    protected void log(String message) {
        Platform.runLater(() -> logArea.appendText(message + "\n"));
    }
}
