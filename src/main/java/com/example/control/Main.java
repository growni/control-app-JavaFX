package com.example.control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.control.utils.windows.PATHS.ICON_MAIN;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        prepareStage(stage);
        stage.show();
    }

    void prepareStage(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("scenes/login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Control");
        stage.setScene(scene);

        Image icon = new Image(ICON_MAIN.getValue());
        stage.getIcons().add(icon);

        stage.setResizable(false);
    }
}
