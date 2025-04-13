package com.example.control.controllers;

import com.example.control.utils.windows.APPS;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Set;

public class InstallController {

    @FXML private StackPane loadingOverlay;
    private Utils utilController;

    @FXML
    public void initialize() {
        utilController = new Utils(loadingOverlay);
    }

    @FXML
    public void fetch(Set<String> selectedApps) {
        List<APPS> apps = selectedApps.stream().map(APPS::valueOf).toList();

        for (int i = 0; i < apps.size(); i++) {
            utilController.runCommand(apps.get(i).getInstallCommand(), true, apps.get(i).isRequiresAdmin());
        }
    }
}
