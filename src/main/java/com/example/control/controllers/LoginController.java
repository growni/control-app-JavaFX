package com.example.control.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final SceneController sceneController = new SceneController();

    @FXML
    public void goToRegister(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), "/com/example/control/scenes/register.fxml");
    }

    @FXML
    public void goToMain(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), "/com/example/control/scenes/main.fxml");
    }
}
