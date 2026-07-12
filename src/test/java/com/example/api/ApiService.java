package com.example.api;

public class ApiService {
    public String fetchUserData() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "{\"name\": \"Real User\", \"email\": \"real@example.com\"}";
    }
}