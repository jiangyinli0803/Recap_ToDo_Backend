package org.example.recap_todo_backend.dto;

import org.example.recap_todo_backend.model.Status;

public record TaskDto(String title, Status status, String description) {
}
