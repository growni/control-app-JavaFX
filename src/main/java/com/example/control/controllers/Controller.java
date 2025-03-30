package com.example.control.controllers;

import com.example.control.utils.windows.Apps;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.synedra.validatorfx.Check;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Controller {

    @FXML private TextArea logArea;
    @FXML private Button button_log;

    @FXML private AnchorPane rootPane;

    private SceneController sceneController = new SceneController();
    private InstallController installController;
    private UninstallController uninstallController;

    @FXML
    public void initialize() {
        Utils utilController = new Utils(logArea);
        installController = new InstallController(utilController);
        uninstallController = new UninstallController(utilController);
    }

    @FXML
    public void goToDebloat(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), "/com/example/control/scenes/debloat.fxml");
    }

    @FXML
    public void install() {
        Set<String> selectedApps = getSelectedApps();
        installController.install(selectedApps);
    }

    @FXML
    public void uninstall() {
        Set<String> selectedApps = getSelectedApps();
        uninstallController.uninstall(selectedApps);
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




