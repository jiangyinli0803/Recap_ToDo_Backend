package org.example.recap_todo_backend.model;

import org.springframework.data.annotation.Id;

//@Document(collection = "task")
public record Task(@Id String id, String description, Status status) {
}
