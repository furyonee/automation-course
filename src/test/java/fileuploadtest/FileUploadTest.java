package fileuploadtest;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.*;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class FileUploadTest {
    Playwright playwright;
    APIRequestContext request;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        request = playwright.request().newContext();
    }

    @Test
    void testFileUploadAndDownload() {
        byte[] testFile = {
                (byte) 0x89, 0x50, 0x4E, 0x47,
                0x0D, 0x0A, 0x1A, 0x0A
        };

        APIResponse uploadResponse = request.post(
                "https://httpbin.org/post",
                RequestOptions.create().setMultipart(
                        FormData.create().set("file",
                                new FilePayload("test.png", "image/png", testFile))
                )
        );

        String responseBody = uploadResponse.text();
        assertTrue(responseBody.contains("image/png"));

        String base64Data = responseBody.split("\"file\": \"")[1].split("\"")[0];
        byte[] receivedBytes = Base64.getDecoder().decode(base64Data.split(",")[1]);

        assertArrayEquals(testFile, receivedBytes);

        APIResponse downloadResponse =
                request.get("https://httpbin.org/image/png");

        assertEquals("image/png",
                downloadResponse.headers().get("content-type"));

        byte[] content = downloadResponse.body();

        assertEquals(0x89, content[0] & 0xFF);
        assertEquals(0x50, content[1] & 0xFF);
        assertEquals(0x4E, content[2] & 0xFF);
        assertEquals(0x47, content[3] & 0xFF);
    }

    @AfterEach
    void tearDown() {
        request.dispose();
        playwright.close();
    }
}