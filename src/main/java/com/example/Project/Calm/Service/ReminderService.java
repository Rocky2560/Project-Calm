package com.example.Project.Calm.Service;

// src/main/java/com/example/calm/service/ReminderService.javax

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {

    @Async
    public void scheduleWelcomeEmail(Long clientId) {
        System.out.println("📧 Welcome email scheduled for client ID: " + clientId);
    }

    @Async
    public void scheduleMissingInfoReminder(Long clientId) {
        System.out.println("⚠️ Missing info reminder scheduled for client ID: " + clientId);
    }
}
