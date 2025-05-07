package com.example.control.utils.windows;

public enum PATHS {
    REGISTER("/com/example/control/scenes/register.fxml"),
    MAIN("/com/example/control/scenes/main.fxml"),
    LOGIN("/com/example/control/scenes/login.fxml"),
    RESET_PASSWORD("/com/example/control/scenes/reset-password.fxml"),
    DEBLOAT("/com/example/control/scenes/debloat.fxml"),
    PROFILE("/com/example/control/scenes/profile.fxml"),
    STYLE_ALL("/com/example/control/style.css"),
    ICON_MAIN("file:/M:/study/Control/src/main/resources/com/example/control/images/app_icon.png");

    private final String path;

    PATHS(String path) {
        this.path = path;
    }

    public String getValue() {
        return path;
    }
}
