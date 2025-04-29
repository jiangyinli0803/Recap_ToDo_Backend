package org.example.recap_todo_backend.model.chatgpt;

import java.util.Collections;
import java.util.List;

public record ChatGPTRequest(String model,
                             List<ChatGPTRequestMessage> messages) {
   public ChatGPTRequest(String message) {
        this("gpt-4o-mini", Collections.singletonList(new ChatGPTRequestMessage("user", message)));
    }
}
