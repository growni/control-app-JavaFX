package com.example.control.utils.windows;

public enum URLs {
    WEBSITE("http://localhost:5173/");


    private final String url;

    URLs(String url) {
        this.url = url;
    }

    public String getValue() {
        return url;
    }
}
