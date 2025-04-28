package org.example.recap_todo_backend.exceptions;

import java.time.LocalDateTime;

public record ErrorMessage(String message, LocalDateTime timeStamp, int statusCode) {
}
