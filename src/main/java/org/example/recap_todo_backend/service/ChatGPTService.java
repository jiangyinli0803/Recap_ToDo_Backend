package org.example.recap_todo_backend.service;

import org.example.recap_todo_backend.model.chatgpt.ChatGPTRequest;
import org.example.recap_todo_backend.model.chatgpt.ChatGPTRequestMessage;
import org.example.recap_todo_backend.model.chatgpt.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    public ChatGPTService(RestClient.Builder restClientBuilder,
                          @Value("${ChatGPT_API_KEY}") String apiKey,
                          @Value("${ChatGPT_API_URL}") String baseUrl) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }


    public String spellingCheck(String taskDescription) {
        ChatGPTResponse response = restClient.post()
                .body(new ChatGPTRequest("Correct the spelling error in the todo description and return the corrected: " + taskDescription))
                .retrieve()
                .body(ChatGPTResponse.class);
        return response.text();
    }
}
