package com.example.control.DTO;

public class Session {
    private static String username;
    private static String email;
    private static boolean subscribed = false;
    private static boolean isGuest = true;

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername() {
        return username;
    }

    public static String getEmail() {
        return email;
    }

    public static void setSubscribed(boolean isSubscribed) {
        Session.subscribed = isSubscribed;
    }

    public static boolean getSubscribed() {
        return Session.subscribed;
    }

    public static boolean isGuest() {
        return isGuest;
    }

    public static void setGuest(boolean isGuest) {
        Session.isGuest = isGuest;
    }

    public static void setEmail(String email) {
        Session.email = email;
    }

    public static void clear() {
        username = null;
        isGuest = true;
    }
}
