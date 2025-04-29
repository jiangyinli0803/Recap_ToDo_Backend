package org.example.recap_todo_backend.controller;

import org.example.recap_todo_backend.Repository.TaskRepository;
import org.example.recap_todo_backend.model.Status;
import org.example.recap_todo_backend.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(properties="chatgpt.api-key=123")
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void getAllTasks_shouldReturnAllTasks() throws Exception {
        //given
        Task task1 = new Task("001", "Aufgabe1", Status.OPEN);
        repo.save(task1);
        //when
        //then
        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {
                        "id": "001",
                        "description": "Aufgabe1",
                        "status": "OPEN"
                        }
                        ]
                        """));
    }

    @Test
    void getTaskById() throws Exception {
      Task task = new Task("001", "Aufgabe1", Status.OPEN);
      repo.save(task);
      mockMvc.perform(get("/api/todo/001"))
              .andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().json("""
            {
                "id": "001",
                "description": "Aufgabe1",
                "status": "OPEN"
                }
            """));
    }

    @Test
    void getTaskById_shouldReturn404_whenCalledInvalidId() throws Exception {
        mockMvc.perform(get("/api/todo/a123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task with ID: a123 not found."));
    }

    @Test
    void addTask() throws Exception {

        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
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
                                "content": "Tests schreiben"
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
        mockMvc.perform(post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                      {
                         "description": "Tests schreiben",
                         "status": "OPEN"                        }
               """))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """ 
                        {
                         "description": "Tests schreiben",
                         "status": "OPEN"
                        }
               """))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void updateTask() throws Exception {
        //given
        Task task = new Task("002", "Tests update", Status.IN_PROGRESS);
        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
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
                                "content": "Tests update"
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
        repo.save(task);
        //when
        mockMvc.perform(put("/api/todo/002").contentType(MediaType.APPLICATION_JSON)
                .content("""
              {
              "id": "002",
              "description": "Tests update",
              "status": "IN_PROGRESS"
              }
              """))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
              {
              "id": "002",
              "description": "Tests update",
              "status": "IN_PROGRESS"
              }
              """
                ));
        //then
    }

    @Test
    void deleteTask() throws Exception {
        //given
        Task todo = new Task("1", "Tests schreiben", Status.OPEN);
        repo.save(todo);
        //when

        //then
        mockMvc.perform(delete("/api/todo/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         "id": "1",
                         "description": "Tests schreiben",
                         "status": "OPEN"
                        }
                        """));
    }


}