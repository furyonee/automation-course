package com.example.tests;

import com.example.api.ApiService;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class MockedApiTest extends BaseTest {
    private static ApiService apiService;

    @BeforeAll
    static void setupClass() {
        apiService = mock(ApiService.class);
        when(apiService.fetchUserData()).thenReturn("{\"name\": \"Test User\", \"email\": \"test@example.com\"}");
    }

    @Test
    void testUserProfileWithMockApi() {
        page.navigate("https://the-internet.herokuapp.com/dynamic_content");
        String userData = apiService.fetchUserData();
        page.evaluate("(data) => window.userData = data", userData);
        Object result = page.evaluate("() => window.userData");

        assertNotNull(result);
        assertTrue(result.toString().contains("Test User"));
    }
}