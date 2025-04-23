package org.example.recap_todo_backend.model;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "task")
@With
public record Task(@Id String id, String title, Status status, String description) {
}
