package com.example.control.DTO;

public class FeedbackRequest {
    private String email;
    private String message;

    public FeedbackRequest(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public FeedbackRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
