package org.example.recap_todo_backend.service;

import org.example.recap_todo_backend.Repository.TaskRepository;
import org.example.recap_todo_backend.dto.TaskDto;
import org.example.recap_todo_backend.exceptions.TaskNotFoundException;
import org.example.recap_todo_backend.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;
    private final IdService idService;
    public TaskService(TaskRepository repo, IdService idService) {
        this.repo = repo;
        this.idService = idService;
    }

    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    public Task getTaskById(String id) throws TaskNotFoundException {
        return repo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with ID: " + id + " not found."));
    }

    public Task addTask(TaskDto taskDto) {
        Task newTask = new Task(
                idService.randomId(),
                taskDto.title(),
                taskDto.status(),
                taskDto.description()
        );
        repo.save(newTask);
        return newTask;
    }

    public Task updateTask(String id, Task newTask) throws TaskNotFoundException {
        Task oldTask = repo.findById(id).orElseThrow(()->new TaskNotFoundException("Task with ID: " + id + " not found."));
        return repo.save(oldTask.withTitle(newTask.title())
                .withStatus(newTask.status())
                .withDescription(newTask.description()));
       }

    public void deleteTask(String id) throws TaskNotFoundException {
       if(!repo.existsById(id)) {
           throw new TaskNotFoundException("Task with ID: " + id + " not found.");
       }
       repo.deleteById(id);
    }
}
