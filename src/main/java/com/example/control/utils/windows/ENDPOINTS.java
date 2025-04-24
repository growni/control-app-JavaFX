package com.example.control.utils.windows;

public enum ENDPOINTS {
    LOGIN_URL("https://control-app-backend-aqm1.onrender.com/api/auth/login"),
    SEND_PASSWORD_RESET_LINK_URL("https://control-app-backend-aqm1.onrender.com/api/auth/send-password-reset-link"),
    FEEDBACK("https://control-app-backend-aqm1.onrender.com/api/profile/feedback"),
    IS_SUBSCRIBED("https://control-app-backend-aqm1.onrender.com/api/profile/is-subscribed?email="),
    SUBSCRIBE("https://control-app-backend-aqm1.onrender.com/api/profile/subscribe"),
    UNSUBSCRIBE("https://control-app-backend-aqm1.onrender.com/api/profile/unsubscribe"),
    REGISTER("https://control-app-backend-aqm1.onrender.com/api/auth/register"),
    UPDATE_PASSWORD("https://control-app-backend-aqm1.onrender.com/api/profile/update-password");

    // Uncomment for local testing
//    LOGIN_URL("http://localhost:8000/api/auth/login"),
//    SEND_PASSWORD_RESET_LINK_URL("http://localhost:8000/api/auth/send-password-reset-link"),
//    FEEDBACK("http://localhost:8000/api/profile/feedback"),
//    IS_SUBSCRIBED("http://localhost:8000/api/profile/is-subscribed?email="),
//    SUBSCRIBE("http://localhost:8000/api/profile/subscribe"),
//    UNSUBSCRIBE("http://localhost:8000/api/profile/unsubscribe"),
//    REGISTER("http://localhost:8000/api/auth/register"),
//    UPDATE_PASSWORD("http://localhost:8000/api/profile/update-password");

    private final String url;

    ENDPOINTS(String url) {
        this.url = url;
    }

    public String getValue() {
        return url;
    }
}
