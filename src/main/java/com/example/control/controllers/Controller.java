package com.example.control.controllers;

import com.example.control.DTO.Session;
import com.example.control.utils.windows.APPS;
import com.example.control.utils.windows.PATHS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Controller {

    @FXML private TextArea logArea;
    @FXML private Button button_log;
    @FXML private AnchorPane rootPane;
    @FXML private Label welcomeLabel;
    @FXML private StackPane loadingOverlay;


    private SceneController sceneController = new SceneController();
    private InstallController installController;
    private UninstallController uninstallController;
    private Utils utilController;

    @FXML
    public void initialize() {
        installController = new InstallController();
        uninstallController = new UninstallController();
        utilController = new Utils(logArea, loadingOverlay);

        if(Session.isGuest()) {
            welcomeLabel.setText("Login");
        } else {
            String loggedUsername = Session.getUsername();
            welcomeLabel.setText("Welcome, " + loggedUsername + "!");
        }

    }

    @FXML
    public void fetch() {
        Set<String> selectedApps = getSelectedApps();
        List<APPS> apps = selectedApps.stream().map(APPS::valueOf).toList();

        for (int i = 0; i < apps.size(); i++) {
            utilController.runCommand(apps.get(i).getInstallCommand(), true, apps.get(i).isRequiresAdmin());
        }
    }

    @FXML
    public void uninstall() {
        Set<String> selectedApps = getSelectedApps();

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

    @FXML
    public void goToLogin(MouseEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.LOGIN.getValue());
    }

    @FXML
    public void goToDebloat(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.DEBLOAT.getValue());
    }

    @FXML
    public void goToProfile(MouseEvent event) throws IOException {
        sceneController.switchScene((Node) event.getSource(), PATHS.PROFILE.getValue());
    }

    @FXML
    public void configureGoTo(MouseEvent event) throws IOException {
        if (Session.isGuest()) {
            goToLogin(event);
        } else {
            goToProfile(event);
        }
    }

    private Set<String> getSelectedApps() {
        return rootPane.lookupAll(".check-box")
                .stream()
                .filter(node -> ((CheckBox) node).isSelected())
                .map(node -> node.getId().replace("checkbox", "").toUpperCase())
                .collect(Collectors.toSet());
    }

    @FXML
    public void showLog() {
        boolean isVisible = logArea.isVisible();

        logArea.setVisible(!isVisible);
        logArea.setManaged(!isVisible);

        button_log.setText(isVisible ? "Show log" : "Hide log");
    }
}




