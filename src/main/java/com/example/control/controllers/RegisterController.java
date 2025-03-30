package com.example.control.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final SceneController sceneController = new SceneController();

    @FXML
    public void goToLogin(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/control/scenes/login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goToMain(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), "/com/example/control/scenes/main.fxml");
    }
}
