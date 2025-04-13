package com.example.control.controllers;

import com.example.control.DTO.FeedbackRequest;
import com.example.control.DTO.Session;
import com.example.control.utils.windows.ENDPOINTS;
import com.example.control.utils.windows.PATHS;
import com.example.control.utils.windows.URLs;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileController {

    @FXML private Label profile_welcomeLabel;
    @FXML private TextField profile_username_field;
    @FXML private TextField profile_password_field;
    @FXML private TextField profile_email_field;
    @FXML private TextField profile_new_password_field;
    @FXML private TextArea profile_feedback_field;
    @FXML private Button profile_subscribe_button;
    @FXML private StackPane loadingOverlay;

    private final SceneController sceneController = new SceneController();
    private Utils utilController;


    @FXML
    public void initialize() {
        utilController = new Utils(loadingOverlay);

        profile_username_field.setEditable(false);
        profile_password_field.setEditable(false);
        profile_email_field.setEditable(false);

        String loggedUsername = Session.getUsername();
        if(loggedUsername != null && profile_welcomeLabel != null) {
            profile_welcomeLabel.setText("");
            profile_welcomeLabel.setText("Hi, " + loggedUsername + "! This is your profile page.");
        }

        profile_username_field.setText(Session.getUsername());
        profile_password_field.setText("********");
        profile_email_field.setText(Session.getEmail());

        applySubscriptionStyle(Session.getEmail());
    }

    @FXML
    public void goToMain(ActionEvent e) throws IOException {
        sceneController.switchScene((Node) e.getSource(), PATHS.MAIN.getValue());
    }

    @FXML
    private void handleSaveChanges(ActionEvent event) {
        Task task = new Task() {

            @Override
            protected Object call() throws Exception {
                utilController.showLoading();
                saveChangesOnProfile();

                return null;
            }
        };

        task.setOnSucceeded(e -> utilController.hideLoading());
        task.setOnFailed(e -> utilController.hideLoading());

        new Thread(task).start();
    }

    public void saveChangesOnProfile() {
        String newPassword = profile_new_password_field.getText();

        String username = Session.getUsername();

        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("newPassword", newPassword);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    ENDPOINTS.UPDATE_PASSWORD.getValue(),
                    HttpMethod.PUT,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                utilController.runLaterAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully!");
                profile_new_password_field.clear();

            } else {
                utilController.runLaterAlert(Alert.AlertType.ERROR, "Update Failed", response.getBody());
                throw new RuntimeException();
            }

        } catch (HttpClientErrorException e) {
            utilController.runLaterAlert(Alert.AlertType.ERROR, "Error",  e.getResponseBodyAsString());
            throw new RuntimeException();
        }
    }

    @FXML
    public void handleSubscribe(ActionEvent event) {
        Task task = new Task() {

            @Override
            protected Object call() throws Exception {
                utilController.showLoading();
                subscribe();

                return null;
            }
        };

        task.setOnSucceeded(_ -> utilController.hideLoading());
        task.setOnFailed(_ -> utilController.hideLoading());

        new Thread(task).start();
    }

    public void subscribe() {
        String email = profile_email_field.getText();

        if (email == null || email.isBlank()) {
            utilController.runLaterAlert(Alert.AlertType.WARNING, "Missing Email", "Please provide an email.");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        boolean isSubscribed = isEmailSubscribed(email);

        String url = isSubscribed
                ? ENDPOINTS.UNSUBSCRIBE.getValue()
                : ENDPOINTS.SUBSCRIBE.getValue();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Platform.runLater(() -> applySubscriptionStyle(email));
            } else {
                utilController.runLaterAlert(Alert.AlertType.ERROR, "Failed", response.toString());
                throw new RuntimeException();
            }
        } catch (Exception e) {
            utilController.runLaterAlert(Alert.AlertType.ERROR, "Error", "Could not complete request: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @FXML
    public void handleSendFeedback(ActionEvent e) {

        String email = profile_email_field.getText();
        String feedbackMessage = profile_feedback_field.getText();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                utilController.showLoading();
                sendFeedback(email, feedbackMessage);

                return null;
            }
        };

        task.setOnSucceeded(_ -> utilController.hideLoading());
        task.setOnFailed(_ -> utilController.hideLoading());

        new Thread(task).start();
    }


    private void sendFeedback(String email, String feedbackMessage) {
        if (email == null || email.isBlank() || feedbackMessage == null || feedbackMessage.isBlank()) {
            utilController.runLaterAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill in your feedback.");

            return;
        }

        FeedbackRequest feedbackRequest = new FeedbackRequest(email, feedbackMessage);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FeedbackRequest> request = new HttpEntity<>(feedbackRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(ENDPOINTS.FEEDBACK.getValue(), request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                utilController.runLaterAlert(Alert.AlertType.INFORMATION, "Feedback Sent", "Thank you for your feedback.");
                Platform.runLater(() -> profile_feedback_field.clear());
            } else {
                utilController.runLaterAlert(Alert.AlertType.ERROR, "Failed", response.getBody());
            }
        } catch (Exception e) {
            utilController.runLaterAlert(Alert.AlertType.ERROR, "Error", "Could not send feedback: " + e.getMessage());
        }
    }


    private boolean isEmailSubscribed(String email) {

        RestTemplate restTemplate = new RestTemplate();
        String url = ENDPOINTS.IS_SUBSCRIBED.getValue() + email;

        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);

            if (response.getBody() != null && response.getBody()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error checking subscription status: " + e.getMessage());
            return false;
        }
    }

    private void applySubscriptionStyle(String email) {

        if(isEmailSubscribed(email)) {
            profile_subscribe_button.setText("Unsubscribe 🔥");
            profile_subscribe_button.getStyleClass().setAll("button-subscribed");
        } else {
            profile_subscribe_button.setText("Subscribe and don't miss any updates");
            profile_subscribe_button.getStyleClass().setAll("btn-main");
        }

    }

}
