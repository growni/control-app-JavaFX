package com.example.control.controllers;

import com.example.control.utils.windows.Apps;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InstallController {

    private final Utils utilController;

    public InstallController(Utils utilController) {
        this.utilController = utilController;
    }


    @FXML
    public void install(Set<String> selectedApps) {
        List<Apps> apps = selectedApps.stream().map(Apps::valueOf).toList();

        for (int i = 0; i < apps.size(); i++) {
            utilController.runCommand(apps.get(i).getInstallCommand(), apps.get(i).isRequiresAdmin());
        }
    }
}
