package com.example.control.controllers;

import com.example.control.utils.windows.APPS;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Set;

public class UninstallController {

    @FXML StackPane loadingOverlay;

    private Utils utilController;

    @FXML
    public void initialize() {
        utilController = new Utils(loadingOverlay);
    }
    public void uninstall(Set<String> selectedApps) {
        utilController.log("Uninstall clicked!");

        List<APPS> apps = selectedApps.stream().map(APPS::valueOf).toList();

        utilController.stopPendingInstallations();

        for(int i = 0; i < apps.size(); i++) {
            APPS app = apps.get(i);
           // Set<Path> paths = app.getPaths();
            utilController.runCommand(apps.get(i).getKillCommand(), true, apps.get(i).isRequiresAdmin());

//            if(!paths.isEmpty()) {
//                for (Path path : paths) {
//                    try {
//                        apps.get(i).delete(path);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }


            utilController.runCommand(app.getUninstallCommand(), false, app.isRequiresAdmin());
        }
    }



}
