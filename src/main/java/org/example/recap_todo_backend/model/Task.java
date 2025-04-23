package org.example.recap_todo_backend.model;

import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "task")
@With
public record Task(String id, String title, String status, String description) {
}
