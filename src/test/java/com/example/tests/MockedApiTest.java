package com.example.tests;

import com.example.api.ApiService;
import com.example.context.TestContext;
import com.microsoft.playwright.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class MockedApiTest {
    private static TestContext context;
    private BrowserContext browserContext;
    private Page page;
    private static ApiService apiService;

    @BeforeAll
    static void setupClass() {
        context = new TestContext();
        apiService = mock(ApiService.class);
        when(apiService.fetchUserData()).thenReturn("{\"name\": \"Test User\", \"email\": \"test@example.com\"}");
    }

    @BeforeEach
    void setup() {
        browserContext = context.getBrowser().newContext();
        page = browserContext.newPage();
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

    @AfterEach
    void tearDown() {
        if (browserContext != null)
            browserContext.close();
    }

    @AfterAll
    static void tearDownClass() {
        if (context != null)
            context.close();
    }
}