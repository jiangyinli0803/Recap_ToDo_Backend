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
    private final ChatGPTService chatGPTService;
    public TaskService(TaskRepository repo, IdService idService, ChatGPTService chatGPTService) {
        this.repo = repo;
        this.idService = idService;
        this.chatGPTService = chatGPTService;
    }

    public List<Task> getAllTasks() throws TaskNotFoundException {
        List<Task> tasks = repo.findAll();
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("Task not found");
        }
        return tasks;
    }

    public Task getTaskById(String id) throws TaskNotFoundException {
        return repo.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with ID: " + id + " not found."));
    }

    public Task addTask(TaskDto taskDto) {
        String correctedDescription = chatGPTService.spellingCheck(taskDto.description());
        ;
        Task newTask = new Task(
                idService.randomId(),
                correctedDescription,
                taskDto.status()
        );
        repo.save(newTask);
        return newTask;
    }

    public Task updateTask(Task newTask) throws TaskNotFoundException {
        if(repo.existsById(newTask.id())){
            repo.save(newTask);
            return newTask;
        }else{
            throw new TaskNotFoundException("Task with ID: " + newTask.id() + " not found.");
        }
       }

    public Task deleteTask(String id) throws TaskNotFoundException {
       if(!repo.existsById(id)) {
           throw new TaskNotFoundException("Task with ID: " + id + " not found.");
       }else{
           Task deletedTask = repo.findById(id).get();
           repo.deleteById(id);
           return deletedTask;
       }
    }
}
