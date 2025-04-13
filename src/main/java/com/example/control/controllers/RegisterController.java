package com.example.control.controllers;

import com.example.control.DTO.RegisterRequest;
import com.example.control.DTO.Session;
import com.example.control.utils.windows.ENDPOINTS;
import com.example.control.utils.windows.PATHS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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

    @FXML
    private TextField email;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    public void handleRegister(ActionEvent e)  {
        RegisterRequest request = new RegisterRequest(username.getText(), password.getText(), email.getText());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(REGISTER_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                showAlert(Alert.AlertType.INFORMATION, "Success", response.getBody());
                goToLogin(e);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", response.getBody());
            }
        } catch (HttpClientErrorException ex) {
            String errorMessage = ex.getResponseBodyAsString();
            showAlert(Alert.AlertType.ERROR, "Error", errorMessage);
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong: " + ex.getMessage());
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
