package org.example.recap_todo_backend.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
public class IdService {

    public String randomId() {
        return UUID.randomUUID().toString();
    }

}
