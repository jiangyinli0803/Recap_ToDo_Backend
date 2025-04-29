package org.example.recap_todo_backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureMockRestServiceServer
public class ChatGPTServiceTest {

   @Autowired
    private ChatGPTService chatGPTService;

   @Autowired
   private MockRestServiceServer mockServer;

  @Test
   void testSpellingCheck() {
       String expected = "This is a test";
       String responseJson = "{\"description\": \"This is a test\"}";

       mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
               .andExpect(method(HttpMethod.POST))
               .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

      String actual = chatGPTService.spellingCheck(expected);

       assertEquals(expected, actual);
      mockServer.verify();
   }



}