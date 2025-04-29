package org.example.recap_todo_backend.controller;

import org.example.recap_todo_backend.Repository.TaskRepository;
import org.example.recap_todo_backend.model.Status;
import org.example.recap_todo_backend.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;

    @Test
    void getAllTasks_shouldReturnErrorMessage() throws Exception {

        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

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
    void addTask() throws Exception {
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