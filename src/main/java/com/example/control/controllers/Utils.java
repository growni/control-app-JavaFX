package com.example.control.controllers;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

    private TextArea logArea;
    @FXML private StackPane loadingOverlay;

    public Utils(TextArea logArea) {
        this.logArea = logArea;
    }

    public Utils(StackPane loadingOverlay) {
        this.loadingOverlay = loadingOverlay;
    }

    public Utils(TextArea logArea, StackPane loadingOverlay) {
        this.logArea = logArea;
        this.loadingOverlay = loadingOverlay;
    }

    public Utils() {
    }

    protected void runCommand(String command, boolean isInstall, boolean requiresAdmin) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                showLoading();

                if (isInstall) {
                    executeInstallCommand(command);
                } else {
                    executeUninstallCommand(command, requiresAdmin);
                }

                return null;
            }
        };

        task.setOnSucceeded(event -> {
            hideLoading();
        });

        task.setOnFailed(event -> {
            hideLoading();
        });

        new Thread(task).start();
    }

    private void executeInstallCommand(String command) {
        try {
            ProcessBuilder builder;

            builder = new ProcessBuilder("cmd.exe", "/c", command);

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
    }

    private void executeUninstallCommand(String command, boolean requiresAdmin) {
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

    protected void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showLoading() {
        Platform.runLater(() -> {
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), loadingOverlay);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            loadingOverlay.setVisible(true);
            fadeIn.play();
        });
    }

    protected void hideLoading() {
        Platform.runLater(() -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), loadingOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(event -> loadingOverlay.setVisible(false));
            fadeOut.play();
        });
    }

    protected void runLaterAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> showAlert(type, title, message));
    }
}
