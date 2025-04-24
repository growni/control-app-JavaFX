package com.example.control.controllers;

import com.example.control.utils.windows.PATHS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class DebloatController {

    private final SceneController sceneController = new SceneController();

    @FXML private ListView<String> lv_found_apps;
    @FXML private ListView<String> lv_apps_to_remove;
    @FXML private TextArea logArea;
    @FXML private Button button_log;
    @FXML private SplitPane pane_kill_services_mask_button;

    private final ObservableList<String> foundApps = FXCollections.observableArrayList();
    private final ObservableList<String> appsToRemove = FXCollections.observableArrayList();

    @FXML
    public void goToMain(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.MAIN.getValue());
    }

    @FXML
    public void initialize() {
        lv_found_apps.setItems(foundApps);
        lv_apps_to_remove.setItems(appsToRemove);

        lv_found_apps.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                moveAppToRemoveList();
            }
        });
    }

    @FXML
    private void scanForBloaterApps() {
        foundApps.clear();

        try {
            ProcessBuilder builder = new ProcessBuilder("powershell", "-Command",
                    "Get-AppxPackage | Where-Object { $_.Name -match 'Xbox|Skype|OneNote|Zune|Feedback|3D|People|GetHelp|MixedReality' } | Select-Object -ExpandProperty Name");
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                foundApps.add(line);
            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveAppToRemoveList() {
        String selectedApp = lv_found_apps.getSelectionModel().getSelectedItem();
        if (selectedApp != null) {
            foundApps.remove(selectedApp);
            appsToRemove.add(selectedApp);
        }
    }

    @FXML
    private void removeSelectedApps() {
        List<String> apps = appsToRemove.stream().toList();
        appsToRemove.clear();

        for (String app : apps) {
            try {
                ProcessBuilder builder = new ProcessBuilder("powershell", "-Command",
                        "Get-AppxPackage " + app + " | Remove-AppxPackage");
                builder.redirectErrorStream(true);
                Process process = builder.start();
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void showLog() {
        boolean isVisible = logArea.isVisible();

        logArea.setVisible(!isVisible);
        logArea.setManaged(!isVisible);

        button_log.setText(isVisible ? "Show log" : "Hide log");
    }
}
