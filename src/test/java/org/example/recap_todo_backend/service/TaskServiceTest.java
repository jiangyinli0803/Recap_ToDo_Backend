package org.example.recap_todo_backend.service;

import org.example.recap_todo_backend.Repository.TaskRepository;
import org.example.recap_todo_backend.dto.TaskDto;
import org.example.recap_todo_backend.exceptions.TaskNotFoundException;
import org.example.recap_todo_backend.model.Status;
import org.example.recap_todo_backend.model.Task;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    TaskRepository mockRepo = Mockito.mock(TaskRepository.class);
    IdService mockId = Mockito.mock(IdService.class);
    ChatGPTService mockChat = Mockito.mock(ChatGPTService.class);

    @Test
    void getAllTasks_shouldReturnAllTasks_whenCalled() throws TaskNotFoundException {
        //Given
        TaskService taskService = new TaskService(mockRepo, mockId, mockChat);
        Task task1 = new Task("001", "Aufgabe1", Status.OPEN);
        List<Task> expected = List.of(task1);
        Mockito.when(mockId.randomId()).thenReturn("1");
        Mockito.when(mockRepo.findAll()).thenReturn(expected);

        //When
        List<Task> actual =  taskService.getAllTasks();
        //then
        assertEquals(expected, actual);
    }

    @Test
    void getTaskById_shouldReturnTask_whenCalledWithId() throws TaskNotFoundException {
        //Given
        TaskService taskService = new TaskService(mockRepo, mockId, mockChat);
        Task expected = new Task("001", "Aufgabe1", Status.OPEN);
        Mockito.when(mockRepo.findById("001")).thenReturn(Optional.of(expected));

        //When
        Task actual = taskService.getTaskById("001");

        //then
        assertEquals(expected, actual);
    }

    @Test
    void addTask_shouldReturnTask_whenCalledWithDto(){
        //Given
        TaskService taskService = new TaskService(mockRepo, mockId, mockChat);
        Task expected = new Task("001", "Aufgabe1", Status.OPEN);
        Mockito.when(mockId.randomId()).thenReturn("001");
        TaskDto taskDto = new TaskDto("Test to add task", Status.OPEN);
        //When
        Task actual = taskService.addTask(taskDto);
        //then
        assertEquals(expected, actual);
    }

    @Test
    void updateTask() throws TaskNotFoundException {
        //Given
        TaskService taskService = new TaskService(mockRepo, mockId, mockChat);
        Task newTask = new Task("001", "Aufgabe1", Status.OPEN);

        Mockito.when(mockRepo.existsById("001")).thenReturn(true);
        //When
        Task actual = taskService.updateTask(newTask);
        //then
        assertEquals(newTask, actual);
        Mockito.verify(mockRepo).save(newTask);
    }

    @Test
    void updateTask_shouldThrowException_whenCalledWithInvalidId(){
        //Given
        TaskService taskService = new TaskService(mockRepo, mockId, mockChat);
        Task newTask = new Task("001", "Aufgabe1", Status.OPEN);

        Mockito.when(mockRepo.existsById("001")).thenReturn(false);
        //When

        //then
        try{
            taskService.updateTask(newTask);
            fail();
        }catch (TaskNotFoundException e){
            assertTrue(true);
        }

    }

    @Test
    void deleteTask_shouldCallDeleteById_whenCalled() throws TaskNotFoundException {
        //Given
        TaskService taskService = new TaskService(mockRepo, mockId, mockChat);
        Task expected = new Task("001", "Aufgabe1", Status.OPEN);
        Mockito.when(mockRepo.existsById("001")).thenReturn(true);
        Mockito.when(mockRepo.findById("001")).thenReturn(Optional.of(expected));

        //When
        Task actual = taskService.deleteTask("001");
        //then
        assertEquals(expected, actual);
        Mockito.verify(mockRepo).deleteById("001");
    }
}