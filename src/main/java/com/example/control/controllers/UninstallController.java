package com.example.control.controllers;

import com.example.control.utils.windows.Apps;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

import java.io.File;
import java.util.List;
import java.util.Set;

public class UninstallController {

    private final Utils utilController;

    public UninstallController(Utils utilController) {
        this.utilController = utilController;
    }

    public void uninstall(Set<String> selectedApps) {
        utilController.log("Uninstall clicked!");

        List<Apps> apps = selectedApps.stream().map(Apps::valueOf).toList();

        for(int i = 0; i < apps.size(); i++) {

            utilController.stopPendingInstallations();

            File fileDirectory = new File(apps.get(i).getPath());

            utilController.runCommand(apps.get(i).getKillCommand(), apps.get(i).isRequiresAdmin());
            apps.get(i).delete(fileDirectory);
            utilController.runCommand(apps.get(i).getUninstallCommand(), apps.get(i).isRequiresAdmin());

        }
    }

}
