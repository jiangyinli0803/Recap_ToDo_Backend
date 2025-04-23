package org.example.recap_todo_backend.controller;

import org.example.recap_todo_backend.dto.TaskDto;
import org.example.recap_todo_backend.exceptions.TaskNotFoundException;
import org.example.recap_todo_backend.model.Task;
import org.example.recap_todo_backend.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService taskService) {
        this.service = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return service.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable String id) throws TaskNotFoundException {
        return service.getTaskById(id);
    }

    @PostMapping
    public Task addTask(@RequestBody TaskDto taskDto) {
        return service.addTask(taskDto);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable String id, @RequestBody Task newtTask) throws TaskNotFoundException {
        return service.updateTask(id, newtTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id) throws TaskNotFoundException {
         service.deleteTask(id);
    }
}
