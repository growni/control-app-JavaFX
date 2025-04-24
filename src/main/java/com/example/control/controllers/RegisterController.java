package com.example.control.controllers;

import com.example.control.DTO.RegisterRequest;
import com.example.control.utils.windows.ENDPOINTS;
import com.example.control.utils.windows.PATHS;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class RegisterController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final SceneController sceneController = new SceneController();
    private static final String REGISTER_URL = ENDPOINTS.REGISTER.getValue();
    private Utils utilController;

    @FXML private TextField email;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private StackPane loadingOverlay;

    @FXML
    public void initialize() {
        this.utilController = new Utils(loadingOverlay);
    }

    @FXML
    public void handleRegister(ActionEvent event)  {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                utilController.showLoading();
                register();

                return null;
            }
        };

        task.setOnSucceeded(e -> {
            utilController.hideLoading();

            try {
                goToLogin(event);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        task.setOnFailed(e -> utilController.hideLoading());

        new Thread(task).start();
    }

    public void register() {
        RegisterRequest request = new RegisterRequest(username.getText(), password.getText(), email.getText());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(REGISTER_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                utilController.runLaterAlert(Alert.AlertType.INFORMATION, "Success", response.getBody());
            } else {
                utilController.runLaterAlert(Alert.AlertType.ERROR, "Error", response.getBody());
                throw new RuntimeException();
            }
        } catch (HttpClientErrorException ex) {
            String errorMessage = ex.getResponseBodyAsString();
            utilController.runLaterAlert(Alert.AlertType.ERROR, "Error", errorMessage);

            throw new RuntimeException();
        } catch (Exception ex) {
            utilController.runLaterAlert(Alert.AlertType.ERROR, "Error", "Something went wrong: " + ex.getMessage());
            throw new RuntimeException();
        }
    }

    @FXML
    public void goToLogin(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.LOGIN.getValue());
    }

    @FXML
    public void goToMain(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.MAIN.getValue());
    }
}
