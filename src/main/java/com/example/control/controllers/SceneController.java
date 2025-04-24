package com.example.control.controllers;

import com.example.control.utils.windows.PATHS;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void switchScene(Node source, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        root = loader.load();

        stage = (Stage) source.getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(PATHS.STYLE_ALL.getValue()).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

}
