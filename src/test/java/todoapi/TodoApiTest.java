package todoapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TodoApiTest {
    Playwright playwright;
    APIRequestContext requestContext;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        requestContext = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://jsonplaceholder.typicode.com")
        );
    }

    @Test
    void testTodoApi() throws Exception {
        APIResponse response = requestContext.get("/todos/1");

        assertEquals(200, response.status());

        JsonNode json = objectMapper.readTree(response.text());

        assertTrue(json.has("userId"));
        assertTrue(json.has("id"));
        assertTrue(json.has("title"));
        assertTrue(json.has("completed"));
    }

    @AfterEach
    void tearDown() {
        requestContext.dispose();
        playwright.close();
    }
}