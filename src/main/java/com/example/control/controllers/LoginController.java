package com.example.control.controllers;

import com.example.control.DTO.LoginRequest;
import com.example.control.DTO.PasswordResetRequest;
import com.example.control.DTO.Session;
import com.example.control.utils.windows.ENDPOINTS;
import com.example.control.utils.windows.PATHS;
import com.example.control.utils.windows.URLs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gluonhq.attach.util.Util;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LoginController {

    @FXML private TextField login_username_field;
    @FXML private PasswordField login_password_field;
    @FXML private TextField reset_password_email_field;
    @FXML private StackPane loadingOverlay;

    private final String LOGIN_URL = ENDPOINTS.LOGIN_URL.getValue();
    private final String SEND_PASSWORD_RESET_URL = ENDPOINTS.SEND_PASSWORD_RESET_LINK_URL.getValue();

    private final SceneController sceneController = new SceneController();
    private Utils utilController;

    @FXML
    public void initialize() {
        utilController = new Utils(loadingOverlay);
    }

    @FXML
    public void goToRegister(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.REGISTER.getValue());
    }

    @FXML
    public void goToMain(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.MAIN.getValue());
    }

    @FXML
    public void goToResetPassword(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.RESET_PASSWORD.getValue());
    }

    @FXML
    public void goToLogin(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.LOGIN.getValue());
    }

    @FXML
    public void handleLogin(ActionEvent event) {

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                utilController.showLoading();
                login();

                return null;
            }
        };

        task.setOnSucceeded(e -> {
            utilController.hideLoading();

            try {
                goToMain(event);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        task.setOnFailed(e -> utilController.hideLoading());

        new Thread(task).start();
    }

    private void login() {
        LoginRequest request = new LoginRequest(login_username_field.getText(), login_password_field.getText());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(LOGIN_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseBody = mapper.readTree(response.getBody());

                String username = responseBody.get("username").asText();
                String email = responseBody.get("email").asText();

                Session.setUsername(username);
                Session.setEmail(email);
                Session.setGuest(false);

            } else {
                utilController.runLaterAlert(Alert.AlertType.ERROR, "Login Failed", response.getBody());
                throw new RuntimeException();
            }
        } catch (HttpClientErrorException e) {
            utilController.runLaterAlert(Alert.AlertType.ERROR, "Login Failed", e.getResponseBodyAsString());
            throw new RuntimeException();
        } catch (Exception e) {
            utilController.runLaterAlert(Alert.AlertType.ERROR, "Error", "Unexpected error: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @FXML
    public void handleInitiatePasswordReset(ActionEvent event) throws IOException {
        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws Exception {
                utilController.showLoading();
                initiatePasswordReset();

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

    @FXML
    private void handleWebsiteLink() {
        try {
            Desktop.getDesktop().browse(new URI(URLs.WEBSITE.getValue()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void initiatePasswordReset() {
        PasswordResetRequest request = new PasswordResetRequest(reset_password_email_field.getText());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PasswordResetRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(SEND_PASSWORD_RESET_URL, HttpMethod.POST, entity, String.class);

            if(response.getStatusCode() == HttpStatus.OK) {
                utilController.runLaterAlert(Alert.AlertType.INFORMATION, "Password reset initiated.", response.getBody());
            }

        } catch (HttpClientErrorException e) {
            utilController.runLaterAlert(Alert.AlertType.ERROR, "Failed to send reset link.", e.getResponseBodyAsString());
            throw new RuntimeException();
        }
    }

    @FXML
    private void handleGuestLogin(ActionEvent event) throws IOException {
        Session.setGuest(true);
        Session.setUsername(null);

        goToMain(event);
    }
}
