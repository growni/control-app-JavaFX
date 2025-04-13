package com.example.control.utils.windows;

public enum ENDPOINTS {
    LOGIN_URL("http://localhost:8000/api/auth/login"),
    SEND_PASSWORD_RESET_LINK_URL("http://localhost:8000/api/auth/send-password-reset-link"),
    FEEDBACK("http://localhost:8000/api/profile/feedback"),
    IS_SUBSCRIBED("http://localhost:8000/api/profile/is-subscribed?email="),
    SUBSCRIBE("http://localhost:8000/api/profile/subscribe"),
    UNSUBSCRIBE("http://localhost:8000/api/profile/unsubscribe"),
    REGISTER("http://localhost:8000/api/auth/register");

    private final String url;

    ENDPOINTS(String url) {
        this.url = url;
    }

    public String getValue() {
        return url;
    }
}
