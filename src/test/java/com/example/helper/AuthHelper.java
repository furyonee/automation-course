package com.example.helper;

import com.microsoft.playwright.Page;

public class AuthHelper {
    public static void login(Page page, String username, String password) {
        page.navigate("https://the-internet.herokuapp.com/login");

        page.locator("#username").fill(username);
        page.locator("#password").fill(password);
        page.locator("button[type='submit']").click();
    }
}