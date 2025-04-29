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

@SpringBootTest(properties="chatgpt.api-key=123")        //!!!卡住的点，这里要添加FAke key，Class里面定义了几个就要写几个
@AutoConfigureMockRestServiceServer
public class ChatGPTServiceTest {

   @Autowired
    private ChatGPTService chatGPTService;

   @Autowired
   private MockRestServiceServer mockServer;

  @Test
   void testSpellingCheck() {
       String expected = "This is a test";

       mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
               .andExpect(method(HttpMethod.POST))       //!!!要写对相应model的example Response
               .andRespond(withSuccess("""
                        {    
                          "id": "chatcmpl-abc123",
                          "object": "chat.completion",
                          "created": 1715000000,
                          "model": "gpt-4o-mini",
                          "choices": [
                            {
                              "index": 0,
                              "message": {
                                "role": "assistant",
                                "content": "This is a test"
                              },
                              "finish_reason": "stop"
                            }
                          ],
                          "usage": {
                            "prompt_tokens": 30,
                            "completion_tokens": 10,
                            "total_tokens": 40
                          }
                        }
                        """, MediaType.APPLICATION_JSON));

      String actual = chatGPTService.spellingCheck(expected);

       assertEquals(expected, actual);
      mockServer.verify();
   }



}