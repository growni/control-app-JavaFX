package com.example.control.utils.windows;

public enum URLs {
    WEBSITE("https://control-app-free.netlify.app/");


    private final String url;

    URLs(String url) {
        this.url = url;
    }

    public String getValue() {
        return url;
    }
}
